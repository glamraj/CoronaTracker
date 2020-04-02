package com.crazylegend.coronatracker.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionListenerAdapter
import androidx.transition.TransitionManager
import com.crazylegend.coronatracker.R
import com.crazylegend.coronatracker.abstracts.AbstractFragment
import com.crazylegend.coronatracker.adapters.CoronaPlaceHolderAdapter
import com.crazylegend.coronatracker.adapters.CoronaViewHolder
import com.crazylegend.coronatracker.databinding.FragmentCountriesBinding
import com.crazylegend.coronatracker.dtos.CoronaModel
import com.crazylegend.coronatracker.vms.MainActivityViewModel
import com.crazylegend.kotlinextensions.livedata.sharedProvider
import com.crazylegend.kotlinextensions.recyclerview.clickListeners.forItemClickListenerDSL
import com.crazylegend.kotlinextensions.recyclerview.generateRecycler
import com.crazylegend.kotlinextensions.retrofit.handle
import com.crazylegend.kotlinextensions.transition.StaggerTransition
import com.crazylegend.kotlinextensions.transition.interpolators.FAST_OUT_SLOW_IN
import com.crazylegend.kotlinextensions.transition.utils.LARGE_EXPAND_DURATION
import com.crazylegend.kotlinextensions.transition.utils.plusAssign
import com.crazylegend.kotlinextensions.transition.utils.transitionSequential
import com.crazylegend.kotlinextensions.viewBinding.viewBinding


/**
 * Created by crazy on 4/2/20 to long live and prosper !
 */
class CountriesFragment : AbstractFragment(R.layout.fragment_countries) {
    override val binding by viewBinding(FragmentCountriesBinding::bind)
    private val viewModel by lazy {
        sharedProvider<MainActivityViewModel>()
    }

    private val coronaAdapter by lazy {
        generateRecycler<CoronaModel, CoronaViewHolder>(R.layout.itemview_corona, CoronaViewHolder::class.java) { item, holder, _ ->
            holder.bind(item)
        }
    }
    private var savedItemAnimator: RecyclerView.ItemAnimator? = null

    private val fade = transitionSequential {
        duration = LARGE_EXPAND_DURATION
        interpolator = FAST_OUT_SLOW_IN
        this += Fade(Fade.OUT)
        this += Fade(Fade.IN)
        addListener(object : TransitionListenerAdapter() {
            override fun onTransitionEnd(transition: Transition) {
                if (savedItemAnimator != null) {
                    binding.list.itemAnimator = savedItemAnimator
                }
            }
        })
    }
    private val placeHolderAdapter by lazy {
        CoronaPlaceHolderAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.refreshCoronaStats->{
                viewModel.fetchData()
                true
            }

            else->{
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        viewModel.coronaList.observe(viewLifecycleOwner, Observer {
            it.handle({
                //loading
                binding.list.adapter = placeHolderAdapter
            }, {
                //empty data
                setupSuccess(emptyList())

            }, { // call error
                throwable ->
                setupSuccess(emptyList())

            }, { // api error
                errorBody, responseCode ->
                setupSuccess(emptyList())

            }, {
                //success handle
                setupSuccess(this)
            })
        })

        coronaAdapter.forItemClickListener = forItemClickListenerDSL { _, item, _ ->
            val action = CountriesFragmentDirections.actionDetailedCountry().setModel(item)
           findNavController().navigate(action)
        }
    }


    private fun setupSuccess(list: List<CoronaModel>) {
        TransitionManager.beginDelayedTransition(binding.list, StaggerTransition())
        if (binding.list.adapter != coronaAdapter) {
            binding.list.adapter = coronaAdapter
            savedItemAnimator = binding.list.itemAnimator
            binding.list.itemAnimator = null
            TransitionManager.beginDelayedTransition(binding.list, fade)
        }
        coronaAdapter.submitList(list)
    }
}