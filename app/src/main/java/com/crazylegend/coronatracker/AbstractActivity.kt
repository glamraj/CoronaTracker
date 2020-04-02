package com.crazylegend.coronatracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding


/**
 * Created by crazy on 3/29/20 to long live and prosper !
 */
abstract class AbstractActivity : AppCompatActivity() {
    abstract val binding:ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

}