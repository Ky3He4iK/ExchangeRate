package dev.ky3he4ik.exchange.presentation.viewmodel

import dev.ky3he4ik.exchange.domain.model.ExchangeRate
import dev.ky3he4ik.exchange.presentation.repository.Repository

object ExchangeViewModel {
    fun getExchangeRate(
        currencyFrom: String,
        currencyTo: String
    ): ExchangeRate? =
        Repository.currencyRepository.getRate(Pair(currencyFrom, currencyTo))

    fun getAllRates(): List<ExchangeRate> =
        Repository.currencyRepository.getAllRates()
}
