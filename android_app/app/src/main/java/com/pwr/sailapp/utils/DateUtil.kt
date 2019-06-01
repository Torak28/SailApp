package com.pwr.sailapp.utils

import android.annotation.SuppressLint
import android.util.Log
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*

const val MAX_FORECAST_DAYS = 10
/*
Format the date according to format used in JSON
 */
object DateUtil{
    // 2019-05-30T02:00:00.118Z
    private const val DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS"

    @SuppressLint("SimpleDateFormat")
    fun dateToString(date: Date?): String {
        val simpleDateFormat = SimpleDateFormat(DATE_PATTERN)
        val dateStr = simpleDateFormat.format(date)
        return dateStr+"Z"
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


    fun isForecastAvailable(dateToCheck: Date?): Boolean {
        if(dateToCheck == null){Log.e("isForecastAvailable", "dateToCheck = null");return false}
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateTimeToCheck = DateTime(dateToCheck)
        val currentDateTime = DateTime(currentTime)
        val maxDateTime = currentDateTime.plusDays(MAX_FORECAST_DAYS)
        return dateTimeToCheck < maxDateTime
    }

    fun isOneYearBefore(dateToCheck: Date, today: Date):Boolean{
        val dateTimeToCheck = DateTime(dateToCheck)
        val currentDateTime = DateTime(today)
        val yearBeforeDateTime = currentDateTime.minusYears(1)
        return dateTimeToCheck > yearBeforeDateTime
    }

    fun isTheSameYear(date1: Date, date2: Date):Boolean{
        val dateTime1 = DateTime(date1)
        val dateTime2 = DateTime(date2)
        return dateTime1.year == dateTime2.year
    }

    fun getMonth(date: Date):Int = DateTime(date).monthOfYear

    fun getYear(date: Date):Int = DateTime(date).year






}
