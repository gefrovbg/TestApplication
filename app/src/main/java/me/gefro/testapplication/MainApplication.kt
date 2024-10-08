package me.gefro.testapplication

import android.app.Application
import me.gefro.testapplication.koin.mainModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger(
                level = Level.DEBUG
            )
            androidContext(this@MainApplication)
            modules(mainModules(this@MainApplication))
        }
    }

}