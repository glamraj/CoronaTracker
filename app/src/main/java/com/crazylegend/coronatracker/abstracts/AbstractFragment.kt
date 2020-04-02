package com.crazylegend.coronatracker.abstracts

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding


/**
 * Created by crazy on 4/2/20 to long live and prosper !
 */
abstract class AbstractFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {
    abstract val binding: ViewBinding

}