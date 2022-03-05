package dev.ky3he4ik.exchange.presentation.repository.network.exchange_rates

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRateAvailable(
    val result: String,
    val documentation: String,
    val terms_of_use: String,
    val supportedCodes: List<List<String>>
) {
    constructor() : this("", "", "", listOf())
}
