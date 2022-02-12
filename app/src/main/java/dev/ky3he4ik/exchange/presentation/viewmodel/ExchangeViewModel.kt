package dev.ky3he4ik.exchange.presentation.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import dev.ky3he4ik.exchange.domain.model.ExchangeRate
import dev.ky3he4ik.exchange.presentation.repository.Repository

object ExchangeViewModel {
    fun getExchangeRate(
        currencyFrom: String,
        currencyTo: String,
        lifecycleOwner: LifecycleOwner
    ): LiveData<ExchangeRate?> =
        Repository.currencyRepository.getRate(Pair(currencyFrom, currencyTo), lifecycleOwner)

    fun getAllRates(): LiveData<List<ExchangeRate>> =
        Repository.currencyRepository.getAllRates()
}
