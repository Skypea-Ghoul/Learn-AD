package com.example.myapplication.ui.theme

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

class DeleteHeroActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_hero)

        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            val id = findViewById<EditText>(R.id.etId).text.toString().toInt()
            deleteHero(id)
        }
        findViewById<Button>(R.id.btnCancel).setOnClickListener {
            finish()
        }
    }

    private fun deleteHero(id: Int) {
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

        val retrofitData = api.deleteHero("Bearer $token", id)
        retrofitData.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("DeleteHeroActivity", "Hero deleted successfully")
                    runOnUiThread{
                        Toast.makeText(this@DeleteHeroActivity, "Hero deleted successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@DeleteHeroActivity, MainActivity::class.java))
                        finish()
                    }
                } else {
                    Log.d("DeleteHeroActivity", "Error: ${response.errorBody()}")
                    runOnUiThread{
                        Toast.makeText(this@DeleteHeroActivity, "Error: ${response.errorBody()}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("DeleteHeroActivity", "Error: ${t.message}")
                runOnUiThread{
                    Toast.makeText(this@DeleteHeroActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}