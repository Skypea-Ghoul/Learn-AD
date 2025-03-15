from typing import Optional
from datetime import datetime, timedelta
from jose import JWTError, jwt
from passlib.context import CryptContext
from fastapi.security import OAuth2PasswordBearer, OAuth2PasswordRequestForm

import uvicorn
from fastapi import Depends, FastAPI, HTTPException
from sqlmodel import SQLModel, Field, Session, create_engine, select

class Hero(SQLModel, table=True):
    id: Optional[int] = Field(primary_key=True)
    name: str
    secret_name: str
    age: Optional[int]

class User(SQLModel, table=True):
    id: Optional[int] = Field(default=None, primary_key=True)
    username: str = Field(index=True, unique=True)
    full_name: Optional[str] = None
    email: Optional[str] = None
    hashed_password: str
    disabled: Optional[bool] = None

app = FastAPI()

engine = create_engine("sqlite:///database.db")
SQLModel.metadata.create_all(engine)

def get_session():
    with Session(engine) as session:
        yield session

SECRET_KEY = "kunci_rahasia"
ALGORITHM = "HS256"
ACCESS_TOKEN_EXPIRE_MINUTES = 30

pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")


def verify_password(plain_password, hashed_password):
    return pwd_context.verify(plain_password, hashed_password)

def get_password_hash(password):
    return pwd_context.hash(password)

def create_access_token(data: dict, expires_delta: Optional[timedelta] = None):
    to_encode = data.copy()
    if expires_delta:
        expire = datetime.now() + expires_delta
    else:
        expire = datetime.now() + timedelta(minutes=15)
    to_encode.update({"exp": expire})
    encoded_jwt = jwt.encode(to_encode, SECRET_KEY, algorithm=ALGORITHM)
    return encoded_jwt

def get_current_user(token: str = Depends(oauth2_scheme)):
    credentials_exception = HTTPException(
        status_code=401,
        detail="Could not validate credentials",
        headers={"WWW-Authenticate": "Bearer"},
    )
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        username: str = payload.get("sub")
        if username is None:
            raise credentials_exception
    except JWTError:
        raise credentials_exception
    return username

def get_user_by_username(session: Session, username: str) -> Optional[User]:
    statement = select(User).where(User.username == username)
    return session.exec(statement).first()

def authenticate_user(session: Session, username: str, password: str) -> Optional[User]:
    user = get_user_by_username(session, username)
    if not user:
        return None
    if not verify_password(password, user.hashed_password):
        return None
    return user

@app.post("/token", response_model=dict)
def login_for_access_token(form_data: OAuth2PasswordRequestForm = Depends(), session: Session = Depends(get_session)):
    user = authenticate_user(session, form_data.username, form_data.password)
    if not user:
        raise HTTPException(status_code=401, detail="Incorrect username or password", headers={'WWW-Authenticate': 'Bearer'})
    access_token_expires = timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    access_token = create_access_token(
        data={"sub": user.username}, expires_delta=access_token_expires
        )
    return {"access_token": access_token, "token_type": "bearer"}

@app.post("/heroes", response_model=Hero)
def create_hero(hero: Hero, session: Session = Depends(get_session), current_user: str = Depends(get_current_user)):
    session.add(hero)
    session.commit()
    session.refresh(hero)
    return hero

@app.get("/heroes", response_model=list[Hero])
def get_heroes(skip: int = 0, limit: int = 10, session: Session = Depends(get_session), current_user: str = Depends(get_current_user)):
    heroes = session.exec(select(Hero).offset(skip).limit(limit)).all()
    return heroes

@app.get("/heroes/{hero_id}", response_model=Hero)
def get_hero(hero_id: int, session: Session = Depends(get_session), current_user: str = Depends(get_current_user)):
    hero = session.get(Hero, hero_id)
    if not hero:
        raise HTTPException(status_code=404, detail="Hero not found")
    return hero

@app.put("/heroes/{hero_id}", response_model=Hero)
def update_hero(hero_id: int, hero_data:Hero, session: Session = Depends(get_session), current_user: str = Depends(get_current_user)) :
    hero = session.get(Hero, hero_id)
    if not hero:
        raise HTTPException(status_code=404, detail="Hero not found")

    for field, value in hero_data.model_dump().items():
        setattr(hero, field, value)
    session.commit()
    session.refresh(hero)
    return hero

@app.delete("/heroes/{hero_id}", response_model=Hero)
def delete_hero(hero_id: int, session: Session = Depends(get_session), current_user: str = Depends(get_current_user)) :
    hero = session.get(Hero, hero_id)
    if not hero:
        raise HTTPException(status_code=404, detail="Hero not found")
    session.delete(hero)
    session.commit()
    return hero

def create_sample_user():
    with Session(engine) as session:
        user = get_user_by_username(session, "test")
        if not user:
            sample_user = User(
                username="test",
                hashed_password=get_password_hash("test"),
                full_name="Test User",
                email="test@example.com",
                disabled=False,
            )
            session.add(sample_user)
            session.commit()

if __name__ == "__main__":
    create_sample_user()
    uvicorn.run(app, host="127.0.0.1", port=8000)