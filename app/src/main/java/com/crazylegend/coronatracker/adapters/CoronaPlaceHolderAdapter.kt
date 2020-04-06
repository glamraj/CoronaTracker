package com.crazylegend.coronatracker.adapters

import android.view.ViewGroup
import com.crazylegend.coronatracker.databinding.ItemviewCoronaBinding
import com.crazylegend.kotlinextensions.recyclerview.PlaceholderAdapter
import com.crazylegend.kotlinextensions.views.inflater


/**
 * Created by crazy on 4/2/20 to long live and prosper !
 */
class CoronaPlaceHolderAdapter : PlaceholderAdapter<CoronaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoronaViewHolder {
        val binding = ItemviewCoronaBinding.inflate(parent.inflater, parent, false)
        return CoronaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoronaViewHolder, position: Int) {
        holder.showPlaceHolder()
    }
}