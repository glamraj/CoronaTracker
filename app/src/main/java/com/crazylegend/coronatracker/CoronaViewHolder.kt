package com.crazylegend.coronatracker

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.coronatracker.databinding.ItemviewCoronaBinding
import com.crazylegend.kotlinextensions.recyclerview.context
import com.crazylegend.kotlinextensions.viewBinding.viewBinding
import com.crazylegend.kotlinextensions.views.dp


/**
 * Created by crazy on 3/29/20 to long live and prosper !
 */
class CoronaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding by viewBinding(ItemviewCoronaBinding::bind)
    private val headerLayout = binding.itemLayout
    fun bind(header: CoronaModel) {
        headerLayout.removeAllViews()
        context.generateText(header.country,headerLayout, 100.dp)
        generateDivider(headerLayout)
        context.generateText(header.totalCases,headerLayout)
        generateDivider(headerLayout)
        context.generateText(header.newCases,headerLayout)
        generateDivider(headerLayout)
        context.generateText(header.totalDeaths,headerLayout)
        generateDivider(headerLayout)
        context.generateText(header.newDeaths,headerLayout)
        generateDivider(headerLayout)
        context.generateText(header.totalRecovered,headerLayout)
        generateDivider(headerLayout)
        context.generateText(header.activeCases,headerLayout)
        generateDivider(headerLayout)
        context.generateText(header.seriousCritical,headerLayout)
        generateDivider(headerLayout)
        context.generateText(header.totCasesPerMPopulation,headerLayout)
        generateDivider(headerLayout)
        context.generateText(header.firstCaseDate,headerLayout)

    }
}