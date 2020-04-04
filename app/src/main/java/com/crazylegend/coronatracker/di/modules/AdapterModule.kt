package com.crazylegend.coronatracker.di.modules

import com.crazylegend.coronatracker.R
import com.crazylegend.coronatracker.adapters.CoronaPlaceHolderAdapter
import com.crazylegend.coronatracker.adapters.CoronaViewHolder
import com.crazylegend.coronatracker.dtos.CoronaModel
import com.crazylegend.kotlinextensions.recyclerview.generateRecycler
import dagger.Module
import dagger.Provides


/**
 * Created by crazy on 4/4/20 to long live and prosper !
 */

@Module
object AdapterModule {

    @Provides
    fun coronaAdapter() = generateRecycler<CoronaModel, CoronaViewHolder>(R.layout.itemview_corona, CoronaViewHolder::class.java) { item, holder, _ ->
        holder.bind(item)
    }

    @Provides
    fun placeHolderAdapter() = CoronaPlaceHolderAdapter()
}