package com.jh.dailypoem.network

import android.util.Log
import com.jh.dailypoem.App
import com.jh.dailypoem.network.bean.BaseResult
import com.jh.dailypoem.util.toast
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper{
    private val retrofit by lazy { initRetrofit() }

    private fun initRetrofit(): Retrofit {
        val httpLoggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d("Retrofit", message)
        }.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://v2.jinrishici.com/")
            .build()
    }

//    suspend fun <T> requestAsync(call: suspend (Retrofit) -> T,
//                            onError: (Exception) -> Boolean = { false }): T? = coroutineScope {
//        try {
//            return@coroutineScope async(Dispatchers.IO){
//                return@async startRequest(call)
//            }.await()
//        } catch (e: Exception) {
//            Log.d("error", e.message ?: "")
//            if (!onError(e)){
//                //default error action
//            }
//            return@coroutineScope null
//        } finally {
//
//        }
//    }

    fun <T> requestAsync(call: suspend (Retrofit) -> T,
                    onError: (Exception) -> Boolean = { false }): Deferred<T?> {
        return CoroutineScope(Dispatchers.IO).async { request(call, onError) }
    }

    suspend fun <T> request(call: suspend (Retrofit) -> T,
                         onError: (Exception) -> Boolean = { false }): T? {
        try {
            return startRequest(call, onError)
        } catch (e: Exception) {
            Log.d("error", e.message ?: "")
            CoroutineScope(Dispatchers.Main).async {
                if (!onError(e)){
                    //网络错误
                    App.getCurrentActivity()?.toast(e.message ?: "network error")
                }
            }
            return null
        }
    }

    private suspend fun <T> startRequest(call: suspend (Retrofit) -> T, onError: (Exception) -> Boolean): T?{
        val result = call.invoke(retrofit)
        if (result is BaseResult<*>){
            if (result.status == "success"){
                return result
            }else{
                CoroutineScope(Dispatchers.Main).async {
                    if (!onError(Exception(result.errMessage))) {
                        //接口请求错误
//                    throw e
                        App.getCurrentActivity()?.toast(result.errMessage ?: "network error")
                    }
                }
                return null
            }
        }else{
            return result
        }
    }

    //example
    //        mainScope.launch {
    //            try {
    //                val result1 = RetrofitHelper.requestAsync({ retrofit -> retrofit.create(ApiService::class.java).giftFiles() })
    //                val result2 = RetrofitHelper.requestAsync({ retrofit -> retrofit.create(ApiService::class.java).giftFiles() })
    //            }catch (e: Exception){
    //                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
    //            }
    //        }
}