package com.crazylegend.coronatracker.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.crazylegend.coronatracker.R
import com.crazylegend.coronatracker.abstracts.AbstractFragment
import com.crazylegend.coronatracker.databinding.FragmentDetailedCountryBinding
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.kotlinextensions.viewBinding.viewBinding


/**
 * Created by crazy on 4/2/20 to long live and prosper !
 */
class DetailedCountryFragment : AbstractFragment(R.layout.fragment_detailed_country) {
    override val binding by viewBinding (FragmentDetailedCountryBinding::bind)

    private val args by navArgs<DetailedCountryFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        debug("CORONA ${args.model}")
    }
}