package dev.ky3he4ik.exchange.presentation.repository.room

import android.app.Application
import androidx.lifecycle.MutableLiveData
import dev.ky3he4ik.exchange.domain.model.ExchangeRate
import dev.ky3he4ik.exchange.presentation.repository.CurrencyRepository
import dev.ky3he4ik.exchange.presentation.repository.Repository
import dev.ky3he4ik.exchange.presentation.repository.model.ExchangeRateDTO
import dev.ky3he4ik.exchange.presentation.repository.room.dao.ExchangeRateDAO
import java.util.*

class CurrencyRepositoryImpl(application: Application) : CurrencyRepository {
    private var exchangeRateDAO: ExchangeRateDAO

    init {
        val db: ExchangeDatabase = ExchangeDatabase.getInstance(application)
        exchangeRateDAO = db.exchangeRateDAO()
    }

    override fun addRate(exchangeRate: ExchangeRate) {
        ExchangeDatabase.databaseWriteExecutor.execute {
            exchangeRateDAO.addRate(
                ExchangeRateDTO(exchangeRate)
            )
        }
    }

    override fun getRate(
        pair: Pair<String, String>
    ): ExchangeRate? {
        val data = MutableLiveData<ExchangeRate>()
        var currentRate: ExchangeRate? = null
        ExchangeDatabase.databaseWriteExecutor.execute {
            var rate = exchangeRateDAO.getRate(pair)
            if (rate == null || rate.nextUpdate >= Date()) {
                Repository.exchangeRates.getRates(pair.first)?.forEach { exchangeRate ->
                    exchangeRateDAO.addRate(ExchangeRateDTO(exchangeRate))
                    if (exchangeRate.currencies == pair)
                        rate = ExchangeRateDTO(exchangeRate)
                }
            }
            currentRate = rate
        }
        return currentRate
    }


    override fun getAllRates(): List<ExchangeRate> {
        return exchangeRateDAO.getAllRates()
    }
}
