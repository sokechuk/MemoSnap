package com.example.MemoSnap.ui

import com.example.MemoSnap.R

data class PhotoItem(
    val imageRes: Int,
    val title: String,
    val description: String,
    val hashtags: List<String>,
    val date: String,
    val userImage: Boolean,
    val group: String
)

val samplePhotos = listOf(
    PhotoItem(
        R.drawable.family1, 
        "Moonwalk", 
        "My graduation vacation to the moon.",
        listOf("#moon", "#space", "#adventure"), 
        "2024-07-01", 
        true, 
        "Family Pics"
    ),
    PhotoItem(
        R.drawable.family2, 
        "Family BBQ", 
        "Summer BBQ with the family.",
        listOf("#bbq", "#family", "#summer"), 
        "2024-06-10", 
        false, 
        "Family Pics"
    ),
    PhotoItem(
        R.drawable.family3, 
        "Family Reunion", 
        "Annual family reunion.",
        listOf("#reunion", "#family", "#fun"), 
        "2024-05-15", 
        true, 
        "Family Pics"
    ),
    PhotoItem(
        R.drawable.family4, 
        "Holiday Dinner", 
        "Festive dinner with loved ones.",
        listOf("#holiday", "#dinner", "#family"), 
        "2024-04-20", 
        false, 
        "Family Pics"
    ),
    PhotoItem(
        R.drawable.car1, 
        "My Car", 
        "A birthday gift. I really wanted a Ferrari but this will do.",
        listOf("#car", "#gift", "#birthday"), 
        "2024-06-15", 
        true, 
        "My Cars"
    ),
    PhotoItem(
        R.drawable.car2, 
        "Road Trip", 
        "Driving along the coast.",
        listOf("#roadtrip", "#car", "#coast"), 
        "2024-05-22", 
        false, 
        "My Cars"
    ),
    PhotoItem(
        R.drawable.car3, 
        "Car Show", 
        "Classic cars everywhere.",
        listOf("#classic", "#carshow", "#vintage"), 
        "2024-04-18", 
        false, 
        "My Cars"
    ),
    PhotoItem(
        R.drawable.car4, 
        "Garage", 
        "My collection grows.",
        listOf("#garage", "#collection", "#cars"), "2024-03-30", 
        false, 
        "My Cars"
    ),
    PhotoItem(
        R.drawable.space1, 
        "Space Walk", 
        "Floating above Earth.",
        listOf("#space", "#walk", "#earth"), 
        "2024-07-04", 
        true, 
        "Space Travel"
    ),
    PhotoItem(
        R.drawable.space2, 
        "Rocket Launch", 
        "Witnessed a launch up close.",
        listOf("#rocket", "#launch", "#space"),
        "2024-06-12", 
        false, 
        "Space Travel"
    ),
    PhotoItem(
        R.drawable.space3, 
        "Mars Rover", 
        "Saw the rover in action.",
        listOf("#mars", "#rover", "#explore"), 
        "2024-05-25", 
        false, 
        "Space Travel"
    ),
    PhotoItem(
        R.drawable.sample1, 
        "Lunar Base", 
        "Visited the moon base.",
        listOf("#lunar", "#base", "#moon"), 
        "2024-04-14", 
        false, 
        "Space Travel"
    ),
    PhotoItem(
        R.drawable.vacation1, 
        "Beach Day", 
        "Relaxing by the ocean.",
        listOf("#beach", "#ocean", "#vacation"), 
        "2024-07-10", 
        true, 
        "Vacation Pics"
    ),
    PhotoItem(
        R.drawable.vacation2, 
        "Mountain Hike", 
        "Reached the summit.",
        listOf("#mountain", "#hike", "#adventure"), 
        "2024-06-18", 
        false, 
        "Vacation Pics"
    ),
    PhotoItem(
        R.drawable.vacation3, 
        "City Lights", 
        "Make memories you can't remember.",
        listOf("#city", "#lights", "#memories"), 
        "2024-03-05", 
        false, 
        "Vacation Pics"
    ),
    PhotoItem(
        R.drawable.vacation4, 
        "Desert Safari", 
        "Exploring the dunes.",
        listOf("#desert", "#safari", "#explore"), 
        "2024-05-28", 
        false, 
        "Vacation Pics"
    )
)