package com.assessment.test.repo

import com.assessment.test.dataclass.ApiResponse
import com.assessment.test.dataclass.PostData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MyApi {
   /* @GET("posts")
    suspend fun getPosts(): Response<ApiResponse>
*/
    @GET("posts")
    suspend fun getPosts(): Response<List<PostData>>

}
