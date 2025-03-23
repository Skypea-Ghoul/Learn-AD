# Learn AD

Learn AD adalah proyek pembelajaran Android Studio yang mengintegrasikan backend FastAPI (Python) dengan aplikasi berbasis Kotlin. Proyek ini bertujuan untuk membangun sistem autentikasi yang aman dengan JWT dan menghubungkannya dengan aplikasi klien.

## 📌 Teknologi yang Digunakan
- **Backend:** FastAPI (Python), Uvicorn, SQLModel, OAuth2, JWT
- **Aplikasi Klien:** Kotlin dengan Gradle (Kotlin DSL)
- **Database:** SQLModel (berbasis SQLAlchemy)
- **Dependency Management:** pip (Python), Gradle (Kotlin)

---

## 📂 Struktur Proyek
```
Learn-AD/
├── .idea/                  # Konfigurasi IDE
├── ServerFastAPI/          # Backend FastAPI (Python)
│   ├── main.py             # File utama aplikasi FastAPI
│   ├── requirements.txt    # Daftar dependencies Python
│   └── ...                 # File lainnya
├── app/                    # Kode sumber aplikasi Kotlin
│   └── ...                 # Struktur kode aplikasi Kotlin
├── gradle/                 # Wrapper Gradle
├── build.gradle.kts        # Skrip build utama (Kotlin DSL)
├── gradlew                 # Skrip wrapper Gradle
├── gradlew.bat             # Skrip wrapper Gradle (Windows)
└── settings.gradle.kts     # Konfigurasi Gradle
```

---

## 🚀 Cara Menjalankan

### 1. Menjalankan Backend FastAPI
1. Masuk ke direktori backend:
   ```sh
   cd ServerFastAPI
   ```
2. Install dependencies:
   ```sh
   pip install -r requirements.txt
   ```
3. Jalankan server FastAPI:
   ```sh
   uvicorn main:app --reload
   ```
4. API tersedia di: [http://127.0.0.1:8000](http://127.0.0.1:8000)

### 2. Menjalankan Aplikasi Kotlin
1. Pastikan Gradle telah terinstal atau gunakan wrapper.
2. Jalankan aplikasi Kotlin:
   ```sh
   ./gradlew run
   ```
   *(Untuk Windows: `gradlew.bat run`)*
3. Jika ini adalah aplikasi Android, buka di Android Studio dan jalankan.

---

## 🔗 Integrasi Backend dan Klien
- **Backend FastAPI** menyediakan endpoint untuk autentikasi (login, registrasi, verifikasi token) menggunakan JWT.
- **Aplikasi Kotlin** mengonsumsi API dari backend untuk autentikasi dan pengambilan data.

Pastikan URL backend sudah dikonfigurasi dengan benar di aplikasi klien Kotlin agar integrasi berjalan lancar.

---

## 🤝 Kontribusi
Kami sangat mengapresiasi kontribusi dalam bentuk perbaikan bug, fitur baru, atau peningkatan dokumentasi. Cara berkontribusi:
1. **Fork** repository ini.
2. **Buat branch baru** (`git checkout -b fitur-baru`).
3. **Commit perubahan** (`git commit -m "Menambahkan fitur X"`).
4. **Push** ke repository forked (`git push origin fitur-baru`).
5. **Buat Pull Request** untuk direview.

---

## 📜 Lisensi
Proyek ini dilisensikan di bawah **MIT License** – lihat file LICENSE untuk detail lebih lanjut.

---

## 📞 Kontak
Jika ada pertanyaan atau saran, hubungi:
- **GitHub:** [Skypea-Ghoul](https://github.com/Skypea-Ghoul)

Selamat belajar dan mengembangkan proyek ini! 🚀

