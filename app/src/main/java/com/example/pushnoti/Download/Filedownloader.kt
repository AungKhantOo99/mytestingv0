package com.example.pushnoti.Download

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.fragment.app.FragmentActivity

class FileDownloader(private val activity: FragmentActivity) {

    private var downloadId: Long = 0

    private val downloadReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
                val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadId) {
                    Toast.makeText(activity, "Download completed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun downloadFile(fileUrl: String, fileName: String) {
        if (!checkPermission()) {
            requestPermission()
            return
        }
        val request = DownloadManager.Request(Uri.parse(fileUrl))
            .setTitle(fileName)
            .setDescription("Downloading")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

        val downloadManager = activity.getSystemService<DownloadManager>()
        downloadId = downloadManager?.enqueue(request) ?: -1

        registerDownloadReceiver()

        Toast.makeText(activity, "Download started", Toast.LENGTH_SHORT).show()
    }

    private fun checkPermission(): Boolean {
        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val result = ContextCompat.checkSelfPermission(activity, permission)
        return result == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        val permission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        activity.requestPermissions(permission, PERMISSION_REQUEST_CODE)
    }

    private fun registerDownloadReceiver() {
        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        activity.registerReceiver(downloadReceiver, filter)
    }
    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }
}
