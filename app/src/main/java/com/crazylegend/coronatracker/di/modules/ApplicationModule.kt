package com.crazylegend.coronatracker.di.modules

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton


/**
 * Created by crazy on 4/4/20 to long live and prosper !
 */

@Module
object ApplicationModule {

    @Provides
    @Singleton
    fun defaultPrefs(application: Application): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)

    @Provides
    fun compositeDisposable() = CompositeDisposable()

}