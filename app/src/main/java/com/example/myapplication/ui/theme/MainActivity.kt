package com.example.myapplication.ui.theme

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R

const val BASE_URL = "http://10.0.2.2:8000/"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        if(token.isNullOrEmpty()){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
    findViewById<Button>(R.id.btnCreate).setOnClickListener {
        startActivity(Intent(this, CreateHeroActivity::class.java))
    }

        findViewById<Button>(R.id.btnRead).setOnClickListener {
            startActivity(Intent(this, ReadHeroActivity::class.java))
        }
        findViewById<Button>(R.id.btnReadAll).setOnClickListener {
            startActivity(Intent(this, ReadAllHeroActivity::class.java))
        }
        findViewById<Button>(R.id.btnUpdate).setOnClickListener {
            startActivity(Intent(this, UpdateHeroActivity::class.java))
        }
        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            startActivity(Intent(this, DeleteHeroActivity::class.java))
        }

        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            logout()
        }
    }

    private fun logout(){
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("token")
        editor.apply()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}