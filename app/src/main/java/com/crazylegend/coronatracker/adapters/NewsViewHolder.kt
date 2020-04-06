package com.crazylegend.coronatracker.adapters

import android.graphics.Typeface
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.coronatracker.databinding.ItemviewNewsBinding
import com.crazylegend.coronatracker.dtos.NewsModel
import com.crazylegend.kotlinextensions.views.dp
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.visible


/**
 * Created by crazy on 4/6/20 to long live and prosper !
 */
class NewsViewHolder(private val binding: ItemviewNewsBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: NewsModel, position: Int, itemCount: Int) {
        if (item.date.isNullOrEmpty()) {
            setupViewHolderAsTitle(item.text)
        } else {
            setupViewHolderAsDate(item.date)
        }
        if (position == itemCount - 1) {
            binding.divider.gone()
        }
    }

    private fun setupViewHolderAsTitle(text: String?) {
        binding.openInBrowser.visible()
        binding.divider.visible()
        binding.date.textSize = 6.dp.toFloat()
        binding.date.setTypeface(null, Typeface.NORMAL)
        binding.date.text = text
        binding.date.updateLayoutParams<ConstraintLayout.LayoutParams> {
            setMargins(0, 10, 0, 10)
        }
    }

    private fun setupViewHolderAsDate(date: String) {
        binding.date.text = date
        binding.openInBrowser.gone()
        binding.divider.gone()
        binding.date.setTypeface(null, Typeface.BOLD_ITALIC)
        binding.date.textSize = 10.dp.toFloat()
    }
}