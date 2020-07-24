package com.example.filmschecker.domain

import com.beust.klaxon.Json
import com.example.filmschecker.R
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Actor(
    val name: String,
    var character: String,
    @SerializedName("profile_path") var imageUrl: String
)