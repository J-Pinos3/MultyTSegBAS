package com.seguridadbas.multytenantseguridadbas.controllers.repository

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FileRepository(private val context: Context) {

    fun uriToFile(uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val fileName = getFileNameFromUri(uri) ?: "temp_photo_${System.currentTimeMillis()}.jpg"

            // Crear directorio si no existe
            val photoDir = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "visitor_photos")
            if (!photoDir.exists()) {
                photoDir.mkdirs()
            }

            val file = File(photoDir, fileName)

            inputStream.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }




    fun createCameraFileUri(): Pair<Uri, File> {
        try {
            // Crear directorio para fotos
            val photoDir = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "visitor_photos")
            if (!photoDir.exists()) {
                photoDir.mkdirs()
            }

            // Crear nombre único para la foto
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val photoFile = File(photoDir, "IMG_$timeStamp.jpg")

            // Crear URI usando FileProvider
            val photoUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                photoFile
            )

            android.util.Log.d("FileRepository", "Camera URI created: $photoUri")
            android.util.Log.d("FileRepository", "Photo file path: ${photoFile.absolutePath}")

            return Pair(photoUri, photoFile)
        } catch (e: Exception) {
            android.util.Log.e("FileRepository", "Error creating camera URI", e)
            throw e
        }
    }


    private fun getFileNameFromUri(uri: Uri): String? {
        return try {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                val nameIndex = it.getColumnIndexOrThrow("_display_name")
                it.moveToFirst()
                it.getString(nameIndex)
            }
        } catch (e: Exception) {
            null
        }
    }

    fun getFileSizeInBytes(file: File): Long = file.length()


    fun getStorageKey(fileName: String, tenantId: String): String = "tenant/$tenantId/visitorLog/idPhoto/$fileName"

}


