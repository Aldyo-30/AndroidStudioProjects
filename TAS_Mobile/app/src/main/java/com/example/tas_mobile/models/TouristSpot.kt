package com.example.tas_mobile.models

import com.google.gson.annotations.SerializedName

data class TouristResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data_wisata") val dataWisata: List<TouristSpot>
)

data class TouristSpot(
    @SerializedName("id_wisata") val idWisata: String = "",
    @SerializedName("nama") val nama: String = "",
    @SerializedName("kategori") val kategori: String = "",
    @SerializedName("alamat") val alamat: String = "",
    @SerializedName("latitude") val latitude: Double = 0.0,
    @SerializedName("longitude") val longitude: Double = 0.0,
    @SerializedName("rating") val rating: Double = 0.0,
    val isOfficial: Boolean = true
)