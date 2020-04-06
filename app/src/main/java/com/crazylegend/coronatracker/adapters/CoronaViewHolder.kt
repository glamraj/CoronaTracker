package com.crazylegend.coronatracker.adapters

import android.animation.ObjectAnimator
import android.os.SystemClock
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.coronatracker.R
import com.crazylegend.coronatracker.databinding.ItemviewCoronaBinding
import com.crazylegend.coronatracker.dtos.CoronaModel
import com.crazylegend.coronatracker.utils.countryFlag
import com.crazylegend.coronatracker.utils.manualCountryFlag
import com.crazylegend.kotlinextensions.glide.loadImgNoCache
import com.crazylegend.kotlinextensions.recyclerview.context
import com.crazylegend.kotlinextensions.recyclerview.getDrawable
import com.crazylegend.kotlinextensions.views.dp
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.setPrecomputedText
import com.crazylegend.kotlinextensions.views.visible


/**
 * Created by crazy on 3/29/20 to long live and prosper !
 */
class CoronaViewHolder(private val binding: ItemviewCoronaBinding) : RecyclerView.ViewHolder(binding.root) {

    private val FADE_DURATION = 1000L


    private val animation = ObjectAnimator.ofFloat(itemView, View.ALPHA, 1f, 0f, 1f).apply {
        repeatCount = ObjectAnimator.INFINITE
        duration = FADE_DURATION
        // Reset the alpha on animation end.
        doOnEnd { itemView.alpha = 1f }
    }

    fun bind(item: CoronaModel) {
        animation.end()
        if (item.hideFlag){
            binding.flag.gone()
            binding.country.updateLayoutParams<ConstraintLayout.LayoutParams> {
                marginStart = 16.dp
            }
            binding.totalCasesAndDeaths.updateLayoutParams<ConstraintLayout.LayoutParams> {
                marginStart = 16.dp
            }

        } else {
            binding.flag.visible()
            val countryFlag = manualCountryFlag(item.flagCode)?: countryFlag(item.countryName)
            binding.flag.loadImgNoCache(countryFlag, getDrawable(R.drawable.ic_launcher_foreground)!!, getDrawable(R.drawable.ic_launcher_foreground)!!)
        }
        binding.country.setPrecomputedText(item.countryName)
        binding.totalCasesAndDeaths.text = item.casesAndDeathsSpan(context)
        binding.todayCasesAndDeaths.text = item.newCasesAndDeathsSpan(context)
    }

    fun showPlaceHolder() {
        // Shift the timing of fade-in/out for each item by its adapter position. We use the
        // elapsed real time to make this independent from the timing of method call.
        animation.currentPlayTime =
                (SystemClock.elapsedRealtime() - adapterPosition * 30L) % FADE_DURATION
        animation.start()
        // Show the placeholder UI.
        binding.flag.setImageResource(R.drawable.image_placeholder)
        binding.totalCasesAndDeaths.text = null
        binding.country.text = null
        binding.todayCasesAndDeaths.text = null
        binding.totalCasesAndDeaths.setBackgroundResource(R.drawable.text_placeholder)
        binding.country.setBackgroundResource(R.drawable.text_placeholder)
        binding.todayCasesAndDeaths.setBackgroundResource(R.drawable.text_placeholder)

        binding.flag.updateLayoutParams<ConstraintLayout.LayoutParams> {
            marginStart = 16.dp
        }

        binding.totalCasesAndDeaths.updateLayoutParams<ConstraintLayout.LayoutParams> {
            width = 140.dp
            height = 20.dp
            horizontalBias = 0f
            marginStart = 16.dp
        }

        binding.todayCasesAndDeaths.updateLayoutParams<ConstraintLayout.LayoutParams> {
            width = 80.dp
            height = 30.dp
        }

        binding.country.updateLayoutParams<ConstraintLayout.LayoutParams> {
            width = 160.dp
            height = 20.dp
            horizontalBias = 0f
            marginStart = 16.dp
        }

    }
}