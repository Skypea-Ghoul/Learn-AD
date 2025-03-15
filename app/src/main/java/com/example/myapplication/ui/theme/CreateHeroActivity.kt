package com.example.myapplication.ui.theme

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
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
    private var retrofitCall: Call<List<MyHeroesItem>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_hero)

        getHighestId()

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            val name = findViewById<EditText>(R.id.etName).text.toString().trim()
            val ageText = findViewById<EditText>(R.id.etAge).text.toString().trim()
            val secretName = findViewById<EditText>(R.id.etSecretName).text.toString().trim()

            // Validasi input
            if (name.isEmpty() || ageText.isEmpty() || secretName.isEmpty()) {
                showToast("Semua field harus diisi")
                return@setOnClickListener
            }

            val age = try {
                ageText.toInt()
            } catch (e: NumberFormatException) {
                showToast("Masukkan usia yang valid")
                return@setOnClickListener
            }

            // Pastikan highestId sudah diambil dari API
            val id = highestId + 1
            postHero(id, name, age, secretName)
        }

        findViewById<Button>(R.id.btnCancel).setOnClickListener {
            finish()
        }
    }

    private fun getHighestId() {
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        if (token.isNullOrEmpty()) {
            showToast("Token Is Null Or Empty")
            return
        }
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIinterface::class.java)

        retrofitCall = api.getHeroes("Bearer $token")
        retrofitCall?.enqueue(object : Callback<List<MyHeroesItem>?> {
            override fun onResponse(
                call: Call<List<MyHeroesItem>?>,
                response: Response<List<MyHeroesItem>?>
            ) {
                val responseBody = response.body()
                if (responseBody != null && responseBody.isNotEmpty()) {
                    highestId = responseBody.maxOf { it.id }
                }
            }

            override fun onFailure(call: Call<List<MyHeroesItem>?>, t: Throwable) {
                showToast("Error: ${t.message}")
            }
        })
    }

    private fun postHero(id: Int, name: String, age: Int, secretName: String) {
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        if (token.isNullOrEmpty()) {
            showToast("Token Is Null Or Empty")
            return  // Hentikan eksekusi jika token tidak valid
        }
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIinterface::class.java)

        val newHero = MyHeroesItem(id = id, name = name, age = age, secret_name = secretName)
        val retrofitData = api.createHero("Bearer $token", newHero)
        retrofitData.enqueue(object : Callback<MyHeroesItem?> {
            override fun onResponse(call: Call<MyHeroesItem?>, response: Response<MyHeroesItem?>) {
                if (response.isSuccessful) {
                    val hero = response.body()
                    if (hero != null) {
                        showToast("Hero created successfully")
                        finish()
                    } else {
                        showToast("Error: Hero is null")
                    }
                } else {
                    showToast("Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<MyHeroesItem?>, t: Throwable) {
                showToast("Error: ${t.message}")
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        retrofitCall?.cancel()
        hideKeyboard()
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
