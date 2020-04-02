package com.crazylegend.coronatracker

import android.app.Application
import com.crazylegend.coronatracker.di.components.AppComponent
import com.crazylegend.coronatracker.di.components.DaggerAppComponent
import com.crazylegend.coronatracker.di.modules.AppModule


/**
 * Created by crazy on 4/2/20 to long live and prosper !
 */
class CoronaTracker : Application() {

    lateinit var wikiComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

    }

    private fun initDagger(app: Application): AppComponent =
            DaggerAppComponent.builder()
                    .appModule(AppModule(app))
                    .build()

}