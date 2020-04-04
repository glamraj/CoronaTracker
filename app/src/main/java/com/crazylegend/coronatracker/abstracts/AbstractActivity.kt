package com.crazylegend.coronatracker.abstracts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import dagger.android.AndroidInjection


/**
 * Created by crazy on 3/29/20 to long live and prosper !
 */
abstract class AbstractActivity : AppCompatActivity() {
    abstract val binding:ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

}