package com.ferdi0054.galeriseni.ui.Screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.ferdi0054.galeriseni.model.Karya
import com.ferdi0054.galeriseni.network.ApiStatus
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel: ViewModel() {
    var data = mutableStateOf(emptyList<Karya>())
        private  set
    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

}
