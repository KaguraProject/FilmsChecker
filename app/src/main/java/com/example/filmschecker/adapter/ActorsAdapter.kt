package com.example.filmschecker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmschecker.R
import com.example.filmschecker.domain.Actor

class ActorsAdapter : RecyclerView.Adapter<ActorViewHolder>() {

    var actors: List<Actor> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        return ActorViewHolder(parent.inflate(R.layout.layout_actor))
    }

    override fun getItemCount() = actors.size

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        val weapon = actors[position]
        holder.bindData(weapon)
    }
}

class ActorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var pictureImv: ImageView = itemView.findViewById(R.id.film_actor_photo)
    private var acteurTxv: TextView = itemView.findViewById(R.id.film_acteur_nom)
    private var roleTxv: TextView = itemView.findViewById(R.id.film_acteur_role)

    fun bindData(actor: Actor) {
        val context = itemView.context
        if(actor.imageUrl == null){
            Glide.with(context)
                .load(R.drawable.unknown)
                .placeholder(R.drawable.ic_launcher_background)
                .into(pictureImv)
        }
        else {
            Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500"+actor.imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(pictureImv)
        }

        acteurTxv.text = actor.name
        roleTxv.text = actor.character
    }
}

fun ViewGroup.inflate(@LayoutRes layout: Int): View {
    return LayoutInflater.from(context).inflate(layout, this, false)
}