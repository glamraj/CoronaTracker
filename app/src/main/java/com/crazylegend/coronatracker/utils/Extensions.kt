package com.crazylegend.coronatracker.utils

import com.crazylegend.kotlinextensions.misc.getCountryCode


/**
 * Created by crazy on 3/29/20 to long live and prosper !
 */


fun countryFlag(countryName:String): String {
    return "https://www.countryflags.io/${getCountryCode(countryName)}/flat/64.png"
}