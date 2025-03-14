package com.example.myapplication.ui.theme

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CreateHeroActivity : AppCompatActivity() {
    private var highestId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_hero)

        getHighestId()

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            val id = highestId + 1
            val name = findViewById<EditText>(R.id.etName).text.toString()
            val age = findViewById<EditText>(R.id.etAge).text.toString().toInt()
            val secretName = findViewById<EditText>(R.id.etSecretName).text.toString()
            postHero(id, name, age, secretName)
        }
    }

    private fun getHighestId() {
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIinterface::class.java)

        val retrofitData = api.getHeroes()

        retrofitData.enqueue(object : Callback<List<MyHeroesItem>?> {
            override fun onResponse(
                call: Call<List<MyHeroesItem>?>,
                response: Response<List<MyHeroesItem>?>
            ) {
                val responseBody = response.body()!!
                highestId = responseBody.maxOfOrNull { it.id } ?: 0
            }

            override fun onFailure(call: Call<List<MyHeroesItem>?>, t: Throwable) {
                showToast("Error: ${t.message}")
            }
        })
    }

    private fun postHero(id: Int, name: String, age: Int, secretName: String){
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIinterface::class.java)

        val newHero = MyHeroesItem(id = id, name = name, age = age, secret_name = secretName)
        val retrofitData = api.createHero(newHero)

        retrofitData.enqueue(object : Callback<MyHeroesItem?> {
            override fun onResponse(call: Call<MyHeroesItem?>, response: Response<MyHeroesItem?>) {
                if(response.isSuccessful){
                    showToast("Hero created successfully")
                    finish()
                } else{
                    showToast("Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<MyHeroesItem?>, t: Throwable) {
                showToast("Error: ${t.message}")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}