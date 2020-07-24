package com.example.filmschecker.domain

import com.beust.klaxon.Json
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Film(
    val id: Int = 0,
    val title: String?=null,
    @SerializedName("original_title") val titreOriginal: String?=null,
    @SerializedName("release_date") val date: String?=null,
    val budget :Int = 0,
    @SerializedName("genre_ids")val genresId: IntArray,
    var genres: MutableList<Category> = mutableListOf(),
    val restriction: String?=null,
    val runtime: Int = 0,
    var revenue: Long = 0,
    var tagline: String?=null,
    val overview: String?=null,
    @SerializedName("poster_path") val affiche: String?=null,
    val popularity: Double = 0.0,
    @SerializedName("vote_average") val voteAverage: Double = 0.0
)