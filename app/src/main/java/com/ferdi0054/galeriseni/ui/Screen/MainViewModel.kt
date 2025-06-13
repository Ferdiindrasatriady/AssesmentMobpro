package com.ferdi0054.galeriseni.ui.Screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ferdi0054.galeriseni.model.Karya
import com.ferdi0054.galeriseni.network.ApiStatus
import com.ferdi0054.galeriseni.network.KaryaApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    var data = mutableStateOf(emptyList<Karya>())
        private set
    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    fun retrieveData() {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                data.value = KaryaApi.service.getKarya()
                Log.d("KARYA_LIST", "Jumlah karya: ${data.value.size}")
                Log.d("KARYA_LIST", "Judul pertama: ${data.value.firstOrNull()?.judul}")

                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("MainViewModel", "FailureL ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }
}

//    fun saveData(userId: String, judul: String, deskripsi: String, bitmap: Bitmap){
//        viewModelScope.launch (Dispatchers.IO){
//            try {
//                val result = KaryaApi.service.postKarya(
//                    userId,
//                    judul.toRequestBody("text/plain".toMediaTypeOrNull()),
//                    deskripsi.toRequestBody("text/plain".toMediaTypeOrNull()),
//                    bitmap.toMultipartBody()
//
//                )
//                if (result.status=="success"){
//                    retrieveData(userId)
//                }else
//                    throw Exception(result.message)
//            }catch (e:Exception){
//                Log.d("MainViewModel", "Failure:${e.message}")
//                errorMessage.value = "Error: ${e.message}"
//            }
//        }
//    }
//
//
//}
