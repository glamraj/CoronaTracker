package com.crazylegend.coronatracker.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.crazylegend.coronatracker.R
import com.crazylegend.coronatracker.abstracts.AbstractFragment
import com.crazylegend.coronatracker.adapters.CoronaViewHolder
import com.crazylegend.coronatracker.databinding.FragmentDetailedCountryBinding
import com.crazylegend.coronatracker.databinding.ItemviewCoronaBinding
import com.crazylegend.coronatracker.dtos.CoronaModel
import com.crazylegend.coronatracker.utils.countryFlag
import com.crazylegend.coronatracker.utils.manualCountryFlag
import com.crazylegend.coronatracker.vms.CountryViewModel
import com.crazylegend.kotlinextensions.abstracts.AbstractViewBindingAdapter
import com.crazylegend.kotlinextensions.fragments.compatColor
import com.crazylegend.kotlinextensions.fragments.drawable
import com.crazylegend.kotlinextensions.glide.loadImgNoCache
import com.crazylegend.kotlinextensions.livedata.fragmentProviderAVM
import com.crazylegend.kotlinextensions.orElse
import com.crazylegend.kotlinextensions.retrofit.handle
import com.crazylegend.kotlinextensions.viewBinding.viewBinding
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.visible
import javax.inject.Inject


/**
 * Created by crazy on 4/2/20 to long live and prosper !
 */
class DetailedCountryFragment : AbstractFragment(R.layout.fragment_detailed_country) {
    override val binding by viewBinding(FragmentDetailedCountryBinding::bind)

    private val args by navArgs<DetailedCountryFragmentArgs>()

    private lateinit var viewModel: CountryViewModel

    @Inject
    lateinit var coronaAdapter: AbstractViewBindingAdapter<CoronaModel, CoronaViewHolder, ItemviewCoronaBinding>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addBackListener()
        viewModel = fragmentProviderAVM(args.model?.countryCode.toString())
        binding.news.adapter = newsAdapter
        binding.cities.adapter = coronaAdapter
        loadFlag(args.model)
        loadUI(args.model)

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

        viewModel.coronaList.observe(viewLifecycleOwner, Observer {
            it.handle({
                //loading
                binding.loading.visible()

            }, {
                //empty data
                binding.loading.gone()

            }, { // call error
                throwable ->
                throwable.printStackTrace()
                binding.loading.gone()

            }, { // api error
                _, _ ->
                binding.loading.gone()

            }, {
                //success handle
                binding.loading.gone()
                coronaAdapter.submitList(this)
            })
        })

        viewModel.mildCondition.observe(viewLifecycleOwner, Observer {
            setupMildConditionCases(it)
        })

        setupFabNewsClickListener(binding.fabNews, binding.scrim)
        setupForNewsItemClickListener(binding.fabNews)
        setupCloseNewsClickListener(binding.fabNews, binding.closeNews)

    }

    private fun setupMildConditionCases(mildCondition: String?) {
        if (mildCondition.isNullOrEmpty()){
            binding.mildCondition.root.gone()
        } else {
            binding.mildCondition.root.visible()
        }
        binding.mildCondition.apply {
            title.text = getString(R.string.mild_condition)
            title.setTextColor(compatColor(R.color.primaryLight))
            subtitle.text = mildCondition ?: notAvailableString
        }
    }

    private fun loadUI(model: CoronaModel?) {
        model ?: return

        binding.country.text = model.countryName

        binding.totalCases.apply {
            title.text = getString(R.string.total_cases)
            title.setTextColor(compatColor(R.color.casesColor))
            subtitle.text = model.totalCases.orElse(notAvailableString)
        }

        binding.totalDeaths.apply {
            title.text = getString(R.string.total_deaths)
            title.setTextColor(compatColor(R.color.deathsColor))
            subtitle.text = model.totalDeaths.orElse(notAvailableString)
        }

        binding.totalRecoveries.apply {
            title.text = getString(R.string.total_recoveries)
            title.setTextColor(compatColor(R.color.recoveriesColor))
            subtitle.text = model.totalRecovered.orElse(notAvailableString)
        }

        binding.activeCases.apply {
            title.text = getString(R.string.active_cases)
            title.setTextColor(compatColor(R.color.casesColor))
            subtitle.text = model.activeCases.orElse(notAvailableString)
        }

        binding.seriouslyCritical.apply {
            title.text = getString(R.string.seriously_critical)
            title.setTextColor(compatColor(R.color.deathsColor))
            subtitle.text = model.seriousCritical.orElse(notAvailableString)
        }

        binding.totalTests.apply {
            title.text = getString(R.string.total_tests)
            title.setTextColor(compatColor(R.color.secondaryText))
            subtitle.text = model.totalTests.orElse(notAvailableString)
        }

    }

    private fun loadFlag(item: CoronaModel?) {
        val countryFlag = manualCountryFlag(item?.flagCode)
                ?: countryFlag(item?.countryName.toString())
        binding.flag.loadImgNoCache(countryFlag, drawable(R.drawable.ic_launcher_foreground)!!, drawable(R.drawable.ic_launcher_foreground)!!)
    }

    private fun addBackListener() {
        requireActivity()
                .onBackPressedDispatcher
                .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (binding.fabNews.isExpanded) {
                            binding.fabNews.isExpanded = false
                        } else {
                            if (isEnabled) {
                                isEnabled = false
                                requireActivity().onBackPressed()
                            }
                        }
                    }
                }
                )
    }
}