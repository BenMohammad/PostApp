package com.benmohammad.core.application

import android.app.Application
import com.benmohammad.core.BuildConfig
import com.benmohammad.core.di.AppModule
import com.benmohammad.core.di.CoreComponent
import com.benmohammad.core.di.DaggerCoreComponent
import com.benmohammad.core.networking.synk.Synk
import com.facebook.stetho.Stetho

open class CoreApp: Application() {

    companion object{
        lateinit var coreComponent: CoreComponent
    }

    override fun onCreate() {
        super.onCreate()
        initSynk()
        initDI()
        initStetho()
    }

    private fun initSynk() {
        Synk.init(this)
    }


    private fun initStetho() {
        if(BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

    private fun initDI() {
        coreComponent = DaggerCoreComponent.builder().appModule(AppModule(this)).build()
    }

}