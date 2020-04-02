package com.crazylegend.coronatracker

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crazylegend.kotlinextensions.context.getCompatColor
import com.crazylegend.kotlinextensions.retrofit.RetrofitResult
import com.crazylegend.kotlinextensions.retrofit.callErrorPost
import com.crazylegend.kotlinextensions.retrofit.loadingPost
import com.crazylegend.kotlinextensions.retrofit.successPost
import com.crazylegend.kotlinextensions.rx.clearAndDispose
import com.crazylegend.kotlinextensions.rx.ioThreadScheduler
import com.crazylegend.kotlinextensions.rx.mainThreadScheduler
import com.crazylegend.kotlinextensions.rx.singleFrom
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


/**
 * Created by crazy on 3/29/20 to long live and prosper !
 */
class MainActivityViewModel : ViewModel(){

    private val compositeDisposable = CompositeDisposable()

    private val documentData: MutableLiveData<RetrofitResult<Document>> = MutableLiveData()
    val document: LiveData<RetrofitResult<Document>> = documentData

    init {
        fetchData()
    }

    fun fetchData() {
        documentData.loadingPost()
        singleFrom {
            Jsoup.connect(PRIMARY_URL)
                .timeout(10000)
                .get()
        }.subscribeOn(ioThreadScheduler)
            .observeOn(mainThreadScheduler)
            .subscribe({
                documentData.successPost(it)
            }, {
                documentData.callErrorPost(it)
                it.printStackTrace()
            }).addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clearAndDispose()
    }
}