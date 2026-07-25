package com.example.moodmate.utils

import android.content.Context
import android.os.Environment
import android.widget.Toast
import com.example.moodmate.data.local.MoodEntity
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CSVExporter @Inject constructor(
    private val context: Context
) {

    fun exportMoodsToCSV(
        moods: List<MoodEntity>,
        onSuccess: (File) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(Date())

            val fileName = "mood_history_$timestamp.csv"
            val file = File(
                context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                fileName
            )

            FileOutputStream(file).use { outputStream ->
                // Write headers
                val headers = "Date,Time,Mood,Smile Probability,Notes\n"
                outputStream.write(headers.toByteArray())

                // Write data
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

                moods.sortedBy { it.createdAt }.forEach { mood ->
                    val date = Date(mood.createdAt)
                    val row = buildString {
                        append(dateFormat.format(date))
                        append(",")
                        append(timeFormat.format(date))
                        append(",")
                        append(mood.mood.name)
                        append(",")
                        append(String.format("%.2f", mood.smileProbability * 100))
                        append("%,")
                        append(mood.note ?: "") // Note field will be added later
                        append("\n")
                    }
                    outputStream.write(row.toByteArray())
                }
            }

            onSuccess(file)

        } catch (e: Exception) {
            onError("Export failed: ${e.message}")
        }
    }

    fun shareCSV(context: Context, file: File) {
        // You can implement sharing via Intent
        val uri = androidx.core.content.FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(android.content.Intent.EXTRA_STREAM, uri)
            addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(
            android.content.Intent.createChooser(intent, "Share CSV")
        )
    }
}