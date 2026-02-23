package com.seguridadbas.multytenantseguridadbas.controllers.repository

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FileRepository(private val context: Context) {

    fun uriToFile(uri: Uri): File? {
        return try{
            val inputStream  = context.contentResolver.openInputStream(uri) ?: return null
            val fileName = getFileNameFromUri(uri) ?: "temp_photo_${System.currentTimeMillis()}.jpg"
            val file = File(context.cacheDir, fileName)

            inputStream.use { input ->
                file.outputStream().use{ output ->
                    input.copyTo(output)
                }
            }
            file
        }catch (e: Exception){
            e.printStackTrace()
            null
        }
    }




    fun createCameraFileUri(): Pair<Uri, File>{
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val photoFile = File(context.cacheDir, "IMG_$timeStamp.jpg")

        val photoUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            photoFile
        )

        return Pair(photoUri, photoFile)
    }


    private fun getFileNameFromUri(uri: Uri): String? {
        return try{
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use{
                val nameIndex = it.getColumnIndexOrThrow("_display_name")
                it.moveToFirst()
                it.getString(nameIndex)
            }

        }catch (e: Exception){  null  }
    }

    fun getFileSizeInBytes(file: File): Long = file.length()


    fun getStorageKey(fileName: String, tenantId: String): String = "tenant/$tenantId/visitorLog/idPhoto/$fileName"

}


