package com.example.shared.presentation.util

object Utils{
    //Size in byte
    fun parseSize(fileSize: Int): String {
        val sizeInKb = fileSize / 1024
        return if (sizeInKb / 1024 > 0) "${sizeInKb / 1024} Mb" else "$sizeInKb Kb"
    }
}


