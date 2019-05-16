package com.pwr.sailapp.utils

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.format.DateTimeFormatter
import java.util.*

/*
Format the date according to format used in JSON
 */
object DateUtil{
    const val DATE_PATTERN = "dd-MM-yyyy'T'HH:mm:ss"

    @SuppressLint("SimpleDateFormat")
    fun dateToString(date: Date?): String? {
        val simpleDateFormat = SimpleDateFormat(DATE_PATTERN)
        val dateStr = simpleDateFormat.format(date)
        return dateStr
    }

    @SuppressLint("SimpleDateFormat")
    fun stringToDate(dateStr: String?): Date? {
        val simpleDateFormat = SimpleDateFormat(DATE_PATTERN)
        val date =  simpleDateFormat.parse(dateStr)
        return date
    }

    fun timeDiff(dateStart: Date, dateEnd:Date):Pair<Int, Int>{
        val diffSeconds = (dateEnd.time - dateStart.time)/1000
        val hours:Int = (diffSeconds / (60 * 60)).toInt()
        val minutes:Int = (diffSeconds / 60 % 60 ).toInt()
        return Pair(hours, minutes)
    }
}
