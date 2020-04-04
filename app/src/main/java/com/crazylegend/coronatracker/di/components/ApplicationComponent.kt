package com.crazylegend.coronatracker.di.components

import android.app.Application
import com.crazylegend.coronatracker.CoronaTracker
import com.crazylegend.coronatracker.di.builders.ActivityBuilder
import com.crazylegend.coronatracker.di.builders.FragmentBuilder
import com.crazylegend.coronatracker.di.modules.ApplicationModule
import com.crazylegend.coronatracker.di.modules.AdapterModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton


/**
 * Created by crazy on 4/4/20 to long live and prosper !
 */

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    ActivityBuilder::class,
    FragmentBuilder::class,
    AdapterModule::class,
    ApplicationModule::class])
interface ApplicationComponent: AndroidInjector<CoronaTracker> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun create(application: Application): Builder
        fun build(): ApplicationComponent
    }

}