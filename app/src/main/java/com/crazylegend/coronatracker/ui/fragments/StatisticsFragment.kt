package com.crazylegend.coronatracker.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.crazylegend.coronatracker.R
import com.crazylegend.coronatracker.abstracts.AbstractFragment
import com.crazylegend.coronatracker.databinding.FragmentStatisticsBinding
import com.crazylegend.coronatracker.databinding.LayoutCardStatsBinding
import com.crazylegend.coronatracker.dtos.CoronaModel
import com.crazylegend.coronatracker.dtos.NewsModel
import com.crazylegend.coronatracker.vms.MainActivityViewModel
import com.crazylegend.kotlinextensions.fragments.compatColor
import com.crazylegend.kotlinextensions.intent.openWebPage
import com.crazylegend.kotlinextensions.livedata.sharedProvider
import com.crazylegend.kotlinextensions.orElse
import com.crazylegend.kotlinextensions.recyclerview.clickListeners.forItemClickListenerDSL
import com.crazylegend.kotlinextensions.retrofit.handle
import com.crazylegend.kotlinextensions.tryOrIgnore
import com.crazylegend.kotlinextensions.viewBinding.viewBinding
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textview.MaterialTextView


/**
 * Created by crazy on 4/2/20 to long live and prosper !
 */
@SuppressLint("InflateParams")

class StatisticsFragment : AbstractFragment(R.layout.fragment_statistics) {

    override val binding by viewBinding(FragmentStatisticsBinding::bind)
    private val viewModel by lazy { sharedProvider<MainActivityViewModel>() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.news.adapter = newsAdapter
        viewModel.casesList.observe(viewLifecycleOwner, Observer {
            handleCases(it)
        })


        viewModel.footer.observe(viewLifecycleOwner, Observer {
            handleFooter(it)
        })

        viewModel.lastUpdated.observe(viewLifecycleOwner, Observer {
            handleLastUpdated(it)
        })

        viewModel.news.observe(viewLifecycleOwner, Observer {
            it.handle({
                //loading

            }, {
                //empty data

            }, { // call error
                throwable ->
                throwable.printStackTrace()
            }, { // api error
                _, _ ->

            }, {
                //success handle
                setupNews(this, binding.fabNews)
            })
        })

        setupFabNewsClickListener(binding.fabNews, binding.scrim)
        setupForNewsItemClickListener(binding.fabNews)
        setupCloseNewsClickListener(binding.fabNews, binding.closeNews)
    }

    private fun handleLastUpdated(lastUpdated: String?) {
        generateCard(onTitle = {
            text = getString(R.string.last_updated)
            setTextColor(compatColor(R.color.secondaryText))
        }, onSubtitle = {
            text = lastUpdated
        })
    }

    private fun handleCases(list: List<String>?) {
        if (list.isNullOrEmpty()) {
            generateCard(onTitle = {
                text = getString(R.string.no_cases_returned)
            }, onSubtitle = {
                text = getString(R.string.try_again_later)
            })

        } else {
            list.forEachIndexed { index, listText ->
                val color = when (index) {
                    0 -> {
                        compatColor(R.color.casesColor)
                    }
                    1 -> {
                        compatColor(R.color.deathsColor)
                    }
                    2 -> {
                        compatColor(R.color.recoveriesColor)
                    }
                    else -> {
                        compatColor(R.color.secondaryText)
                    }
                }
                val title = listText.substringBefore(":")
                val numbers = listText.substringAfter(": ")
                generateCard(onTitle = {
                    text = title
                    setTextColor(color)
                }, onSubtitle = {
                    text = numbers
                })
            }
        }
    }

    private fun generateCard(onTitle: MaterialTextView.() -> Unit, onSubtitle: MaterialTextView.() -> Unit) {
        val layout = layoutInflater.inflate(R.layout.layout_card_stats, null)
        val cardBinding = LayoutCardStatsBinding.bind(layout)
        cardBinding.title.onTitle()
        cardBinding.subtitle.onSubtitle()
        binding.layout.addView(cardBinding.root)
    }

    private fun handleFooter(model: CoronaModel?) {
        if (model == null) return
        generateCard(onTitle = {
            text = getString(R.string.world)
        }, onSubtitle = {
            tryOrIgnore {
                text = model.newCasesAndDeathsSpan(requireContext())
            }
        })

        generateCard(onTitle = {
            text = getString(R.string.active_cases)
            setTextColor(compatColor(R.color.secondaryText))
        }, onSubtitle = {
            text = model.activeCases.orElse(notAvailableString)
        })

        generateCard(onTitle = {
            text = getString(R.string.seriously_critical)
            setTextColor(compatColor(R.color.deathsColor))
        }, onSubtitle = {
            text = model.seriousCritical.orElse(notAvailableString)
        })

        generateCard(onTitle = {
            text = getString(R.string.total_cases_per1m)
            setTextColor(compatColor(R.color.casesColor))
        }, onSubtitle = {
            text = model.totCasesPerMPopulation.orElse(notAvailableString)
        })


        generateCard(onTitle = {
            text = getString(R.string.total_deaths_per1m)
            setTextColor(compatColor(R.color.deathsColor))
        }, onSubtitle = {
            text = model.deathsPerMPopulation.orElse(notAvailableString)
        })


        generateCard(onTitle = {
            text = getString(R.string.total_tests)
        }, onSubtitle = {
            text = model.totalTests.orElse(notAvailableString)
        })

        generateCard(onTitle = {
            text = getString(R.string.total_tests_per1m)
        }, onSubtitle = {
            text = model.testsPerMPopulation.orElse(notAvailableString)
        })


    }

}