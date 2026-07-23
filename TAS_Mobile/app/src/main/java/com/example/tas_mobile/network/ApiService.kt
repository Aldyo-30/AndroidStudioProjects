package com.example.tas_mobile.network

import com.example.tas_mobile.models.TouristResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("a8d23a9c-b132-487c-b492-a09454ae3574")
    fun getOfficialSpots(): Call<TouristResponse>
}