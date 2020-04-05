package com.crazylegend.coronatracker.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.doOnLayout
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
import com.crazylegend.coronatracker.consts.SEARCH_QUERY_KEY
import com.crazylegend.coronatracker.databinding.FragmentCountriesBinding
import com.crazylegend.coronatracker.dtos.CoronaModel
import com.crazylegend.coronatracker.vms.MainActivityViewModel
import com.crazylegend.kotlinextensions.abstracts.AbstractListAdapter
import com.crazylegend.kotlinextensions.getFromMemory
import com.crazylegend.kotlinextensions.isNotNullOrEmpty
import com.crazylegend.kotlinextensions.livedata.sharedProvider
import com.crazylegend.kotlinextensions.putInMemory
import com.crazylegend.kotlinextensions.recyclerview.clickListeners.forItemClickListenerDSL
import com.crazylegend.kotlinextensions.retrofit.handle
import com.crazylegend.kotlinextensions.rx.bindings.textChanges
import com.crazylegend.kotlinextensions.rx.clearAndDispose
import com.crazylegend.kotlinextensions.transition.StaggerTransition
import com.crazylegend.kotlinextensions.transition.interpolators.FAST_OUT_SLOW_IN
import com.crazylegend.kotlinextensions.transition.utils.LARGE_EXPAND_DURATION
import com.crazylegend.kotlinextensions.transition.utils.plusAssign
import com.crazylegend.kotlinextensions.transition.utils.transitionSequential
import com.crazylegend.kotlinextensions.viewBinding.viewBinding
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


/**
 * Created by crazy on 4/2/20 to long live and prosper !
 */
class CountriesFragment : AbstractFragment(R.layout.fragment_countries) {
    override val binding by viewBinding(FragmentCountriesBinding::bind)

    private val viewModel by lazy { sharedProvider<MainActivityViewModel>() }

    @Inject
    lateinit var coronaAdapter: AbstractListAdapter<CoronaModel, CoronaViewHolder>

    @Inject
    lateinit var placeHolderAdapter: CoronaPlaceHolderAdapter

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

    private var searchView: SearchView? = null
    private var searchQuery: String? = null
    private val compositeDisposable by lazy {
        CompositeDisposable()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clearAndDispose()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)

        val searchItem = menu.findItem(R.id.app_bar_search)

        searchItem?.apply {
            searchView = this.actionView as SearchView?
        }
        searchView?.queryHint = getString(R.string.search_by_country_name)

        searchView?.textChanges(compositeDisposable = compositeDisposable) {
            searchQuery = it
            viewModel.filter(it)
        }

        getFromMemory(SEARCH_QUERY_KEY)?.apply {
            if (this is String && isNotNullOrEmpty()) {
                searchItem.expandActionView()
                searchView?.doOnLayout {
                    searchView?.setQuery(this, true)
                }
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refreshCoronaStats -> {
                viewModel.fetchData()
                true
            }

            else -> {
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

        viewModel.filteredCoronaList.observe(viewLifecycleOwner, Observer {
            coronaAdapter.submitList(it)
        })
    }

    override fun onPause() {
        super.onPause()
        putInMemory(SEARCH_QUERY_KEY, searchView?.query.toString())
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