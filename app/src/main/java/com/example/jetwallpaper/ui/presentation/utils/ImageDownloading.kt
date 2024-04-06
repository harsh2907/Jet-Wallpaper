package com.example.jetwallpaper.ui.presentation.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL

object WallpaperUtils{

    suspend fun saveBitmapToStorage(
        context: Context,
        imageUrl:String,
        imageId: String,
        onSuccess:()->Unit,
        onFailed:(Exception)->Unit
    ) {
        withContext(Dispatchers.IO) {
            try {
                val bitmap = BitmapFactory.decodeStream(URL(imageUrl).openStream())
                val fileName = "$imageId.jpg"


                // For Android 10 (Q) and above
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val values = ContentValues().apply {
                        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/JetWallpaper")
                        put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                        put(MediaStore.Images.Media.IS_PENDING, true)
                    }

                    val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                    uri?.let {
                        context.contentResolver.openOutputStream(it)?.use { outputStream ->
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                            values.clear()
                            values.put(MediaStore.Images.Media.IS_PENDING, false)
                            context.contentResolver.update(uri, values, null, null)
                        }
                    }
                } else {
                    // For Android 9 (Pie) and below
                    val directory = File(context.getExternalFilesDir(null), "JetWallpaper")
                    if (!directory.exists()) {
                        directory.mkdirs()
                    }
                    val imageFile = File(directory, "$fileName.jpg")
                    FileOutputStream(imageFile).use { outputStream ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    }
                    onSuccess()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onFailed(e)
            }
        }
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