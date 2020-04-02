package com.crazylegend.coronatracker.dtos

import android.os.Parcelable
import androidx.annotation.Keep
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
    val firstCaseDate:String
):Parcelable