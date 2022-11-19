package com.example.movieticketer

import kotlin.random.Random

data class Movie(
    var description: String,
    var movieName: String,  //originalTitle
    var categories: String,  //filmGenre
    var cast: String,
    var director: String,
    var production: String,
    var originalLanguage: String,
    var ageRestriction: Int,
    var showingOn: String,
    var releaseDate: String,
    var screenTime: String, //runningTime
    var aviableTime: ArrayList<String>,
    var posterID: Int,
    var trailerURL: String,
    var sitTaken: ArrayList<String> = initSitTaken()
)

fun initSitTaken(): ArrayList<String> {
    val list: ArrayList<String> =
        arrayListOf("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
    for (i in 1..17) {
        val rand = Random.nextInt(0, 2)
        if (rand == 0)
        {
            list[i] = "0" //not taken
        }
        else{
            list[i] = "1" //taken
        }
    }
    return list
}
