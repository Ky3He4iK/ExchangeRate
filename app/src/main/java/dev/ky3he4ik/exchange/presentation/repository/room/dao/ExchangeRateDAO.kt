package dev.ky3he4ik.exchange.presentation.repository.room.dao

import androidx.room.*
import dev.ky3he4ik.exchange.domain.model.ExchangeRate
import dev.ky3he4ik.exchange.presentation.repository.model.ExchangeRateDTO

@Dao
abstract class ExchangeRateDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addRate(exchangeRate: ExchangeRateDTO)

    @Query("SELECT * FROM exchange_rate WHERE currencies = :pair")
    abstract fun getRate(pair: Pair<String, String>): ExchangeRateDTO?

    @Query("SELECT * FROM exchange_rate")
    abstract fun getAllRates(): List<ExchangeRateDTO>
}
