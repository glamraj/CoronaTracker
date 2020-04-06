package com.crazylegend.coronatracker.di.modules

import com.crazylegend.coronatracker.adapters.CoronaPlaceHolderAdapter
import com.crazylegend.coronatracker.adapters.CoronaViewHolder
import com.crazylegend.coronatracker.adapters.NewsViewHolder
import com.crazylegend.coronatracker.databinding.ItemviewCoronaBinding
import com.crazylegend.coronatracker.databinding.ItemviewNewsBinding
import com.crazylegend.coronatracker.dtos.CoronaModel
import com.crazylegend.coronatracker.dtos.NewsModel
import com.crazylegend.kotlinextensions.recyclerview.generateRecycler
import dagger.Module
import dagger.Provides


/**
 * Created by crazy on 4/4/20 to long live and prosper !
 */

@Module
object AdapterModule {

    @Provides
    fun coronaAdapter() = generateRecycler<CoronaModel, CoronaViewHolder, ItemviewCoronaBinding>(CoronaViewHolder::class.java, ItemviewCoronaBinding::inflate) { item, holder, _, _ ->
        holder.bind(item)
    }

    @Provides
    fun placeHolderAdapter() = CoronaPlaceHolderAdapter()

    @Provides
    fun newsAdapter() = generateRecycler<NewsModel, NewsViewHolder, ItemviewNewsBinding>(NewsViewHolder::class.java, ItemviewNewsBinding::inflate){ item, holder, position, itemCount ->
        holder.bind(item, position,itemCount)
    }
}