package com.crazylegend.coronatracker.abstracts

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import dagger.android.AndroidInjection
import javax.inject.Inject


/**
 * Created by crazy on 3/29/20 to long live and prosper !
 */
abstract class AbstractActivity : AppCompatActivity() {
    abstract val binding: ViewBinding

    @Inject
    lateinit var defaultPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

}