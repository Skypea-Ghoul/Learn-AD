package com.example.myapplication.ui.theme

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ReadAllHeroActivity : AppCompatActivity() {

    private val listHero = ArrayList<MyHeroesItem>()
    private lateinit var Adapter: HeroAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_all_hero)

        recyclerView = findViewById(R.id.recycleView)

        Adapter = HeroAdapter(listHero)
        recyclerView.adapter = Adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        getAllHeroes()
    }

    private fun getAllHeroes(){
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

        val retrofitData = api.getHeroes("Bearer $token")

        retrofitData.enqueue(object : Callback<List<MyHeroesItem>?> {
            override fun onResponse(
                call: Call<List<MyHeroesItem>?>,
                response: Response<List<MyHeroesItem>?>
            ) {
                if (response.isSuccessful) {
                    listHero.clear()
                    response.body()?.let {
                        listHero.addAll(it)
                    }
                    Adapter.notifyDataSetChanged()
                } else{
                    showToast("Gagal Memuat Hero, Error: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<List<MyHeroesItem>?>, t: Throwable) {
                showToast("Error: ${t.message}")
            }
        })
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}