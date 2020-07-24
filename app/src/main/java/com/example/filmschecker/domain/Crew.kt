package com.example.filmschecker.domain

import com.example.filmschecker.R
import com.google.gson.annotations.SerializedName

data class Crew(
    val department: String?=null,
    val gender: Int = 0,
    val id: Int = 0,
    val job: String?=null,
    val name: String?=null,
    @SerializedName("profile_path") var imageUrl: String = R.drawable.unknown.toString()
)