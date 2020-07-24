package com.example.filmschecker.domain

import com.google.gson.annotations.SerializedName

data class APIParser (
    @SerializedName("total_results") val totalResults: Int?=null,
    @SerializedName("total_pages") val totalPages: Int?=null,
    @SerializedName("results") val films: List<Film>?=null,
    @SerializedName("genres") val categories: List<Category>?=null,
    @SerializedName("cast") val actors: List<Actor>?=null,
    @SerializedName("crew") val crew: List<Crew>?=null
)
