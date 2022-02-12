package dev.ky3he4ik.exchange.presentation.repository.room

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
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

    override fun <T : ExchangeRate> getRate(
        pair: Pair<String, String>,
        owner: LifecycleOwner
    ): LiveData<T?> {
        val data = MutableLiveData<ExchangeRate>()
        ExchangeDatabase.databaseWriteExecutor.execute {
            var rate = exchangeRateDAO.getRate(pair)
            if (rate == null || rate.nextUpdate >= Date()) {
                Repository.exchangeRates.getRates(pair.first).observe(owner) {
                    it.forEach { exchangeRate ->
                        exchangeRateDAO.addRate(ExchangeRateDTO(exchangeRate))
                        if (exchangeRate.currencies == pair)
                            rate = ExchangeRateDTO(exchangeRate)
                    }
                }
            }
            if (rate != null)
                data.postValue(rate)
        }
        return data as LiveData<T?>
    }

    override fun <T : ExchangeRate> getAllRates(): LiveData<List<T>> {
        val data = MutableLiveData<List<ExchangeRateDTO>>()
        ExchangeDatabase.databaseWriteExecutor.execute {
            data.postValue(exchangeRateDAO.getAllRates())
        }
        return data as LiveData<List<T>>
    }
}
