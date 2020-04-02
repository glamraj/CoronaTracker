package com.crazylegend.coronatracker.adapters

import android.view.ViewGroup
import com.crazylegend.coronatracker.R
import com.crazylegend.kotlinextensions.recyclerview.PlaceholderAdapter
import com.crazylegend.kotlinextensions.views.inflate


/**
 * Created by crazy on 4/2/20 to long live and prosper !
 */
class CoronaPlaceHolderAdapter : PlaceholderAdapter<CoronaViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CoronaViewHolder(parent.inflate(R.layout.itemview_corona))

    override fun onBindViewHolder(holder: CoronaViewHolder, position: Int) {
       holder.showPlaceHolder()
    }
}