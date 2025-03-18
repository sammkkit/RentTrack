package com.samapp.renttrack.util

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File

fun openPdf(context: Context, file: File) {
    Log.d("Invoice", "Opening PDF: ${file.absolutePath}")
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/pdf")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    try {
        context.startActivity(Intent.createChooser(intent, "Open Invoice"))
        Log.d("Invoice", "PDF opened successfully")
    } catch (e: Exception) {
        Log.e("Invoice", "Error opening PDF: ${e.message}")
        e.printStackTrace()
    }
}
