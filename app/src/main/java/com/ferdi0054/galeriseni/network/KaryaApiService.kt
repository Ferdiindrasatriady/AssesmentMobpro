package com.ferdi0054.galeriseni.network

import com.ferdi0054.galeriseni.model.Karya
import com.ferdi0054.galeriseni.model.opStatus
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

private  const val BASE_URL = "http://10.0.2.2:3000/"

private val  moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private  val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface KaryaApiService{
    @GET("karya.php")
    suspend fun getKarya(
        @Header("Authorization") userId: String
    ): List<Karya>

    @Multipart
    @POST("karya.php")
    suspend fun postKarya(
        @Header("Authorization") userId: String,
        @Part("judul") judul: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part image: MultipartBody.Part
    ):opStatus
}

object KaryaApi{
    val service: KaryaApiService by lazy {
        retrofit.create(KaryaApiService::class.java)
    }
    fun getKaryaUrl (imageId : String): String{
        return "${BASE_URL}karya.php?id=$imageId"
    }
}
enum class ApiStatus {LOADING, SUCCESS, FAILED}