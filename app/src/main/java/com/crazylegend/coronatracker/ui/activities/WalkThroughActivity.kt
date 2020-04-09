package com.crazylegend.coronatracker.ui.activities

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.HtmlCompat
import com.crazylegend.coronatracker.R
import com.crazylegend.coronatracker.abstracts.AbstractActivity
import com.crazylegend.coronatracker.databinding.ActivityWalkthroughBinding
import com.crazylegend.coronatracker.utils.walkThroughShown
import com.crazylegend.kotlinextensions.activity.launchActivityAndFinish
import com.crazylegend.kotlinextensions.context.getCompatColor
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
        when (newValue) {
            0 -> {
                binding.title.text = getString(R.string.know_how_it_spreads)
                binding.text.text = getString(R.string.step1_text)
                binding.image.setImageResource(R.drawable.step_1)
                binding.forward.setImageResource(R.drawable.ic_forward)
                binding.back.invisible()
            }
            1 -> {
                binding.title.text = getString(R.string.take_steps_to_protect_yourself)
                binding.text.text = getString(R.string.step2_text)
                binding.image.setImageResource(R.drawable.step_2)
                binding.back.visible()
                binding.forward.setImageResource(R.drawable.ic_forward)
            }
            2 -> {
                binding.title.text = getString(R.string.avoid_close_contact)
                binding.text.text = getString(R.string.steps_text)
                binding.image.setImageResource(R.drawable.step_3)
                binding.back.visible()
                binding.forward.setImageResource(R.drawable.ic_forward)
            }
            3 -> {
                binding.title.text = getString(R.string.take_steps_to_protect_yourself)
                binding.text.text = getString(R.string.step4_text)
                binding.image.setImageResource(R.drawable.step_4)
                binding.back.visible()
                binding.forward.setImageResource(R.drawable.ic_forward)
            }
            MAX_SCREENS -> {
                binding.title.text = getString(R.string.clean_disenfect)
                binding.text.text = getString(R.string.step5_text)
                binding.image.setImageResource(R.drawable.step_5)
                binding.forward.setImageResource(R.drawable.ic_check)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addBottomDots(0)
        setupUI(0)

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
        defaultPrefs.walkThroughShown()
        launchActivityAndFinish<MainActivity>()
    }

    @Suppress("DEPRECATION")
    private fun addBottomDots(currentPage: Int) {
        val inactiveText = with(AppCompatTextView(this@WalkThroughActivity)) {
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
        dots?.add(inactiveText)
        dots?.add(inactiveText)
        dots?.add(inactiveText)
        dots?.add(inactiveText)
        dots?.add(inactiveText)

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