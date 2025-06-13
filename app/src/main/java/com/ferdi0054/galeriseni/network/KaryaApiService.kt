package com.ferdi0054.galeriseni.network

import com.ferdi0054.galeriseni.model.ImageResponse
import com.ferdi0054.galeriseni.model.Karya
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


private const val BASE_URL = "https://684c0ab5ed2578be881d6e9a.mockapi.io/api/galeri/"
private const val IMAGE_BASE_URL = "https://freeimage.host/api/1/upload/"
const val IMAGE_APIKEY = "6d207e02198a847aa98d0a2a901485a5"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(KaryaApi.getHttpClient())
    .baseUrl(BASE_URL)
    .build()

private val imageHttpClient = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(IMAGE_BASE_URL)
    .client(KaryaApi.getHttpClient())
    .build()

interface KaryaApiService {
    @GET("art")
    suspend fun getKarya(
        @Header("Authorization") userId: String
    ): List<Karya>

    @FormUrlEncoded
    @POST("art")
    suspend fun postKarya(
        @Field("judul") judul: String,
        @Field("deskripsi") deskripsi: String,
        @Field("gambar") imageUrl: String,
        @Field("mine") mine:String
    )


    @DELETE("art/{id}")
    suspend fun deleteKarya(
        @Path("id") id: String
    )
}

interface ImageService {
    @Multipart
    @POST("upload")
    suspend fun postImage(
        @Query("key") key: String,
        @Query("action") action: String,
        @Part image: MultipartBody.Part
    ): ImageResponse
}


object KaryaApi {
    val service: KaryaApiService by lazy {
        retrofit.create(KaryaApiService::class.java)
    }

    val imageService: ImageService by lazy {
        imageHttpClient.create(ImageService::class.java)
    }

    fun getHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

}

enum class ApiStatus { LOADING, SUCCESS, FAILED }