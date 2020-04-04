package com.crazylegend.coronatracker.abstracts

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.crazylegend.kotlinextensions.log.debug
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


/**
 * Created by crazy on 4/2/20 to long live and prosper !
 */
abstract class AbstractFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {
    abstract val binding: ViewBinding

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

}