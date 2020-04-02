package com.crazylegend.coronatracker.di.modules

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by crazy on 4/2/20 to long live and prosper !
 */

@Module
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun providesApplicationContext() = application
}