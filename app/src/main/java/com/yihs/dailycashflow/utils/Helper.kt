package com.yihs.dailycashflow.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.google.android.material.color.MaterialColors
import com.google.gson.Gson
import com.yihs.dailycashflow.data.Result
import com.yihs.dailycashflow.data.model.ErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.IOException
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object Helper {

    fun <T> apiCall(
        apiService: suspend () -> Response<T>
    ): Flow<Result<T>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService()
            if(response.isSuccessful){
                response.body()?.let {
                    emit(Result.Success(it, response.code()))
                }?: run{
                    emit(Result.Error(Constant.RESULT_BODY_NULL, response.code()))
                }
            }else{
                val errorJson = response.errorBody()?.string()
                emit(Result.Error(parseErrorMessage(errorJson), response.code()))
            }
        }catch (_: IOException){
            emit(Result.ErrorNetwork)
        }catch (e: Exception){
            emit(Result.Error(e.message ?: Constant.RESULT_UNKNOWN_ERROR, null))
        }
    }.flowOn(Dispatchers.IO)

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

        //safety value
        val safeValue = when {
            valueRatio.isNaN() || valueRatio.isInfinite() -> 0f
            else -> valueRatio
        }

        //show maximum 2 decimal after ,
        numberFormat.maximumFractionDigits = 2
        //convert percent and return
        return numberFormat.format(safeValue)
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


    fun convertTimeStampToStringDate(timestamp: Int, patternType: String = Constant.DATE_WITHOUT_DAY_NAME) : String{
        val localeId = Locale("in", "ID")
        //convert to milliseconds
        val milliSeconds = timestamp * 1000L

        //create object SimpleDateFormat
        val formatter = SimpleDateFormat(patternType, localeId)
        //create object date from timestamp
        val date = Date(milliSeconds)
        return formatter.format(date)
    }



}