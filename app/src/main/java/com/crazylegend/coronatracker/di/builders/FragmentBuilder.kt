package com.crazylegend.coronatracker.di.builders

import com.crazylegend.coronatracker.MainActivity
import com.crazylegend.coronatracker.WalkThroughActivity
import com.crazylegend.coronatracker.abstracts.AbstractActivity
import com.crazylegend.coronatracker.ui.CountriesFragment
import com.crazylegend.coronatracker.ui.DetailedCountryFragment
import com.crazylegend.coronatracker.ui.StatisticsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Created by crazy on 4/4/20 to long live and prosper !
 */

@Module
abstract class FragmentBuilder {

    @ContributesAndroidInjector
    abstract fun bindCountries(): CountriesFragment

    @ContributesAndroidInjector
    abstract fun bindDetailedCountries(): DetailedCountryFragment

    @ContributesAndroidInjector
    abstract fun bindStatistics(): StatisticsFragment

}