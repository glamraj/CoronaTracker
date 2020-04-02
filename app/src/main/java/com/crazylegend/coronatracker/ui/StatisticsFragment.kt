package com.crazylegend.coronatracker.ui

import android.os.Bundle
import android.view.View
import com.crazylegend.coronatracker.R
import com.crazylegend.coronatracker.abstracts.AbstractFragment
import com.crazylegend.coronatracker.databinding.FragmentStatisticsBinding
import com.crazylegend.kotlinextensions.viewBinding.viewBinding


/**
 * Created by crazy on 4/2/20 to long live and prosper !
 */

class StatisticsFragment : AbstractFragment(R.layout.fragment_statistics) {

    override val binding by viewBinding(FragmentStatisticsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}