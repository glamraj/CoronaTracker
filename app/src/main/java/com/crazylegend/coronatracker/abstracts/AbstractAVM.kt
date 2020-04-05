package com.crazylegend.coronatracker.abstracts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.crazylegend.kotlinextensions.rx.clearAndDispose
import io.reactivex.disposables.CompositeDisposable


/**
 * Created by crazy on 4/5/20 to long live and prosper !
 */
abstract class AbstractAVM(application: Application) : AndroidViewModel(application) {
    protected val compositeDisposable = CompositeDisposable()


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clearAndDispose()
    }
}