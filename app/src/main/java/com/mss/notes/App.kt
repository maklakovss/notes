package com.mss.notes

import android.support.multidex.MultiDexApplication
import com.mss.notes.di.appModule
import com.mss.notes.di.mainModule
import com.mss.notes.di.noteModule
import com.mss.notes.di.splashModule
import org.koin.android.ext.android.startKoin

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(appModule, mainModule, splashModule, noteModule))
    }
}