package com.example.filmschecker.adapter

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmschecker.FilmDetailActivity
import com.example.filmschecker.R
import com.example.filmschecker.domain.Film

class FilmAdapter(private val context: Context) : RecyclerView.Adapter<FilmsViewHolder>() {
    var films: List<Film> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmsViewHolder {
        return FilmsViewHolder(parent.inflate(R.layout.layout_films))
    }

    override fun getItemCount() = films.size

    override fun onBindViewHolder(holder: FilmsViewHolder, position: Int) {
        val film = films[position]
        holder.itemView.setOnClickListener {
            val intent = Intent(context, FilmDetailActivity::class.java)
            intent.putExtra("film_id",film.id)
            context.startActivity(intent)
        }
        holder.bindData(film)
    }
}

class FilmsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val context: Context = itemView.context
    private var afficheIv: ImageView = itemView.findViewById(R.id.film_affiche)
    private var synopsisTv: TextView = itemView.findViewById(R.id.film_overview)
    private var titreTv: TextView = itemView.findViewById(R.id.film_titre)
    private var genresTv: TextView = itemView.findViewById(R.id.film_genres)

    fun bindData(film: Film) {
        Glide.with(context)
            .load("https://image.tmdb.org/t/p/w500"+film.affiche)
            .placeholder(R.drawable.ic_launcher_background)
            .into(afficheIv)
        titreTv.text = film.title
        var genres = "Catégories: "
        for(i in film.genres.indices){
            genres += "${film.genres[i].name}"
            if (i != film.genres.size.minus(1)) {
                genres += ", "
            }
        }
        synopsisTv.text = film.overview
        genresTv.text = genres
    }
}