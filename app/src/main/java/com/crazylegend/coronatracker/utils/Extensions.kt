package com.crazylegend.coronatracker.utils

import android.content.SharedPreferences
import com.crazylegend.coronatracker.ui.activities.WalkThroughActivity
import com.crazylegend.kotlinextensions.misc.getCountryCode
import com.crazylegend.kotlinextensions.sharedprefs.putBoolean


/**
 * Created by crazy on 3/29/20 to long live and prosper !
 */


fun countryFlag(countryName: String): String {
    return "https://www.countryflags.io/${getCountryCode(countryName)}/flat/64.png"
}

fun SharedPreferences.shouldShowWalkThrough() = getBoolean(WalkThroughActivity::class.java.simpleName, true)
fun SharedPreferences.walkThroughShown() = putBoolean(WalkThroughActivity::class.java.simpleName, false)