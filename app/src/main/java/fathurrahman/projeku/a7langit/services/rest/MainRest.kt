package fathurrahman.projeku.a7langit.services.rest

import androidx.annotation.Keep
import fathurrahman.projeku.a7langit.BuildConfig
import fathurrahman.projeku.a7langit.services.Response
import fathurrahman.projeku.a7langit.services.entity.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface MainRest {

    //weather
    @GET("weather")
    suspend fun weather(
        @Query("q") q: String,
        @Query("units") units: String? = "metric",
        @Query("lang") lang: String? = "id",
        @Query("appid") appid: String? = BuildConfig.API_KEY,
    ) : ResponseWeather

    //weather hour
    @GET("forecast")
    suspend fun weatherHour(
        @Query("q") q: String,
        @Query("units") units: String? = "metric",
        @Query("lang") lang: String? = "id",
        @Query("appid") appid: String? = BuildConfig.API_KEY,
        @Query("cnt") cnt: String? = "5",
    ) : ResponseWeatherHour
}