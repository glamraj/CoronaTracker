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
import com.crazylegend.coronatracker.vms.MainActivityViewModel
import com.crazylegend.kotlinextensions.fragments.compatColor
import com.crazylegend.kotlinextensions.livedata.sharedProvider
import com.crazylegend.kotlinextensions.viewBinding.viewBinding


/**
 * Created by crazy on 4/2/20 to long live and prosper !
 */
@SuppressLint("InflateParams")

class StatisticsFragment : AbstractFragment(R.layout.fragment_statistics) {

    override val binding by viewBinding(FragmentStatisticsBinding::bind)
    private val viewModel by lazy { sharedProvider<MainActivityViewModel>() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.footer.observe(viewLifecycleOwner, Observer {
            handleFooter(it)
        })

        viewModel.casesList.observe(viewLifecycleOwner, Observer {
            handleCases(it)
        })

        viewModel.lastUpdated.observe(viewLifecycleOwner, Observer {
            handleLastUpdated(it)
        })


    }

    private fun handleLastUpdated(lastUpdated: String?) {
        val layout = layoutInflater.inflate(R.layout.layout_card_stats, null)
        val cardBinding = LayoutCardStatsBinding.bind(layout)
        cardBinding.title.text = getString(R.string.last_updated)
        cardBinding.title.setTextColor(compatColor(R.color.secondaryText))
        cardBinding.subtitle.text = lastUpdated
        binding.layout.addView(cardBinding.root)
    }

    private fun handleCases(list: List<String>?) {
        if (list.isNullOrEmpty()) {
            val layout = layoutInflater.inflate(R.layout.layout_card_stats, null)
            val cardBinding = LayoutCardStatsBinding.bind(layout)

            cardBinding.title.text = getString(R.string.no_cases_returned)
            cardBinding.subtitle.text = getString(R.string.try_again_later)
            binding.layout.addView(cardBinding.root)
        } else {
            list.forEachIndexed { index, text ->
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
                val layout = layoutInflater.inflate(R.layout.layout_card_stats, null)
                val cardBinding = LayoutCardStatsBinding.bind(layout)
                val title = text.substringBefore(":")
                val numbers = text.substringAfter(": ")
                cardBinding.title.text = title
                cardBinding.subtitle.text = numbers
                cardBinding.title.setTextColor(color)
                binding.layout.addView(cardBinding.root)
            }
        }
    }

    private fun handleFooter(model: CoronaModel?) {
        val layout = layoutInflater.inflate(R.layout.layout_card_stats, null)
        val cardBinding = LayoutCardStatsBinding.bind(layout)
        cardBinding.title.text = getString(R.string.world)
        cardBinding.subtitle.text = model?.newCasesAndDeathsSpan(requireContext())
        binding.layout.addView(cardBinding.root)
    }

}