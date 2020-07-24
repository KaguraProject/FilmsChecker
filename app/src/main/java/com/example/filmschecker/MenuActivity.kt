package com.example.filmschecker

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
    }

    fun showSearch(v: View?) {
        val i = Intent(this, ShowAllFilmsActivity::class.java)
        startActivity(i)
    }

    fun showFavoris(v: View?) {
        val i = Intent(this, LikedFilmsActivity::class.java)
        startActivity(i)
    }
}