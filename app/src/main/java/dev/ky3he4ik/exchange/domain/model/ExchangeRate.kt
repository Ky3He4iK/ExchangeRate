package dev.ky3he4ik.exchange.domain.model

import java.sql.Timestamp

open class ExchangeRate (
    open val currencies: Pair<String, String>,
    open val rate: Float,
    open val nextUpdate: Timestamp
)
