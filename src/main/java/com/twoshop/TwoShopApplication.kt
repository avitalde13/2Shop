package com.twoshop

import android.app.Application
import android.content.Context
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class TwoShopApplication : Application() {
    companion object {
        private const val THREAD_AMOUNT = 4
        private val executorService: ExecutorService = Executors.newFixedThreadPool(THREAD_AMOUNT)
        private lateinit var instance: TwoShopApplication;

        fun getExecutorService(): ExecutorService {
            return this.executorService;
        }

        fun getInstance(): Context {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
    }
}
