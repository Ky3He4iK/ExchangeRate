package dev.ky3he4ik.exchange.presentation.repository.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object DataTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun stringToCurrencies(data: String?): Pair<String, String> {
        return Gson().fromJson(data, object : TypeToken<Pair<String, String>>() {}.type)
    }

    @TypeConverter
    fun currenciesToString(someObjects: Pair<String, String>): String = gson.toJson(someObjects)
}
