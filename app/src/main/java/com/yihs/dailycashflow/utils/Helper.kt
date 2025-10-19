package com.yihs.dailycashflow.utils

import com.google.gson.Gson
import com.yihs.dailycashflow.data.model.ErrorResponse
import java.text.NumberFormat


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

}