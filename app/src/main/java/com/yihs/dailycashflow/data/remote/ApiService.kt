package com.yihs.dailycashflow.data.remote

import com.yihs.dailycashflow.data.model.LoginResponse
import com.yihs.dailycashflow.data.model.RegisterResponse
import com.yihs.dailycashflow.data.model.TransactionResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email:String,
        @Field("password") password:String
    ): Response<LoginResponse>


    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<RegisterResponse>

    @GET("cashflow")
    suspend fun getCashFlows(
        @Query("order_by") orderBy: String,
        @Query("type") type: String,
        @Query("page") page: Int
    ): Response<TransactionResponse>

}