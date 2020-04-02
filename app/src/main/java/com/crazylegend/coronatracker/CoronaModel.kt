package com.crazylegend.coronatracker


/**
 * Created by crazy on 3/29/20 to long live and prosper !
 */

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
)