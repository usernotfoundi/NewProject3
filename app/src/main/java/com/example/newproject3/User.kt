package com.example.newproject3

data class User(
    var Brand: String = "",
    var CarName: String = "",
    var engine: String = "",
    var Year: Long = 0,
    var Mileage: Int = 0,
    var LastServiceDate: String = "" // ISO format: yyyy-MM-dd
)
