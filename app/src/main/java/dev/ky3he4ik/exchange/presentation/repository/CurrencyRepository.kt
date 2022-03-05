package dev.ky3he4ik.exchange.presentation.repository

import dev.ky3he4ik.exchange.domain.model.ExchangeRate

interface CurrencyRepository {
    fun addRate(exchangeRate: ExchangeRate)
    fun getRate(pair: Pair<String, String>): ExchangeRate?
    fun getAllRates(): List<ExchangeRate>
}
