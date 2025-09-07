package com.juileebhombe.newsapp.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateFormatUtils {

    private const val API_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    private const val DISPLAY_DATE_FORMAT = "MMM dd, yyyy 'at' HH:mm"

    private val apiDateFormat = SimpleDateFormat(API_DATE_FORMAT, Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    private val displayDateFormat = SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.getDefault()).apply {
        timeZone = TimeZone.getDefault()
    }

    fun formatPublishedDate(publishedAt: String): String {
        return try {
            val date = apiDateFormat.parse(publishedAt)
            date?.let { displayDateFormat.format(it) } ?: publishedAt
        } catch (e: Exception) {
            publishedAt
        }
    }

    fun formatRelativeTime(publishedAt: String): String {
        return try {
            val publishDate = apiDateFormat.parse(publishedAt)
            publishDate?.let { date ->
                val now = Date()
                val diffInMillis = now.time - date.time
                val diffInMinutes = diffInMillis / (1000 * 60)
                val diffInHours = diffInMillis / (1000 * 60 * 60)
                val diffInDays = diffInMillis / (1000 * 60 * 60 * 24)

                when {
                    diffInMinutes < 1 -> Constants.Strings.JUST_NOW
                    diffInMinutes < 60 -> "${diffInMinutes}m ago"
                    diffInHours < 24 -> "${diffInHours}h ago"
                    diffInDays < 7 -> "${diffInDays}d ago"
                    else -> formatPublishedDate(publishedAt)
                }
            } ?: formatPublishedDate(publishedAt)
        } catch (e: Exception) {
            formatPublishedDate(publishedAt)
        }
    }
}
