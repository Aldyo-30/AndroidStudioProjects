package com.example.tas_mobile.models

data class CitizenSpot(
    val id: String = "",
    val nama_spot: String = "",
    val kategori: String = "",
    val deskripsi: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val ditambahkan_pada: String = ""
) {
    fun toTouristSpot(): TouristSpot {
        return TouristSpot(
            idWisata = id,
            nama = nama_spot,
            kategori = kategori,
            alamat = deskripsi,
            latitude = latitude,
            longitude = longitude,
            rating = 0.0,
            isOfficial = false
        )
    }
}