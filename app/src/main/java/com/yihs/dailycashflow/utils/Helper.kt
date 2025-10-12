package com.yihs.dailycashflow.utils

import com.google.gson.Gson
import com.yihs.dailycashflow.data.model.ErrorResponse


object Helper {

    fun parseErrorMessage(errorJson : String?) : String{
        return try {
            val gson = Gson()
            val errorResponse = gson.fromJson(errorJson, ErrorResponse::class.java)
            return errorResponse.message.toString()
        }catch (_: Exception){
            "An unexpected error occurred"
        }
    }

}