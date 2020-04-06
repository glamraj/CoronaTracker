package com.crazylegend.coronatracker.vms

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crazylegend.coronatracker.abstracts.AbstractAVM
import com.crazylegend.coronatracker.consts.PRIMARY_URL
import com.crazylegend.coronatracker.dtos.CoronaModel
import com.crazylegend.kotlinextensions.collections.second
import com.crazylegend.kotlinextensions.retrofit.*
import com.crazylegend.kotlinextensions.rx.ioThreadScheduler
import com.crazylegend.kotlinextensions.rx.mainThreadScheduler
import com.crazylegend.kotlinextensions.rx.singleFrom
import com.crazylegend.kotlinextensions.tryOrPrint
import io.reactivex.rxkotlin.addTo
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


/**
 * Created by crazy on 3/29/20 to long live and prosper !
 */
class MainActivityViewModel(application: Application) : AbstractAVM(application) {

    private val coronaListData: MutableLiveData<RetrofitResult<List<CoronaModel>>> = MutableLiveData()
    val coronaList: LiveData<RetrofitResult<List<CoronaModel>>> = coronaListData

    private val filteredCoronaListData: MutableLiveData<List<CoronaModel>> = MutableLiveData()
    val filteredCoronaList : LiveData<List<CoronaModel>> = filteredCoronaListData

    private val footerData: MutableLiveData<CoronaModel> = MutableLiveData()
    val footer: LiveData<CoronaModel> = footerData

    private val lastUpdatedData: MutableLiveData<String?> = MutableLiveData()
    val lastUpdated: LiveData<String?> = lastUpdatedData

    private val casesListData: MutableLiveData<List<String>> = MutableLiveData()
    val casesList: MutableLiveData<List<String>> = casesListData


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
        val tableSelection = table.select("tbody")
        val tableElements = tableSelection.firstOrNull()?.select("tr")
        val footerElements = tableSelection.second()?.select("tr")?.select("td")
        tryOrPrint {
            tableElements?.removeAt(0)
        }

        val tableList = tableElements?.map { country ->
            val properties = country.select("td")
            val countryName = when (properties.getOrNull(0)?.text().orEmpty()) {
                "USA" -> "United States"
                "UK" -> "United Kingdom"
                "UAE" -> "United Arab Emirates"
                else -> properties.getOrNull(0)?.text().orEmpty()
            }
            val coronaModel = CoronaModel(
                    countryName = countryName,
                    totalCases = properties.getOrNull(1)?.text().orEmpty(),
                    newCases = properties.getOrNull(2)?.text().orEmpty(),
                    totalDeaths = properties.getOrNull(3)?.text().orEmpty(),
                    newDeaths = properties.getOrNull(4)?.text().orEmpty(),
                    totalRecovered = properties.getOrNull(5)?.text().orEmpty(),
                    activeCases = properties.getOrNull(6)?.text().orEmpty(),
                    seriousCritical = properties.getOrNull(7)?.text().orEmpty(),
                    totCasesPerMPopulation = properties.getOrNull(8)?.text().orEmpty(),
                    deathsPerMPopulation = properties.getOrNull(9)?.text().orEmpty(),
                    totalTests = properties.getOrNull(10)?.text().orEmpty(),
                    testsPerMPopulation = properties.getOrNull(11)?.text().orEmpty()
            )
            coronaModel
        }?.sortedByDescending { it.totalCases.replace(",", "").toInt() } ?: emptyList()

        if (tableList.isEmpty()) {
            coronaListData.emptyData()

        } else {
            coronaListData.success(tableList)

        }

        val footer = CoronaModel(
                countryName = footerElements?.getOrNull(0)?.text().orEmpty(),
                totalCases = footerElements?.getOrNull(1)?.text().orEmpty(),
                newCases = footerElements?.getOrNull(2)?.text().orEmpty(),
                totalDeaths = footerElements?.getOrNull(3)?.text().orEmpty(),
                newDeaths = footerElements?.getOrNull(4)?.text().orEmpty(),
                totalRecovered = footerElements?.getOrNull(5)?.text().orEmpty(),
                activeCases = footerElements?.getOrNull(6)?.text().orEmpty(),
                seriousCritical = footerElements?.getOrNull(7)?.text().orEmpty(),
                totCasesPerMPopulation = footerElements?.getOrNull(8)?.text().orEmpty(),
                deathsPerMPopulation = footerElements?.getOrNull(9)?.text().orEmpty(),
                totalTests = footerElements?.getOrNull(10)?.text().orEmpty(),
                testsPerMPopulation = footerElements?.getOrNull(11)?.text().orEmpty()
        )

        footerData.postValue(footer)

    }

    private fun handleDate(document: Document) {
        val lastUpdated = document.getElementsByClass("content-inner")?.select("div")?.getOrNull(2)?.text()
        lastUpdatedData.postValue(lastUpdated)
    }


    private fun handleCases(it: Document) {
        val casesDeathsAndRecovered = it.select("div#maincounter-wrap")
        val casesList = casesDeathsAndRecovered.map { it.text() }
        casesListData.postValue(casesList)
    }

    fun filter(query: String) {
        if (query.isBlank()){
            filteredCoronaListData.value = coronaListData.getSuccess
        } else {
            filteredCoronaListData.value = coronaListData.getSuccess?.filter { it.countryName.contains(query, true) }
        }
    }


}