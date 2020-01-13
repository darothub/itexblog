package com.example.itexblog.ui.base

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class MyApplicationModule(val application: Application) {

    @Provides
    fun provideApplicationContext():Context{
        return application
    }
}