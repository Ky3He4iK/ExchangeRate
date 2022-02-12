package dev.ky3he4ik.exchange.presentation.repository.network.exchange_rates

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.ky3he4ik.exchange.domain.model.ExchangeRate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Timestamp

class ExchangeRateNetwork {
    private val api: ExchangeRateApi

    fun getRates(currency: String): LiveData<List<ExchangeRate>> {
        val info = MutableLiveData<List<ExchangeRate>>()
        api.getRate("334692374bba21501dca1d6b", currency)
            ?.enqueue(object : Callback<ExchangeRateResponse?> {
                override fun onResponse(
                    call: Call<ExchangeRateResponse?>,
                    response: Response<ExchangeRateResponse?>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val nextUpdate = Timestamp(response.body()?.time_next_update_unix ?: 1);
                        val rates = response.body()?.conversion_rates?.map {
                            ExchangeRate(
                                Pair(
                                    currency,
                                    it.key
                                ), it.value, nextUpdate
                            )
                        } ?: listOf()
                        info.postValue(rates)
                    }
                }

                override fun onFailure(call: Call<ExchangeRateResponse?>, t: Throwable) {
                    Log.e("ExchangeAPI", "Fail to get problem: " + t.message, t)
                }
            })
        return info
    }

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://v6.exchangerate-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(ExchangeRateApi::class.java)
    }
}