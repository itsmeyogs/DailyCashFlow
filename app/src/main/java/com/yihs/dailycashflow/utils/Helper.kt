package com.yihs.dailycashflow.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.google.android.material.color.MaterialColors
import com.google.gson.Gson
import com.yihs.dailycashflow.data.model.ErrorResponse
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object Helper {

    fun parseErrorMessage(errorJson : String?) : String{
        return try {
            val gson = Gson()
            val errorResponse = gson.fromJson(errorJson, ErrorResponse::class.java)
            return errorResponse.message
        }catch (_: Exception){
            "An unexpected error occurred"
        }
    }

    fun toPercent(valueRatio : Float) : String{
        val numberFormat = NumberFormat.getPercentInstance()
        //show maximum 2 decimal after ,
        numberFormat.maximumFractionDigits = 2
        //convert percent and return
        return numberFormat.format(valueRatio)
    }

    fun getColorFromAttr(context: Context, attrResId: Int, defaultColor: Int) : Int{
        return MaterialColors.getColor(
            context,
            attrResId,
            defaultColor
        )
    }

    fun getDrawable(context: Context, drawableResId: Int) : Drawable? {
        return ContextCompat.getDrawable(context, drawableResId)
    }

    fun toRupiah(number: Long) : String{
        val localeId = Locale("in", "ID")
        val formatter = NumberFormat.getCurrencyInstance(localeId) as DecimalFormat

        //add symbol Rp
        val symbols = formatter.decimalFormatSymbols
        symbols.currencySymbol = "Rp "
        formatter.decimalFormatSymbols = symbols

        //remove decimal in after comma
        formatter.maximumFractionDigits = 0
        formatter.minimumFractionDigits = 0

        return formatter.format(number)
    }


    fun convertTimeStampToStringDate(timestamp: Long, withDayName: Boolean = false) : String{
        val localeId = Locale("in", "ID")
        //set pattern with day name or not
        val pattern = if(withDayName) "EEEE, dd MMMM yyyy" else "dd MMMM yyyy"

        //convert to milliseconds
        val milliSeconds = timestamp * 1000L

        //create object SimpleDateFormat
        val formatter = SimpleDateFormat(pattern, localeId)
        //create object date from timestamp
        val date = Date(milliSeconds)
        return formatter.format(date)
    }



}