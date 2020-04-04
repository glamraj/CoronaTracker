package com.crazylegend.coronatracker.ui.activities

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.crazylegend.coronatracker.utils.shouldShowWalkThrough
import com.crazylegend.kotlinextensions.activity.launchActivityAndFinish
import dagger.android.AndroidInjection
import javax.inject.Inject


/**
 * Created by crazy on 4/5/20 to long live and prosper !
 */
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var defaultPrefs: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        if (defaultPrefs.shouldShowWalkThrough()) {
            launchActivityAndFinish<WalkThroughActivity>()
        } else {
            launchActivityAndFinish<MainActivity>()
        }

    }
}