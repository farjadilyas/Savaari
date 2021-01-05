package com.farjad.savaari.services.network

import com.farjad.savaari.services.persistence.user.UserDataModel
import retrofit2.Call
import retrofit2.http.Field

import retrofit2.http.POST

interface WebService {

    @POST("user_data")
    fun getUser(@Field("userID") userId: Int): Call<UserDataModel>
}