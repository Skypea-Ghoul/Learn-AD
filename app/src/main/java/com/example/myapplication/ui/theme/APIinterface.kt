package com.example.myapplication.ui.theme

import retrofit2.Call
import retrofit2.http.*

interface APIinterface {
    @GET("heroes")
    fun getHeroes(): Call<List<MyHeroesItem>>

    @POST("heroes")
    fun createHero(@Body hero: MyHeroesItem): Call<MyHeroesItem>

    @PUT("heroes/{id}")
    fun updateHero(@Path("id") id: Int, @Body hero: MyHeroesItem): Call<MyHeroesItem>

    @DELETE("heroes/{id}")
    fun deleteHero(@Path("id") id: Int): Call<Void>

    @GET("heroes/{id}")
    fun getHero(@Path("id") id: Int): Call<MyHeroesItem>

}