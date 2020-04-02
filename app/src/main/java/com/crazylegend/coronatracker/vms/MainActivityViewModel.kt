package com.crazylegend.coronatracker.vms

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crazylegend.coronatracker.R
import com.crazylegend.coronatracker.consts.PRIMARY_URL
import com.crazylegend.coronatracker.dtos.CoronaModel
import com.crazylegend.kotlinextensions.collections.second
import com.crazylegend.kotlinextensions.context.getCompatColor
import com.crazylegend.kotlinextensions.livedata.context
import com.crazylegend.kotlinextensions.retrofit.*
import com.crazylegend.kotlinextensions.rx.clearAndDispose
import com.crazylegend.kotlinextensions.rx.ioThreadScheduler
import com.crazylegend.kotlinextensions.rx.mainThreadScheduler
import com.crazylegend.kotlinextensions.rx.singleFrom
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


/**
 * Created by crazy on 3/29/20 to long live and prosper !
 */
class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()

    private val coronaListData: MutableLiveData<RetrofitResult<List<CoronaModel>>> = MutableLiveData()
    val coronaList: LiveData<RetrofitResult<List<CoronaModel>>> = coronaListData

    init {
        fetchData()
    }

    fun fetchData() {
        coronaListData.loading()
        singleFrom {
            Jsoup.connect(PRIMARY_URL)
                .timeout(10000)
                .get()
        }.subscribeOn(ioThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe({
                handleCases(it)
                handleDate(it)
                handleTable(it)
            }, {
                coronaListData.callErrorPost(it)
                it.printStackTrace()
            }).addTo(compositeDisposable)
    }

    private fun handleTable(it: Document) {

        val table = it.getElementById("main_table_countries_today")
        val headerElements =
                table.select("thead")?.firstOrNull()?.select("tr")?.firstOrNull()?.select("th")

        val tableSelection = table.select("tbody")
        val tableElements = tableSelection.firstOrNull()?.select("tr")
        val footerElements = tableSelection.second()?.select("tr")?.select("td")

        val header = CoronaModel(
                country = headerElements?.getOrNull(0)?.text().orEmpty(),
                totalCases = headerElements?.getOrNull(1)?.text().orEmpty(),
                newCases = headerElements?.getOrNull(2)?.text().orEmpty(),
                totalDeaths = headerElements?.getOrNull(3)?.text().orEmpty(),
                newDeaths = headerElements?.getOrNull(4)?.text().orEmpty(),
                totalRecovered = headerElements?.getOrNull(5)?.text().orEmpty(),
                activeCases = headerElements?.getOrNull(6)?.text().orEmpty(),
                seriousCritical = headerElements?.getOrNull(7)?.text().orEmpty(),
                totCasesPerMPopulation = headerElements?.getOrNull(8)?.text().orEmpty(),
                deathsPerMPopulation = headerElements?.getOrNull(9)?.text().orEmpty(),
                firstCaseDate = headerElements?.getOrNull(10)?.text().orEmpty()
        )


        val tableList = tableElements?.map { country ->
            val properties = country.select("td")
            val countryName = when(properties.getOrNull(0)?.text().orEmpty()){
                "USA"-> "United States"
                "UK"-> "United Kingdom"
                "UAE" ->"United Arab Emirates"
                else-> properties.getOrNull(0)?.text().orEmpty()
            }
            val coronaModel = CoronaModel(
                    country = countryName,
                    totalCases = properties.getOrNull(1)?.text().orEmpty(),
                    newCases = properties.getOrNull(2)?.text().orEmpty(),
                    totalDeaths = properties.getOrNull(3)?.text().orEmpty(),
                    newDeaths = properties.getOrNull(4)?.text().orEmpty(),
                    totalRecovered = properties.getOrNull(5)?.text().orEmpty(),
                    activeCases = properties.getOrNull(6)?.text().orEmpty(),
                    seriousCritical = properties.getOrNull(7)?.text().orEmpty(),
                    totCasesPerMPopulation = properties.getOrNull(8)?.text().orEmpty(),
                    deathsPerMPopulation = properties.getOrNull(9)?.text().orEmpty(),
                    firstCaseDate = properties.getOrNull(10)?.text().orEmpty()
            )
            coronaModel
        }?.sortedByDescending { it.totalCases.replace(",", "").toInt() }?: emptyList()

        if (tableList.isEmpty()){
            coronaListData.emptyData()

        } else {
            coronaListData.success(tableList)

        }

        val footer = CoronaModel(
                country = footerElements?.getOrNull(0)?.text().orEmpty(),
                totalCases = footerElements?.getOrNull(1)?.text().orEmpty(),
                newCases = footerElements?.getOrNull(2)?.text().orEmpty(),
                totalDeaths = footerElements?.getOrNull(3)?.text().orEmpty(),
                newDeaths = footerElements?.getOrNull(4)?.text().orEmpty(),
                totalRecovered = footerElements?.getOrNull(5)?.text().orEmpty(),
                activeCases = footerElements?.getOrNull(6)?.text().orEmpty(),
                seriousCritical = footerElements?.getOrNull(7)?.text().orEmpty(),
                totCasesPerMPopulation = footerElements?.getOrNull(8)?.text().orEmpty(),
                deathsPerMPopulation = footerElements?.getOrNull(9)?.text().orEmpty(),
                firstCaseDate = footerElements?.getOrNull(10)?.text().orEmpty()
        )

    }

    private fun handleDate(document: Document) {
        val lastUpdated = document.getElementsByClass("content-inner")?.select("div")?.getOrNull(2)?.text()

    }


    private fun handleCases(it: Document) {
        val casesDeathsAndRecovered = it.select("div#maincounter-wrap")
        casesDeathsAndRecovered.forEachIndexed { index, element ->
            val color = when (index) {
                0 -> {
                    Color.GRAY
                }
                1 -> {
                    Color.RED
                }
                2 -> {
                    context.getCompatColor(R.color.recoveredColor)
                }
                else -> {
                    Color.LTGRAY
                }
            }
            //addTextToTitleLayout(element.text(), color)
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clearAndDispose()
    }
}