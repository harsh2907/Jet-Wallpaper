package com.example.jetwallpaper.ui.presentation.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL


object WallpaperUtils{

    suspend fun saveImage(context: Context,url: String, id: String) = withContext(Dispatchers.IO) {

            //Creating bitmap
            val image: Bitmap? = try {
                BitmapFactory.decodeStream(URL(url).openStream())
            } catch (e: IOException) {
                null
            }

            val fileName = "$id.jpg"
            var savedImage = ""
            val storageDir =
                File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)}/JetWallpaper")
            var success = true
            if (!storageDir.exists()) {
                success = storageDir.mkdirs()
            }
            if (success) {
                val imageFile = File(storageDir, fileName)
                savedImage= imageFile.absolutePath

                try {
                    val fOut = FileOutputStream(imageFile)
                    if (image == null) return@withContext false
                    image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                    fOut.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                galleryAddPic(context,savedImage)
            }
            return@withContext true
        }

    private fun galleryAddPic(context: Context,imagePath: String) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val f = File(imagePath)
        val contentUri = Uri.fromFile(f)
        mediaScanIntent.data = contentUri
        context.sendBroadcast(mediaScanIntent)

    }

    fun hasPermissions(context: Context, permissions: List<String>): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }
}