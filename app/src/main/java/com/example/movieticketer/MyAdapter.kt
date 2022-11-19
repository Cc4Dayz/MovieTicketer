package com.example.movieticketer

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import java.util.zip.Inflater

class MyAdapter(
    private val context: Activity,
    private val arrayList: ArrayList<Movie>
) : ArrayAdapter<Movie>(context, R.layout.movies_list_template, arrayList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.movies_list_template, null)


        //all views in template.
        val movieName: TextView = view.findViewById(R.id.movieTitle)
        val category: TextView = view.findViewById(R.id.movieCategory)
        val description: TextView = view.findViewById(R.id.movieDiscription)
        val posterID: ImageView = view.findViewById(R.id.posterImage)

        movieName.text = arrayList[position].movieName
        category.text = "${arrayList[position].categories} | ${arrayList[position].screenTime} Minutes"
        description.text = arrayList[position].description
        posterID.setImageResource(arrayList[position].posterID)

        return view
    }

}