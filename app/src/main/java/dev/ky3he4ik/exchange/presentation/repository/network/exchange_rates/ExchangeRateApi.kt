package dev.ky3he4ik.exchange.presentation.repository.network.exchange_rates

import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateApi {
    @GET("v6/{token}/latest/{currency}")
    fun getRate(
        @Path("token") token: String,
        @Path("currency") currency: String
    ): Call<ExchangeRateResponse?>?

//    @GET("v6/{token}/codes")
//    suspend fun supportedCodes(@Path("token") token: String): NetworkResponse<ExchangeRateAvailable?>
}
