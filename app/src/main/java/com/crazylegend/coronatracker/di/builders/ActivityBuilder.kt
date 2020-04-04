package com.crazylegend.coronatracker.di.builders

import com.crazylegend.coronatracker.ui.activities.MainActivity
import com.crazylegend.coronatracker.ui.activities.SplashActivity
import com.crazylegend.coronatracker.ui.activities.WalkThroughActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by crazy on 4/4/20 to long live and prosper !
 */

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector
    abstract fun bindWalkThrough() : WalkThroughActivity

    @ContributesAndroidInjector
    abstract fun bindMainActivity() : MainActivity

     @ContributesAndroidInjector
    abstract fun bindSplash() : SplashActivity


}