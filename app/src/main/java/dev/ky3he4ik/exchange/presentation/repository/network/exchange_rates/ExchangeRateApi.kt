package dev.ky3he4ik.exchange.presentation.repository.network.exchange_rates

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Path

interface ExchangeRateApi {
    @POST("v6/{token}/latest/{currency}")
    fun getRate(@Path("token") token: String, @Path("currency") currency: String): Call<ExchangeRateResponse?>?
}
