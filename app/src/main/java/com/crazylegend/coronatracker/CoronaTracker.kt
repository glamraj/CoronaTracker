package com.crazylegend.coronatracker

import android.app.Application
import android.content.SharedPreferences
import com.crazylegend.coronatracker.di.components.ApplicationComponent
import com.crazylegend.coronatracker.di.components.DaggerApplicationComponent
import com.crazylegend.coronatracker.di.modules.ApplicationModule
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.kotlinextensions.sharedprefs.putBoolean
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.reactivex.plugins.RxJavaPlugins
import javax.inject.Inject


/**
 * Created by crazy on 4/2/20 to long live and prosper !
 */
class CoronaTracker : Application(), HasAndroidInjector {


    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler {  }
        DaggerApplicationComponent.builder()
                .create(this)
                .build()
                .inject(this)

    }

}