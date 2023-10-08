package com.shine.language.data.api

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
//    @POST("api/dvc_info")
    @POST("s")
    suspend fun postDeviceInfo(
        @Field("flag") flag: String,
        @Field("date_json") dataJson: String
    )
}