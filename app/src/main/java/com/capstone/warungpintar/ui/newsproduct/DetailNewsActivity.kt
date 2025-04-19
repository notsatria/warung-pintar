package com.capstone.warungpintar.ui.newsproduct

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.capstone.warungpintar.R

class DetailNewsActivity : AppCompatActivity() {
    companion object {
        const val key_team = "key_team"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detailproduct)
        supportActionBar?.title = "Mengenai Team"

        val dataTeam = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<Team>(key_team, Team::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Team>(key_team)
        }
        val tvDetailName = findViewById<TextView>(R.id.tv_detail_name)
        val tvDetailDescription = findViewById<TextView>(R.id.tv_detail_deskripsi)
        val ivDetailPhoto = findViewById<ImageView>(R.id.iv_detail_gambar)

        if (dataTeam != null) {
            tvDetailName.text = dataTeam.name
            tvDetailDescription.text = dataTeam.description
            ivDetailPhoto.setImageResource(dataTeam.photo)
        }
    }
}