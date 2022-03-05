package dev.ky3he4ik.exchange.presentation.repository.room

import android.app.Application
import android.util.Log
import dev.ky3he4ik.exchange.domain.model.ExchangeRate
import dev.ky3he4ik.exchange.presentation.repository.CurrencyRepository
import dev.ky3he4ik.exchange.presentation.repository.Repository
import dev.ky3he4ik.exchange.presentation.repository.model.ExchangeRateDTO
import dev.ky3he4ik.exchange.presentation.repository.room.dao.ExchangeRateDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class CurrencyRepositoryImpl(application: Application) : CurrencyRepository {
    private var exchangeRateDAO: ExchangeRateDAO

    private val context = Dispatchers.IO

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

    override suspend fun getRate(
        pair: Pair<String, String>
    ): ExchangeRate? {
        Log.w("Exchange/Repo", pair.toString())
        return withContext(context) {
            var rate = exchangeRateDAO.getRate(pair)
            if (rate == null || rate.nextUpdate >= Date()) {
                Log.w("Exchange/Repo1", rate.toString())
                Repository.exchangeRates.getRates(pair.first)?.forEach { exchangeRate ->
                    exchangeRateDAO.addRate(ExchangeRateDTO(exchangeRate))
                    if (exchangeRate.currencies == pair)
                        rate = ExchangeRateDTO(exchangeRate)
                }
            }
            if (rate == null) {
                rate = exchangeRateDAO.getRate(Pair(pair.second, pair.first))
                if (rate != null) {
                    rate = ExchangeRateDTO(
                        currencies = Pair(pair.second, pair.first),
                        rate = 1f / rate!!.rate,
                        nextUpdate = rate!!.nextUpdate
                    )
                }
            }

            Log.w("Exchange/Repo2", rate.toString())
            rate
        }
    }


    override fun getAllRates(): List<ExchangeRate> {
        return exchangeRateDAO.getAllRates()
    }
}
