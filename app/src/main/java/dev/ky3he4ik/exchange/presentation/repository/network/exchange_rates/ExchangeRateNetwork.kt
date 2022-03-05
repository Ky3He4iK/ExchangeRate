package dev.ky3he4ik.exchange.presentation.repository.network.exchange_rates

import android.util.Log
import dev.ky3he4ik.exchange.domain.model.ExchangeRate
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Timestamp

class ExchangeRateNetwork {
    private val api: ExchangeRateApi

    fun getRates(currency: String): List<ExchangeRate>? {
        Log.w("Exchange/Net-", currency)
        val response = api.getRate("334692374bba21501dca1d6b", currency).execute() ?: return null
        Log.w("Exchange/Net0", (response.isSuccessful).toString())
        Log.w("Exchange/Net1", (response.body()).toString())
        if (response.isSuccessful && response.body() != null) {
            Log.w("Exchange/Net", (response.isSuccessful).toString())

            val nextUpdate = Timestamp(response.body()?.time_next_update_unix ?: 1);
            val rates = response.body()?.conversion_rates?.map {
                ExchangeRate(
                    Pair(
                        currency,
                        it.key
                    ), it.value, nextUpdate
                )
            }
            Log.i("Exchange/Net2", rates.toString())
            return rates
        }
        return null
    }

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://v6.exchangerate-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(ExchangeRateApi::class.java)
    }
}
