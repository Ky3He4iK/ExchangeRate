package dev.ky3he4ik.exchange.presentation.repository.network.exchange_rates

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateApi {
    @GET("v6/{token}/latest/{currency}")
    fun getRate(
        @Path("token") token: String,
        @Path("currency") currency: String
    ): Call<ExchangeRateResponse?>
}
