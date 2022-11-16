package com.example.jetwallpaper.ui.presentation.utils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri


fun Context.shareUrl(url:String){
    var link = url
    if (!url.startsWith("https://") && !url.startsWith("http://")){
        link = "http://$url";
    }

    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT,link)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(intent,"Share this wallpaper!")
    startActivity(shareIntent)

}

fun Context.openUrl(url:String){
    var link = url
    if (!url.startsWith("https://") && !url.startsWith("http://")){
        link = "http://$url";
    }
    startActivity(Intent(Intent.ACTION_VIEW,link.toUri()))
}

//Size in byte
fun parseSize(fileSize:Int):String{
    val sizeInKb = fileSize/1024
    return if(sizeInKb/1024>0) "${sizeInKb/1024} Mb" else "$sizeInKb Kb"
}