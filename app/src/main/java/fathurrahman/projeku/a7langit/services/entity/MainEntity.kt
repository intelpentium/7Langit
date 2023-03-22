package fathurrahman.projeku.a7langit.services.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

//weather
@Keep
data class ResponseWeather(
    @SerializedName("id") val id: String,
    @SerializedName("weather") val weather: List<ResponseWeatherItem>,
    @SerializedName("main") val main: ResponseWeatherMain,
    @SerializedName("wind") val wind: ResponseWeatherWind,
    @SerializedName("name") val name: String,
)

@Keep
data class ResponseWeatherItem(
    @SerializedName("id") val id: String,
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String,
)

@Keep
data class ResponseWeatherMain(
    @SerializedName("temp") val temp: Double,
    @SerializedName("humidity") val humidity: String,
)

@Keep
data class ResponseWeatherWind(
    @SerializedName("speed") val speed: String,
)




//weather 3 hour
@Keep
data class ResponseWeatherHour(
    @SerializedName("cod") val cod: String,
    @SerializedName("list") val list: List<ResponseWeatherHourItem>,
)

@Keep
data class ResponseWeatherHourItem(
    @SerializedName("id") val id: String,
    @SerializedName("weather") val weather: List<ResponseWeatherItem>,
    @SerializedName("main") val main: ResponseWeatherMain,
    @SerializedName("wind") val wind: ResponseWeatherWind,
    @SerializedName("dt_txt") val dt_txt: String,
)


