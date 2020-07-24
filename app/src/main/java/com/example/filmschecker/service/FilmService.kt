package com.example.filmschecker.service

import com.example.filmschecker.domain.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface FilmService {
    @GET("genre/movie/list?&language=fr")
    fun getGenres(@Query("api_key") apiKey: String): Call<APIParser>

    @GET("discover/movie?sort_by=popularity.desc&include_adult=false&include_video=false&language=fr")
    fun getAllFilms(@Query("api_key") apiKey: String, @Query("page") nbPage: Int): Call<APIParser>

    @GET("movie/{film_id}?language=fr")
    fun getOneFilm(@Path("film_id") filmId: Int, @Query("api_key") apiKey: String) : Call<Film>

    @GET("movie/{film_id}/credits")
    fun getActorsByFilm(@Path("film_id") apiKey: Int, @Query("api_key") filmId: String): Call<APIParser>

    @GET("search/movie?language=fr&popularity.desc")
    fun getFilmsBySearch(@Query("api_key") apiKey: String, @Query("query") query: String): Call<APIParser>
}