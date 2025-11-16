package com.example.appfinanceiro.di

import android.app.Application

class FinanceiroApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}