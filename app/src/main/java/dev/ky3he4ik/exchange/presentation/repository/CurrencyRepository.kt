package dev.ky3he4ik.exchange.presentation.repository

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import dev.ky3he4ik.exchange.domain.model.ExchangeRate

interface CurrencyRepository {
    fun addRate(exchangeRate: ExchangeRate)
    fun <T : ExchangeRate> getRate(pair: Pair<String, String>, owner: LifecycleOwner): LiveData<T?>
    fun <T : ExchangeRate> getAllRates(): LiveData<List<T>>
}
