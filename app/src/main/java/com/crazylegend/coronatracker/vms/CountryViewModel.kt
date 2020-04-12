package com.crazylegend.coronatracker.vms

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crazylegend.coronatracker.BuildConfig
import com.crazylegend.coronatracker.abstracts.AbstractAVM
import com.crazylegend.coronatracker.consts.PRIMARY_URL
import com.crazylegend.coronatracker.dtos.CoronaModel
import com.crazylegend.coronatracker.dtos.NewsModel
import com.crazylegend.kotlinextensions.collections.second
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.kotlinextensions.retrofit.RetrofitResult
import com.crazylegend.kotlinextensions.retrofit.emptyData
import com.crazylegend.kotlinextensions.retrofit.loading
import com.crazylegend.kotlinextensions.retrofit.success
import com.crazylegend.kotlinextensions.rx.ioThreadScheduler
import com.crazylegend.kotlinextensions.rx.mainThreadScheduler
import com.crazylegend.kotlinextensions.rx.singleFrom
import com.crazylegend.kotlinextensions.tryOrPrint
import io.reactivex.rxkotlin.addTo
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


/**
 * Created by crazy on 4/5/20 to long live and prosper !
 */
class CountryViewModel(application: Application, private val country: String) : AbstractAVM(application) {


    private val coronaListData: MutableLiveData<RetrofitResult<List<CoronaModel>>> = MutableLiveData()
    val coronaList: LiveData<RetrofitResult<List<CoronaModel>>> = coronaListData

    private val newsData: MutableLiveData<RetrofitResult<List<NewsModel>>> = MutableLiveData()
    val news: LiveData<RetrofitResult<List<NewsModel>>> = newsData

    private val mildConditionCasesData :MutableLiveData<String> = MutableLiveData()
    val mildCondition : LiveData<String> = mildConditionCasesData


    init {
        fetchData()
        if (BuildConfig.DEBUG){
            debug("COUNTRY $country")
        }
    }

    @SuppressLint("DefaultLocale")
    fun fetchData() {
        coronaListData.loading()
        newsData.loading()
        singleFrom {
            Jsoup.connect("$PRIMARY_URL/country/${country.toLowerCase()}")
                    .timeout(10000)
                    .get()
        }.subscribeOn(ioThreadScheduler)
                .observeOn(mainThreadScheduler)
                .subscribe({
                    handleUSATable(it)
                    handleNews(it)
                    handleMildCases(it)
                }, {
                    it.printStackTrace()
                }).addTo(compositeDisposable)
    }

    private fun handleMildCases(it: Document?) {
        val casesTable = it?.getElementsByClass("number-table")?.firstOrNull()?.text()
        mildConditionCasesData.value = casesTable
    }


    private fun handleNews(it: Document?) {
        val newsDiv = it?.getElementById("news_block")
        if ((newsDiv == null || newsDiv.childrenSize() == 3) && retryCount<3){
            fetchData()
            retryCount++
        }
        val adapterList = newsDiv?.children()?.asSequence()?.map {
            val newsDate = it.getElementsByClass("btn btn-light date-btn")?.firstOrNull()
            val newsText = it.getElementsByClass("news_li")?.firstOrNull()
            val link = newsText?.getElementsByAttribute("href")?.second()?.attr("href")
            if (newsDate != null || newsText !=null){
                val formattedNewsText = newsText?.text()?.replace("(", "")?.replace(")", "")
                        ?.replace("[source]","")?.trim()
                NewsModel(newsDate?.text(), formattedNewsText, link)
            } else {
                null
            }
        }?.filterNotNull()?.toList()?: emptyList()
        newsData.success(adapterList)
    }

    private fun handleUSATable(it: Document?) {
        val table = it?.getElementById("usa_table_countries_today")
        if (table != null) {
            val tableSelection = table.select("tbody")
            val tableElements = tableSelection.firstOrNull()?.select("tr")
            tryOrPrint {
                tableElements?.removeAt(0)
            }

            val tableList = tableElements?.asSequence()?.map { country ->
                val properties = country.select("td")
                val coronaModel = CoronaModel(
                        countryName = properties.getOrNull(0)?.text().orEmpty(),
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
                        testsPerMPopulation = properties.getOrNull(11)?.text().orEmpty(),
                        hideFlag = true
                )
                coronaModel
            }?.sortedByDescending { it.totalCases.replace(",", "").toInt() }?.toList() ?: emptyList()

            if (tableList.isEmpty()) {
                coronaListData.emptyData()

            } else {
                coronaListData.success(tableList)

            }
        } else {
            coronaListData.emptyData()
        }
    }


}