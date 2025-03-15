package com.example.myapplication.ui.theme

import retrofit2.Call
import retrofit2.http.*

interface APIinterface {
    @GET("heroes")
    fun getHeroes(@Header("Authorization") token: String): Call<List<MyHeroesItem>>

    @POST("heroes")
    fun createHero(@Header("Authorization") token: String, @Body hero: MyHeroesItem): Call<MyHeroesItem>

    @PUT("heroes/{id}")
    fun updateHero(@Header("Authorization") token: String, @Path("id") id: Int, @Body hero: MyHeroesItem): Call<MyHeroesItem>

    @DELETE("heroes/{id}")
    fun deleteHero(@Header("Authorization") token: String, @Path("id") id: Int): Call<Void>

    @GET("heroes/{id}")
    fun getHero(@Header("Authorization") token: String, @Path("id") id: Int): Call<MyHeroesItem>

    @POST("token")
    fun getToken(@Body form: Map<String, String>): Call<Map<String, String>>
}