package com.crazylegend.coronatracker

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.HtmlCompat
import com.crazylegend.coronatracker.abstracts.AbstractActivity
import com.crazylegend.coronatracker.databinding.ActivityWalkthroughBinding
import com.crazylegend.kotlinextensions.activity.launchActivityAndFinish
import com.crazylegend.kotlinextensions.context.getCompatColor
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.kotlinextensions.viewBinding.viewBinding
import com.crazylegend.kotlinextensions.views.invisible
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown
import com.crazylegend.kotlinextensions.views.visible
import kotlin.properties.Delegates


/**
 * Created by crazy on 4/2/20 to long live and prosper !
 */
class WalkThroughActivity : AbstractActivity() {

    override val binding by viewBinding(ActivityWalkthroughBinding::inflate)

    private var dots: ArrayList<TextView>? = null
    private val MAX_SCREENS = 4
    private var currentPage by Delegates.observable(0) { _, _, newValue ->
        setupUI(newValue)
        addBottomDots(newValue)
    }

    private fun setupUI(newValue: Int) {
        debug("NEW VALUE $newValue")
        when (newValue) {
            0 -> {
                binding.forward.setImageResource(R.drawable.ic_forward)
                binding.back.invisible()
            }
            1 -> {
                binding.back.visible()
                binding.forward.setImageResource(R.drawable.ic_forward)
            }
            2 -> {
                binding.back.visible()
                binding.forward.setImageResource(R.drawable.ic_forward)

            }
            3 -> {
                binding.back.visible()
                binding.forward.setImageResource(R.drawable.ic_forward)
            }
            MAX_SCREENS -> {
                binding.forward.setImageResource(R.drawable.ic_check)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addBottomDots(0)

        binding.forward.setOnClickListener {
            if (currentPage == MAX_SCREENS) {
                openMainActivity()
            } else {
                currentPage++
            }
        }

        binding.back.setOnClickListener {
            currentPage--
        }

        binding.skip.setOnClickListenerCooldown {
            openMainActivity()
        }
    }

    private fun openMainActivity() {
        launchActivityAndFinish<MainActivity>()
    }

    private fun addBottomDots(currentPage: Int) {
        val text = with(AppCompatTextView(this@WalkThroughActivity)) {
            text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml("&#8226;", HtmlCompat.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml("&#8226;")
            }
            textSize = 35f
            setTextColor(getCompatColor(R.color.dotInactive))
            this
        }
        dots = ArrayList()
        dots?.add(text)
        dots?.add(text)
        dots?.add(text)
        dots?.add(text)
        dots?.add(text)

        binding.dotsLayout.removeAllViews()
        dots?.apply {
            for (i in indices) {
                this[i] = AppCompatTextView(this@WalkThroughActivity)
                this[i].text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml("&#8226;", HtmlCompat.FROM_HTML_MODE_COMPACT)
                } else {
                    Html.fromHtml("&#8226;")
                }
                this[i].textSize = 35f
                this[i].setTextColor(getCompatColor(R.color.dotInactive))
                binding.dotsLayout.addView(this[i])
            }
            if (size > 0)
                this[currentPage].setTextColor(getCompatColor(R.color.dotActive))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(Int::class.java.simpleName, currentPage)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentPage = savedInstanceState.getInt(Int::class.java.simpleName, 0)
    }
}