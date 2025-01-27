package com.sai.fabula

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.facebook.stetho.Stetho
import com.sai.fabula.database.NewsRepository
import com.sai.fabula.di.FabulaApiModule
import com.sai.fabula.di.FabulaDbModule
import com.sai.fabula.utils.isNight
import com.sai.fabula.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class FabulaApp : Application() {

    private var koinModuleList = module {
        single { FabulaApiModule() }
        single { FabulaDbModule(this@FabulaApp) }
        single { NewsRepository(get(), get()) }
        viewModel { MainViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()
        initTimber()
        initKoin()
        initStetho()
        setUIMode()
    }

    private fun setUIMode() {
        val mode = if (isNight()) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }

        AppCompatDelegate.setDefaultNightMode(mode)
    }

    private fun initStetho() {
        if (BuildConfig.DEBUG)
            Stetho.initializeWithDefaults(this)
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@FabulaApp)
            modules(koinModuleList)
        }
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
