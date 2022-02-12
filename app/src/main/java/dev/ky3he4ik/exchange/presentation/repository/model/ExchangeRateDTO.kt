package dev.ky3he4ik.exchange.presentation.repository.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.ky3he4ik.exchange.domain.model.ExchangeRate
import java.sql.Timestamp

@Entity(tableName = "exchange_rate")
class ExchangeRateDTO(
    @PrimaryKey
    override val currencies: Pair<String, String>,
    override val rate: Float,
    override val nextUpdate: Timestamp
) : ExchangeRate(currencies, rate, nextUpdate) {
    constructor(exchangeRate: ExchangeRate) : this(
        exchangeRate.currencies,
        exchangeRate.rate,
        exchangeRate.nextUpdate
    )
}
