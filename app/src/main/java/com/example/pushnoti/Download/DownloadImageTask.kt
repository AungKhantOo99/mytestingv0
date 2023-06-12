package com.example.pushnoti.Download

import android.os.AsyncTask
import android.os.Environment
import android.util.Log
import android.widget.Toast
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.ProtocolException
import java.net.URL

class DownloadImageTask(
    var imagePath: String?,
    var fileName: String?
) : AsyncTask<Void, Void, Void>() {

    var cachePath: String = ""

    override fun doInBackground(vararg voids: Void?): Void? {
        try {
            Log.d("Download","Downloading")
            val root =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val directoryPath = File(root.absolutePath + "/")
            if (!directoryPath.exists()) {
                directoryPath.mkdir()
            }
            val cachePath = File("$directoryPath/$fileName")
            cachePath.createNewFile()
            Log.d("Download","Downloading $cachePath")
            val buffer = ByteArray(1024)
            var bufferLength: Int
            val url = URL(imagePath)
            val urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.doOutput = false
            urlConnection.connect()
            val inputStream = urlConnection.inputStream
            val fileOutput = FileOutputStream(cachePath)
            while (inputStream.read(buffer).also { bufferLength = it } > 0) {
                fileOutput.write(buffer, 0, bufferLength)
            }
            fileOutput.write(buffer)
            fileOutput.close()
            inputStream.close()
            this.cachePath = cachePath.toString()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: ProtocolException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    override fun onPostExecute(aVoid: Void?) {
        // when the operation is done
        Log.d("Download","Success")
        // you can use this.cachePath here
    }
}