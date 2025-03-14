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

class UpdateHeroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_hero)

        findViewById<Button>(R.id.btnUpdate).setOnClickListener {
            val id = findViewById<EditText>(R.id.etId).text.toString().toInt()
            val name = findViewById<EditText>(R.id.etName).text.toString()
            val age = findViewById<EditText>(R.id.etAge).text.toString().toInt()
            val secretName = findViewById<EditText>(R.id.etSecretName).text.toString()
            updateHero(id, name, age, secretName)
        }
    }

    private fun updateHero(id: Int, name: String, age: Int, secretName: String) {
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIinterface::class.java)

        val updatedHero = MyHeroesItem(id = id, name = name, age = age, secret_name = secretName)
        val retrofitData = api.updateHero(id, updatedHero)

        retrofitData.enqueue(object : Callback<MyHeroesItem?> {
            override fun onResponse(call: Call<MyHeroesItem?>, response: Response<MyHeroesItem?>) {
                if (response.isSuccessful) {
                    showToast("Hero updated successfully")
                    finish()
                } else {
                    showToast("Error: ${response.errorBody()}")
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
