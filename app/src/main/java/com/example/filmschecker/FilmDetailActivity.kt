package com.example.filmschecker

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmschecker.adapter.ActorsAdapter
import com.example.filmschecker.databinding.ActivityFilmsBinding
import com.example.filmschecker.domain.*
import com.example.filmschecker.service.FilmService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.stream.Collectors


class FilmDetailActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityFilmsBinding
    private lateinit var retrofit: Retrofit
    private lateinit var filmService: FilmService
    private lateinit var film: Film
    private var filmId: Int = 0
    private lateinit var actorsRecyclerView: RecyclerView
    private lateinit var actors : List<Actor>
    private lateinit var crew : List<Crew>
    private lateinit var actorsAdapter : ActorsAdapter
    private lateinit var userEmail :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        actorsRecyclerView = binding.actorsRecyclerView
        initRecyclerView()
        filmId = this.intent.extras!!.getInt("film_id")
        userEmail = FirebaseAuth.getInstance().currentUser?.email.toString()


        retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.tmdb_base_url))
            .addConverterFactory(GsonConverterFactory.create()).build()
        filmService = retrofit.create(FilmService::class.java)


        getFilm()
    }

    private fun initRecyclerView() {
        actorsAdapter = ActorsAdapter()
        actorsRecyclerView.apply {
            adapter = actorsAdapter
        }
    }

    private fun getFilm() {
        val filmsCall : Call<Film> = filmService.getOneFilm(filmId,getString(R.string.tmdb_api_key))
        filmsCall.enqueue(object: Callback<Film> {
            override fun onResponse(call: Call<Film>, response: Response<Film>) {
                film = response.body()!!
                initFilm()
                getActors()
            }
            override fun onFailure(call: Call<Film>, t: Throwable) {
                Log.i("error",t.message.toString())
            }
        })
    }

    private fun getActors() {

        val creditsCall : Call<APIParser> = filmService.getActorsByFilm(filmId,getString(R.string.tmdb_api_key))

        creditsCall.enqueue(object : Callback<APIParser>{
            override fun onResponse(call: Call<APIParser>, response: Response<APIParser>) {
                actors = response.body()?.actors?: emptyList()
                crew = response.body()?.crew?: emptyList()
                Log.i("crew size",crew.size.toString())
                actorsAdapter.actors = actors
                setCast()
            }

            override fun onFailure(call: Call<APIParser>, t: Throwable) {}
        })
    }



    private fun initFilm() {
        with(binding){

            manageFavori(true)
            Glide.with(applicationContext)
                .load("https://image.tmdb.org/t/p/w500"+film.affiche)
                .placeholder(R.drawable.ic_launcher_background)
                .into(filmAffiche)
            filmTitle.text = film.title
            filmDate.text = film.date!!.subSequence(0,4)
            filmRestriction.text = film.restriction
            val heure = film.runtime / 60
            val minute = film.runtime % 60
            filmDuration.text = "$heure h $minute min"
            filmDescription.text = film.overview
            var genres = ""
            for(i in film.genres.indices){
                genres += "${film.genres[i].name}"
                if (i != film.genres.size.minus(1)) {
                    genres += ", "
                }
            }
            filmCategories.text = genres
            filmNote.text = "${film.voteAverage}/10"

            val drawable = when {
                film.popularity > 65 -> getDrawable(R.drawable.square_green)
                film.popularity > 40 -> getDrawable(R.drawable.square_orange)
                else -> getDrawable(R.drawable.square_red)
            }
            filmScoreSquare.setImageDrawable(drawable)
            filmPopularity.text = film.popularity.toInt().toString()

        }
    }

    private fun setCast(){
        with(binding){
            val directors: List<Crew> = crew.stream().filter{it.department == "Directing"}.collect(Collectors.toList())
            var directorsString = ""
            for(i in directors.indices){
                directorsString += "${directors[i].name}"
                if(i == 2) break
                if (i != directors.size.minus(1) || i ==2) {
                    directorsString += ", "
                }

            }
            val writers: List<Crew> = crew.stream().filter{it.department == "Writer"}.collect(Collectors.toList())
            var writerssString = ""
            for(i in writers.indices){
                writerssString += "${directors[i].name}"
                if(i == 2) break
                if (i != directors.size.minus(1)) {
                    writerssString += ", "

                }


            }
            filmDirector.text = directorsString
            filmWriters.text = writerssString
        }
    }

    private fun manageFavori(isCheckButton: Boolean){
        database = FirebaseDatabase.getInstance().reference
        database.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                Log.i("error",error.message)
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild("favoris/$userEmail-$filmId".replace(".","*"))){
                    if(!isCheckButton){
                        removeFavori()
                    }else{
                        binding.filmManageFavori.setBackgroundColor(getColor(R.color.herbGreen))
                        binding.filmManageFavori.text = getText(R.string.favori)
                    }
                    Log.i("data","EXISTS")
                }

                else{
                    Log.i("data","EXISTS PAS")
                    if(!isCheckButton)
                        addFavori()
                }
            }
        })
    }
    fun onClickFavori(v: View?=null){
        manageFavori(false)
    }

    private fun addFavori() {
        val f = Favori(filmId, userEmail)
        Log.i("dburl", database.toString())
        database.child("favoris").child("$userEmail-$filmId".replace(".", "*")).setValue(f)
            .addOnSuccessListener {
                Log.i("db", "success")
                with(binding) {
                    filmManageFavori.setBackgroundColor(getColor(R.color.herbGreen))
                    filmManageFavori.text = getText(R.string.favori)
                }
            }
            .addOnFailureListener {
                Log.i("db", it.toString())
            }
    }

    private fun removeFavori(){
        Log.i("data","REMOVE FAV")
        database.child("favoris").child("$userEmail-$filmId".replace(".", "*")).removeValue()
        with(binding){
            filmManageFavori.setBackgroundColor(getColor(R.color.holoDarkBlue))
            filmManageFavori.text = getText(R.string.addFav)
        }
    }
}