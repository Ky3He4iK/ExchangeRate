package dev.ky3he4ik.exchange.presentation.repository.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.ky3he4ik.exchange.presentation.repository.model.ExchangeRateDTO
import dev.ky3he4ik.exchange.presentation.repository.room.dao.ExchangeRateDAO
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@Database(
    entities = [ExchangeRateDTO::class],
    version = 1,
    exportSchema = false
)
abstract class ExchangeDatabase : RoomDatabase() {
    abstract fun exchangeRateDAO(): ExchangeRateDAO

    companion object {
        @Volatile
        private var instance: ExchangeDatabase? = null

        private const val NUMBER_OF_THREADS = 4

        val databaseWriteExecutor: ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

        fun getInstance(context: Context): ExchangeDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): ExchangeDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ExchangeDatabase::class.java,
                "exchange_db"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
