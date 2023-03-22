package fathurrahman.projeku.a7langit.viewmodel

import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fathurrahman.projeku.a7langit.common.ActionLiveData
import fathurrahman.projeku.a7langit.common.UiState
import fathurrahman.projeku.a7langit.ext.errorMesssage
import fathurrahman.projeku.a7langit.services.entity.ResponseWeather
import fathurrahman.projeku.a7langit.services.entity.ResponseWeatherHour
import fathurrahman.projeku.a7langit.services.rest.MainRest
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRest: MainRest
) : ViewModel() {

    val loadState = ActionLiveData<UiState>()

    val responseWeather = ActionLiveData<ResponseWeather>()
    val responseWeatherHour = ActionLiveData<ResponseWeatherHour>()

    fun weather(subdistrict: String) {
        viewModelScope.launch {
            loadState.sendAction(UiState.Loading)
            try {
                val response = mainRest.weather(subdistrict)
                responseWeather.value = response!!
                loadState.sendAction(UiState.Success)
            } catch (e: Exception) {
                loadState.sendAction(UiState.Error(e.errorMesssage))
            }
        }
    }

    fun weatherHour(subdistrict: String) {
        viewModelScope.launch {
            loadState.sendAction(UiState.Loading)
            try {
                val response = mainRest.weatherHour(subdistrict)
                responseWeatherHour.value = response!!
                loadState.sendAction(UiState.Success)
            } catch (e: Exception) {
                loadState.sendAction(UiState.Error(e.errorMesssage))
            }
        }
    }
}