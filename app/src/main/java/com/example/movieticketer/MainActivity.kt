package com.example.movieticketer


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.example.movieticketer.databinding.ActivityMainBinding
import kotlin.collections.ArrayList
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var movieArrayList: ArrayList<Movie>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()


        val blackPanterAviableScreenTime = randTime()

        val minionsAviableScreenTime = randTime()

        val preyAviableScreenTime = randTime()


        val blackPanter = Movie(
            "The nation of Wakanda is pitted against intervening world powers as they mourn the loss of their king T'Challa.",
            "Black Panther: Wakanda Forever",
            "Action, Adventure",
            "Tenoch Huerta, Martin Freeman, Lupita Nyong'o",
            "Ryan Coogler",
            "USA 2022",
            "EN",
            0,
            "Imax, Imax3D, Screen X, 4DX, VIP, Dolby Atoms, 3D, 2D",
            "10-Nov-22",
            "161",
            blackPanterAviableScreenTime,
            R.drawable.black_panther_wakanda_forever_poster,
            "https://youtu.be/RlOB3UALvrQ"
        )

        val minions = Movie(
            "The untold story of one twelve-year-old's dream to become the world's greatest supervillain.",
            "MINIONS: THE RISE OF GRU",
            "Adventure, Animation, Comedy, Kids",
            "Steve Carell, Lucy Lawless, Taraji P. Henson",
            "Kyle Balda",
            "USA 2022",
            "0",
            0,
            "4DX, 2D",
            "30-Jun-22",
            "90",
            minionsAviableScreenTime,
            R.drawable.minions,
            "https://youtu.be/6DxjJzmYsXo"
        )

        val prey = Movie(
            "A nun prepares to perform an exorcism and comes face to face with a demonic force with mysterious ties to her past.",
            "Prey for the Devil",
            "Horror, Thriller",
            "Virginia Madsen, Jacqueline Byers,Colin Salmon",
            "Daniel Stamm",
            "USA 2022",
            "EN",
            14,
            "4DX, 2D",
            "27-Oct-22",
            "93",
            preyAviableScreenTime,
            R.drawable.prey_poster,
            "https://youtu.be/u_jJiZ2oZgs"
        )

        val bros = Movie(
            "Two men with commitment problems attempt a relationship.",
            "MORE INFORMATION ABOUT BROS",
            "Comedy",
            "Kristin Chenoweth, Billy Eichner, Luke Macfarlane",
            "Nicholas Stoller",
            "USA 2022",
            "EN",
            16,
            "2D",
            "20-Oct-22",
            "115",
            preyAviableScreenTime,
            R.drawable.bros_poster,
            "https://youtu.be/BQIeBB9XMe8"
        )

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movies = arrayOf(blackPanter, minions, prey, bros)

        movieArrayList = ArrayList()
        for (i in movies.indices) {

            val movie = Movie(
                movies[i].description,
                movies[i].movieName,
                movies[i].categories,
                movies[i].cast,
                movies[i].director,
                movies[i].production,
                movies[i].originalLanguage,
                movies[i].ageRestriction,
                movies[i].showingOn,
                movies[i].releaseDate,
                movies[i].screenTime,
                movies[i].aviableTime,
                movies[i].posterID,
                movies[i].trailerURL,
                movies[i].sitTaken

            )
            movieArrayList.add(movie)
        }

        binding.listView.isClickable = true
        binding.listView.adapter = MyAdapter(this, movieArrayList)
        binding.listView.setOnItemClickListener { _, _, position, _ ->

            val description = movies[position].description
            val movieName = movies[position].movieName
            val categories = movies[position].categories
            val cast = movies[position].cast
            val director = movies[position].director
            val production = movies[position].production
            val originalLanguage = movies[position].originalLanguage
            val ageRestriction = movies[position].ageRestriction
            val showingOn = movies[position].showingOn
            val releaseDate = movies[position].releaseDate
            val screenTime = movies[position].screenTime
            val aviableTime = movies[position].aviableTime
            val posterId = movies[position].posterID
            val trailerURL = movies[position].trailerURL
            val sitTaken = movies[position].sitTaken

            val i = Intent(this, MovieActivity::class.java)
            i.putExtra("description", description)
            i.putExtra("movieName", movieName)
            i.putExtra("categories", categories)
            i.putExtra("cast", cast)
            i.putExtra("director", director)
            i.putExtra("production", production)
            i.putExtra("originalLanguage", originalLanguage)
            i.putExtra("ageRestriction", ageRestriction.toString())
            i.putExtra("showingOn", showingOn)
            i.putExtra("releaseDate", releaseDate)
            i.putExtra("screenTime", screenTime)
            i.putStringArrayListExtra("aviableTime", aviableTime)
            i.putExtra("posterID", posterId)
            i.putExtra("trailerURL", trailerURL)
            i.putStringArrayListExtra("sitTaken", sitTaken)
            startActivity(i)
        }

    }

    private fun randTime(): ArrayList<String> {

        val timeList: ArrayList<String> = arrayListOf("", "", "", "", "", "")


        for (i in 1..5) {
            val randomHours = Random.nextInt(0, 24)
            val randomMinutes = Random.nextInt(0, 6)
            val randomTime: String =
                if (randomHours in 0..9) "0$randomHours:${randomMinutes}0"
                else "$randomHours:${randomMinutes}0"


            timeList[i] = randomTime
        }

        return timeList
    }

}

