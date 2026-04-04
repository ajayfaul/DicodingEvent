package com.jayfm.dicodingevent.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateUtils {

    fun formatToReadableDate(dateString: String?): String {
        if (dateString.isNullOrEmpty()) return ""

        val apiFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val displayFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.forLanguageTag("id-ID"))

        return try {
            val date = apiFormat.parse(dateString)
            if (date != null) {
                displayFormat.format(date)
            } else {
                dateString
            }
        } catch (e: Exception) {
            dateString
        }
    }
}
