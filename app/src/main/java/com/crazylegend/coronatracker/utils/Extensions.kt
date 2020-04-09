package com.crazylegend.coronatracker.utils

import android.content.SharedPreferences
import com.crazylegend.kotlinextensions.misc.getCountryCode
import com.crazylegend.kotlinextensions.sharedprefs.putBoolean


/**
 * Created by crazy on 3/29/20 to long live and prosper !
 */


fun countryFlag(countryName: String): String {
    return "https://www.countryflags.io/${getCountryCode(countryName)}/flat/64.png"
}

fun manualCountryFlag(countryName: String?):String?{
    return if (countryName==null) null
    else "https://www.countryflags.io/$countryName/flat/64.png"
}

private const val walkThroughKey = "WalkThroughPrefKey"
fun SharedPreferences.shouldShowWalkThrough() = getBoolean(walkThroughKey, true)
fun SharedPreferences.walkThroughShown() = putBoolean(walkThroughKey, false)