package com.crazylegend.coronatracker.dtos

import android.content.Context
import android.os.Parcelable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.annotation.Keep
import com.crazylegend.coronatracker.R
import com.crazylegend.kotlinextensions.context.getColorCompat
import com.crazylegend.kotlinextensions.context.getCompatColor
import kotlinx.android.parcel.Parcelize


/**
 * Created by crazy on 3/29/20 to long live and prosper !
 */


@Keep
@Parcelize
data class CoronaModel(
        val countryName: String,
        val totalCases: String,
        val newCases: String,
        val totalDeaths: String,
        val newDeaths: String,
        val totalRecovered: String,
        val activeCases: String,
        val seriousCritical: String,
        val totCasesPerMPopulation: String,
        val deathsPerMPopulation: String,
        val totalTests: String,
        val testsPerMPopulation: String,
        val hideFlag:Boolean = false
) : Parcelable {

    val countryCode: String
        get() {
            return when (countryName) {
                "United States" -> "us"
                "United Kingdom"-> "uk"
                "S. Korea" -> "south-korea"
                "North Macedonia" -> "macedonia"
                "DRC" -> "democratic-republic-of-the-congo"
                "St. Vincent Grenadines" -> "saint-vincent-and-the-grenadines"
                "St. Barth" -> "saint-barthelemy"
                "Falkland Islands" -> "falkland-islands-malvinas"
                "UAE" -> "united-arab-emirates"
                "CAR" -> "central-african-republic"
                "Honk Kong" -> "china-honk-kong-sar"
                "Czechia" -> "czech-republic"
                else -> countryName.makeCountry()
            }
        }

    val flagCode: String?
        get() {
            return when (countryName) {
                "S. Korea" -> "KR"
                "North Macedonia" -> "MK"
                "Ivory Coast" -> "CI"
                "Bosnia and Herzegovina" -> "BA"
                "DRC" -> "CD"
                "Macao" -> "MO"
                "Saint Martin" -> "MF"
                "Myanmar" -> "MM"
                "Antigua and Barbuda" -> "AG"
                "Saint Lucia" -> "LC"
                "Saint Kitts and Nevis" -> "KN"
                "St. Vincent Grenadines" -> "VC"
                "St. Barth" -> "BL"
                "Saint Pierre Miquelon" -> "PM"
                "Turks and Caicos" -> "TC"
                "Falkland Islands" -> "FK"
                "Faeroe Islands" -> "FO"
                "Cabo Verde" -> "CV"
                "CAR" -> "CF"
                "Trinidad and Tobago" -> "TT"
                else -> null
            }
        }

    val totalCasesAndDeaths get() = "Total cases ${totalCases.ifEmpty { "0" }}\nTotal deaths ${totalDeaths.ifEmpty { "0" }}"
    val newCasesAndDeaths get() = "${newCases.ifEmpty { "0" }} cases\n${newDeaths.ifEmpty { "0" }} deaths"

    fun newCasesAndDeathsSpan(context: Context) = SpannableString(newCasesAndDeaths).apply {
        setSpan(ForegroundColorSpan(context.getCompatColor(R.color.casesColor)), 0, newCasesAndDeaths.indexOfFirst { it.isWhitespace() }, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        setSpan(ForegroundColorSpan(context.getColorCompat(R.color.deathsColor)), newCasesAndDeaths.indexOf("\n"), newCasesAndDeaths.indexOfLast { it.isDigit() } + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }

    fun casesAndDeathsSpan(context: Context) = SpannableString(totalCasesAndDeaths).apply {
        setSpan(ForegroundColorSpan(context.getCompatColor(R.color.casesColor)), totalCasesAndDeaths.indexOfFirst { it.isDigit() }, totalCasesAndDeaths.indexOf("\n"), Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        setSpan(ForegroundColorSpan(context.getCompatColor(R.color.deathsColor)), totalCasesAndDeaths.indexOfLast { it.isWhitespace() }, totalCasesAndDeaths.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }


    private fun String.makeCountry(): String =
            replace(" ", "-").toLowerCase()

}

