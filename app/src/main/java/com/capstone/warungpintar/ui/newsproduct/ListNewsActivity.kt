package com.capstone.warungpintar.ui.newsproduct

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.warungpintar.R

class ListNewsActivity : AppCompatActivity() {
    private lateinit var rvTeam: RecyclerView
    private val list = ArrayList<Team>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_news)
 //     supportActionBar!!.title = "Team Basket Indonesia"

        rvTeam = findViewById(R.id.rv_team)
        rvTeam.setHasFixedSize(true)

        list.addAll(getListTeam())
        showRecyclerList()
    }


    private fun getListTeam(): ArrayList<Team> {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val listTeam = ArrayList<Team>()
        for (i in dataName.indices) {
            val hero = Team(dataName[i], dataDescription[i], dataPhoto.getResourceId(i, -1))
            listTeam.add(hero)
        }
        return listTeam
    }

    private fun showRecyclerList() {
        rvTeam.layoutManager = LinearLayoutManager(this)
        val listTeamAdapter = ListTeamAdapter(list)
        rvTeam.adapter = listTeamAdapter

        listTeamAdapter.setOnItemClickCallback(object : ListTeamAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Team) {
                showSelectedHero(data)
            }
        })
    }

    private fun showSelectedHero(hero: Team) {
        Toast.makeText(this, "Membuka " + hero.name, Toast.LENGTH_SHORT).show()
    }
}