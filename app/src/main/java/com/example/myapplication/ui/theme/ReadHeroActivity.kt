package com.example.myapplication.ui.theme

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ReadHeroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_hero)

        val btnFind = findViewById<Button>(R.id.btnFind)
        val etId = findViewById<EditText>(R.id.etId)

       btnFind.setOnClickListener {
            val idText = etId.text.toString()
           if (idText.isBlank()) {
               showToast("ID tidak boleh kosong")
           } else {
               try {
                   val id = idText.toInt()
                   getHero(id)
               } catch (e: NumberFormatException) {
                   showToast("ID harus berupa angka")
               }
           }
        }
    }

    private fun getHero(id: Int){
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        if(token.isNullOrEmpty()){
            showToast("Token Is Null Or Empty")
            return
        }
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIinterface::class.java)

        val retrofitData = api.getHero("Bearer $token", id)

        retrofitData.enqueue(object : Callback<MyHeroesItem?> {
            override fun onResponse(call: Call<MyHeroesItem?>, response: Response<MyHeroesItem?>) {
                if (response.isSuccessful) {
                    val hero = response.body()
                    if (hero != null) {
                        val heroDetails = """
                        ID: ${hero.id}
                        Name: ${hero.name}
                        Secret Name: ${hero.secret_name}
                        Age: ${hero.age}
                    """.trimIndent()
                        findViewById<TextView>(R.id.txtHeroDetails).text = heroDetails
                    }
                    else {
                        showToast("Hero tidak ditemukan")
                    }
                } else {
                    showToast("Error: ${response.errorBody()?.string() ?: "Unknown error"}")
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