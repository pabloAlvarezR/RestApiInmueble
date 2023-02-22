package com.example.restapiInmueble


import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface InmuebleApi {
    companion object{
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.10.30.218:8080/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @GET("inmuebles")
    fun getImuebles(): Call<List<InmuebleResponse>>

    @POST("inmuebles")
    fun altaInmueble(@Body inmueble: InmuebleResponse): Call<InmuebleResponse>

    @DELETE("inmuebles/{id}")
    fun deleteInmueble(@Path("id") id: Long): Call<Void>

    @POST("inmuebles/{id}")
    fun editarInmueble(@Path("id") id: Long, @Body inmueble: InmuebleResponse): Call<InmuebleResponse>

}