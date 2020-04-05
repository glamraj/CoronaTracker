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
    val country: String,
    val totalCases: String,
    val newCases:String,
    val totalDeaths:String,
    val newDeaths:String,
    val totalRecovered:String,
    val activeCases:String,
    val seriousCritical:String,
    val totCasesPerMPopulation:String,
    val deathsPerMPopulation:String,
    val totalTests:String,
    val testsPerMPopulation:String
):Parcelable {

    val totalCasesAndDeaths get() = "Total cases ${totalCases}\nTotal deaths $totalDeaths"
    val newCasesAndDeaths get() = "${newCases.ifEmpty { "0" }} cases\n${newDeaths.ifEmpty { "0" }} deaths"

    fun newCasesAndDeathsSpan(context: Context) = SpannableString(newCasesAndDeaths).apply {
        setSpan(ForegroundColorSpan(context.getCompatColor(R.color.casesColor)), 0, newCasesAndDeaths.indexOfFirst { it.isWhitespace() }, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        setSpan(ForegroundColorSpan(context.getColorCompat(R.color.deathsColor)), newCasesAndDeaths.indexOf("\n"), newCasesAndDeaths.indexOfLast { it.isDigit() }+1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }

    fun casesAndDeathsSpan(context: Context) = SpannableString(totalCasesAndDeaths).apply {
        setSpan(ForegroundColorSpan(context.getCompatColor(R.color.casesColor)), totalCasesAndDeaths.indexOfFirst { it.isDigit() }, totalCasesAndDeaths.indexOf("\n"), Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        setSpan(ForegroundColorSpan(context.getCompatColor(R.color.deathsColor)), totalCasesAndDeaths.indexOfLast { it.isWhitespace() }, totalCasesAndDeaths.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }

}