package com.example.pushnoti.model.pageresponse

data class Info(
    val page: Int,
    val results: Int,
    val seed: String,
    val version: String
)