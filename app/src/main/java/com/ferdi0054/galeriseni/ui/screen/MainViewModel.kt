package com.ferdi0054.galeriseni.ui.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferdi0054.galeriseni.model.Karya
import com.ferdi0054.galeriseni.network.ApiStatus
import com.ferdi0054.galeriseni.network.IMAGE_APIKEY
import com.ferdi0054.galeriseni.network.KaryaApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class MainViewModel : ViewModel() {
    var data = mutableStateOf(emptyList<Karya>())
        private set
    var status = MutableStateFlow(ApiStatus.LOADING)
        private set
    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun retrieveData(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                data.value = KaryaApi.service.getKarya(userId)
                Log.d("KARYA_LIST", "Jumlah karya: ${data.value.size}")
                Log.d("KARYA_LIST", "Judul pertama: ${data.value.firstOrNull()?.judul}")

                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("MainViewModel", "FailureL ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun deleteImage(id: String, userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                KaryaApi.service.deleteKarya(id)
                retrieveData(userId)
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                status.value = ApiStatus.FAILED
                e.printStackTrace()
                Log.d("MainViewModel", "Failure: ${e.message}")
            }
        }
    }

    fun saveData(
        userId: String,
        judul: String,
        deskripsi: String,
        bitmap: Bitmap,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                val imageResponse = KaryaApi.imageService.postImage(
                    key = IMAGE_APIKEY,
                    action = "upload",
                    image = bitmap.toMultipartBody()
                )
                KaryaApi.service.postKarya(
                    judul = judul,
                    deskripsi = deskripsi,
                    imageUrl = imageResponse.image.thumb.url,
                    mine = "1"
                )
                retrieveData(userId)
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                status.value = ApiStatus.FAILED
                e.printStackTrace()
                Log.d("MainViewModel", "Failure: ${e.message}")
            }
        }
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpg".toMediaTypeOrNull(), 0, byteArray.size
        )
        return MultipartBody.Part.createFormData(
            "source", "image.jpg", requestBody
        )
    }

    fun clearMessage() {
        errorMessage.value = null
    }
}

