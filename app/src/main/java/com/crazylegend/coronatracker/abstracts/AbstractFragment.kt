package com.crazylegend.coronatracker.abstracts

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.crazylegend.kotlinextensions.rx.clearAndDispose
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


/**
 * Created by crazy on 4/2/20 to long live and prosper !
 */
abstract class AbstractFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {
    abstract val binding: ViewBinding

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }


    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clearAndDispose()
    }

}