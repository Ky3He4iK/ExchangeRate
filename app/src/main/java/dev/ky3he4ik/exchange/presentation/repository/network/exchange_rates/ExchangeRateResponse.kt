package dev.ky3he4ik.exchange.presentation.repository.network.exchange_rates

import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRateResponse(
    val result: String,
    val documentation: String,
    val terms_of_use: String,
    val time_last_update_unix: Long,
    val time_last_update_utc: String,
    val time_next_update_unix: Long,
    val time_next_update_utc: String,
    val base_code: String,
    val conversion_rates: HashMap<String, Float>,
) {
    constructor() : this(
        "", "", "", 0,
        "", 0, "", "", hashMapOf()
    )
}
