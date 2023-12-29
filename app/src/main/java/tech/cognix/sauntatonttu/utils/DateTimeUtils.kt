package tech.cognix.sauntatonttu.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class DateTimeUtils {

    fun getFormattedTime(dateTime: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(dateTime)
    }

    fun getDateFromServer(datetime: String?): String {
        if (datetime.isNullOrEmpty()) {
            return "N/A"
        } else {
            val inputFormatter =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormatter = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
            val date = inputFormatter.parse(datetime)
            return outputFormatter.format(date!!)
        }

    }

    fun getTimeFromServer(datetime: String?): String {
        if (datetime.isNullOrEmpty()) {
            return "N/A"
        } else {
            val inputFormatter =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
            val date = inputFormatter.parse(datetime)
            return outputFormatter.format(date!!)
        }

    }
}