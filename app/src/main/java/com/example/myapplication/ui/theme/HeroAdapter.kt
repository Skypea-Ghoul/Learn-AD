package com.example.myapplication.ui.theme

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.layout.Layout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.myapplication.R

class HeroAdapter(private val heroList: List<MyHeroesItem>) : RecyclerView.Adapter<HeroAdapter.HeroViewHolder>() {
    inner class HeroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val heroDescription: TextView = itemView.findViewById(R.id.heroDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hero, parent, false)
        return HeroViewHolder(view)
    }

    override fun getItemCount(): Int = heroList.size

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        val hero =  heroList[position]
        holder.heroDescription.text = "Id: ${hero.id}\nName: ${hero.name}\nSecret: ${hero.secret_name}\nAge: ${hero.age}"
    }


}