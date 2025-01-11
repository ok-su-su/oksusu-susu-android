package com.susu.core.android

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.FileProvider
import java.io.File

object FileUtils {
    fun openFile(context: Context, downloadId: Long) {
        val query = DownloadManager.Query().apply {
            setFilterById(downloadId)  // 다운로드 ID로 파일 조회
        }
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val cursor = downloadManager.query(query)
        cursor?.let {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
                val fileUri = it.getString(columnIndex)
                val filePath = Uri.parse(fileUri)

                val contentUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    File(filePath.path)
                )

                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(contentUri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(intent)
            }
            it.close()
        }
    }
}
