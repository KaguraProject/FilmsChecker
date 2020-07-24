package com.example.filmschecker

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.filmschecker.adapter.FilmAdapter
import com.example.filmschecker.domain.APIParser
import com.example.filmschecker.domain.Category
import com.example.filmschecker.domain.Film
import com.example.filmschecker.service.FilmService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ShowAllFilmsActivity : AppCompatActivity() {

    private var loading = true
    private var page = 1
    private var films : List<Film> = listOf()
    private lateinit var genres: List<Category>

    private lateinit var retrofit: Retrofit
    private lateinit var filmService: FilmService
    private lateinit var filmsRecyclerView: RecyclerView
    private lateinit var filmsAdapter : FilmAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_films)
        filmsAdapter = FilmAdapter(this)
        filmsRecyclerView = findViewById(R.id.films_recycler_view)

        retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.tmdb_base_url))
            .addConverterFactory(GsonConverterFactory.create()).build()
        filmService = retrofit.create(FilmService::class.java)

        filmsRecyclerView.apply {
            adapter = filmsAdapter
        }

        val searchView = findViewById<SearchView>(R.id.search_bar)

        searchView.setOnCloseListener {
            page = 1
            films = emptyList()
            getFilms()
            false
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                page = 1
                films = emptyList()
                getFilmsBySearch(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                page = 1
                Log.i("string",newText)
                films = emptyList()
                getFilmsBySearch(newText)

                return true
            }
        })

        filmsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    if(!loading){
                        page++
                        getFilms()
                    }
                }
            }
        })
        getCategories()
    }

    private fun getCategories(){
        val genresCall : Call<APIParser> = filmService.getGenres(getString(R.string.tmdb_api_key))
        genresCall.enqueue(object: Callback<APIParser>{

            override fun onResponse(
                call: Call<APIParser>,
                response: Response<APIParser>
            ) {
                genres = response.body()?.categories?: emptyList()
                getFilms()
            }
            override fun onFailure(call: Call<APIParser>, t: Throwable) {
                Log.i("error",t.message.toString())
            }
        })
    }

    private fun enqueue(filmsSearchCall: Call<APIParser>){
        filmsSearchCall.enqueue(object : Callback<APIParser>{
            override fun onResponse(call: Call<APIParser>, response: Response<APIParser>) {
                initFilm(response.body()?.films?: emptyList())
            }
            override fun onFailure(call: Call<APIParser>, t: Throwable) {
                Log.i("error",t.message.toString())
            }
        })
    }

    private fun getFilmsBySearch(newText: String){
        enqueue(filmService.getFilmsBySearch(getString(R.string.tmdb_api_key),newText))
    }

    private fun getFilms() {
        enqueue(filmService.getAllFilms(getString(R.string.tmdb_api_key), page))
    }

    private fun initFilm(newFilms: List<Film>) {
        for(f: Film in newFilms){
            f.genres = ArrayList()
            for(i in f.genresId.indices){
                f.genres.add(genres.stream().filter { it.id  == f.genresId[i]}.findFirst().get())
            }
        }
        films= films + newFilms
        filmsAdapter.films = films
        loading = false
        Log.i("init","end init film")
    }
}