package com.crazylegend.coronatracker.di.components

import com.crazylegend.coronatracker.di.modules.AppModule
import dagger.Component


/**
 * Created by crazy on 4/2/20 to long live and prosper !
 */


@Component(modules = [AppModule::class])
interface AppComponent {
}