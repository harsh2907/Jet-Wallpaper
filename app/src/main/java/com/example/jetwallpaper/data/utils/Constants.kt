package com.example.jetwallpaper.data.utils

import com.example.jetwallpaper.BuildConfig


object Constants {
    const val API_KEY = BuildConfig.API_KEY
    const val NEW = "vaporwave||retrowave||noir||outrun||cyberpunk||japan||anime"
    const val POPULAR = "minimal||vaporwave||retrowave||noir"
    const val BASE_URL = "https://wallhaven.cc"
    const val PAGE_SIZE = 12

    object SortingParams{
        const val popular = "views"
    }


    val wallpaperThemes = listOf(
        "nature",
        "animals",
        "landscapes",
        "cities",
        "abstract",
        "flowers",
        "ocean",
        "mountains",
        "space",
        "animation",
        "art",
        "sunsets",
        "waterfalls",
        "desert",
        "animated characters",
        "sports",
        "black and white",
        "vintage",
        "cityscapes",
        "beaches",
        "cars",
        "fantasy",
        "garden",
        "graffiti",
        "hot air balloons",
        "horses",
        "jungle",
        "lakes",
        "lighthouses",
        "marble",
        "minimalist",
        "monochrome",
        "moon",
        "ocean life",
        "patterns",
        "peacocks",
        "photography",
        "plants",
        "quotes",
        "rainbows",
        "red",
        "religion",
        "retro",
        "roses",
        "scenery",
        "sea creatures",
        "sky",
        "snow",
        "space travel",
        "stars",
        "tigers",
        "tropical",
        "underwater",
        "unicorns",
        "vapourwave",
        "villages",
        "water",
        "waves",
        "weddings",
        "wildlife",
        "wood",
        "yellow",
        "zen"
    )

}