package com.crazylegend.coronatracker

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.crazylegend.coronatracker.databinding.ActivityMainBinding
import com.crazylegend.kotlinextensions.collections.second
import com.crazylegend.kotlinextensions.context.getCompatColor
import com.crazylegend.kotlinextensions.delegates.activityVM
import com.crazylegend.kotlinextensions.livedata.observe
import com.crazylegend.kotlinextensions.recyclerview.addHorizontalDivider
import com.crazylegend.kotlinextensions.recyclerview.generateRecycler
import com.crazylegend.kotlinextensions.recyclerview.initRecyclerViewAdapter
import com.crazylegend.kotlinextensions.retrofit.handle
import com.crazylegend.kotlinextensions.viewBinding.viewBinding
import com.crazylegend.kotlinextensions.views.*
import com.google.android.material.textview.MaterialTextView
import org.jsoup.nodes.Document

class MainActivity : AbstractActivity() {

    override val binding by viewBinding(ActivityMainBinding::inflate)
    private val mainActivityViewModel by activityVM<MainActivityViewModel>()

    private val recycler by lazy {
        generateRecycler<CoronaModel, CoronaViewHolder>(R.layout.itemview_corona, CoronaViewHolder::class.java) { item, holder, _ ->
            holder.bind(item)
        }
    }

    private lateinit var linearLayoutManager: LinearLayoutManager

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerTable.initRecyclerViewAdapter(recycler, linearLayoutManager, false)
        binding.recyclerTable.addHorizontalDivider(this, linearLayoutManager)
        observe(mainActivityViewModel.document) {
            it.handle({
                //loading
                binding.loadingIndicator.visible()
            }, {
                //empty data

            }, { // call error
                throwable ->
                throwable.printStackTrace()
                binding.loadingIndicator.gone()

            }, { // api error
                _, _ ->

            }, {
                //success handle
                binding.loadingIndicator.gone()
                handleDate(this)
                handleCases(this)
                handleTable(this)
            })

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.refreshCoronaStats -> {
                mainActivityViewModel.fetchData()
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun handleDate(document: Document) {
        binding.titleLayout.removeAllViews()
        val lastUpdated = document.getElementsByClass("content-inner")?.select("div")?.getOrNull(2)?.text()

        val textView = MaterialTextView(this)
        textView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        textView.maxLines = 1

        textView.text = lastUpdated
        textView.setPadding(5, 5, 5, 5)
        binding.titleLayout.addView(textView)
    }

    private fun handleTable(it: Document) {
        val adapterList = mutableListOf<CoronaModel>()

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

        adapterList.add(header)

        val tableList = tableElements?.map { country ->
            val properties = country.select("td")
            val coronaModel = CoronaModel(
                    country = properties.getOrNull(0)?.text().orEmpty(),
                    totalCases = properties.getOrNull(1)?.text().orEmpty().replace(",", "").toInt()
                            .toString(),
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
        }?.sortedByDescending { it.totalCases.toInt() }

        adapterList.addAll(tableList?: emptyList())

        val footer = CoronaModel(
                country = footerElements?.getOrNull(0)?.text().orEmpty(),
                totalCases = footerElements?.getOrNull(1)?.text().orEmpty().replace(",", "").toInt()
                        .toString(),
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

        adapterList.add(footer)

        recycler.submitList(adapterList)
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
                    getCompatColor(R.color.recoveredColor)
                }
                else -> {
                    Color.LTGRAY
                }
            }
            addTextToTitleLayout(element.text(), color)
        }
    }

    private fun addTextToTitleLayout(text: String?, color: Int) {
        if (text.isNullOrEmpty()) return

        val title = text.substringBefore(":")
        val numbers = text.substringAfter(": ")

        val textView = MaterialTextView(this)
        textView.append(title.toSizeSpan(IntRange(0, title.length), 2.5f))
        textView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        textView.append("\n")
        textView.append(
                numbers.toSizeSpan(IntRange(0, numbers.length))
                        .toColorSpan(IntRange(0, numbers.length), color)
        )
        textView.maxLines = 2
        textView.setPadding(5, 5, 5, 5)
        binding.titleLayout.addView(textView)
    }
}
