package dev.ky3he4ik.exchange.presentation.repository

import android.app.Application
import dev.ky3he4ik.exchange.presentation.repository.network.exchange_rates.ExchangeRateNetwork
import dev.ky3he4ik.exchange.presentation.repository.room.CurrencyRepositoryImpl

object Repository {
    private var _currencyRepository: CurrencyRepository? = null
    val currencyRepository: CurrencyRepository
        get() {
            return _currencyRepository ?: throw RuntimeException("Repository is not initiated!")
        }

    private var _exchangeRates: ExchangeRateNetwork? = null
    val exchangeRates: ExchangeRateNetwork
        get() {
            if (_exchangeRates == null)
                _exchangeRates = ExchangeRateNetwork()
            return _exchangeRates!!
        }

    fun initRepository(application: Application) {
        _currencyRepository = _currencyRepository ?: CurrencyRepositoryImpl(application)
    }
}
