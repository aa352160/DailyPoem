package com.jh.dailypoem.network

import com.jh.dailypoem.network.bean.BaseResult
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {
    // 礼物特效list
    @GET("token")
    suspend fun getPoemToken(): BaseResult<String>

    // 礼物特效list
    @GET("sentence")
    suspend fun getPoemData(@Header("X-User-Token") token: String): BaseResult<PoemBean>
}