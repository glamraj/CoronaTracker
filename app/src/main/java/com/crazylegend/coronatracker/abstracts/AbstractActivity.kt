package com.crazylegend.coronatracker.abstracts

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.crazylegend.kotlinextensions.rx.clearAndDispose
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


/**
 * Created by crazy on 3/29/20 to long live and prosper !
 */
abstract class AbstractActivity : AppCompatActivity() {

    abstract val binding: ViewBinding

    @Inject
    lateinit var defaultPrefs: SharedPreferences

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clearAndDispose()
    }


}