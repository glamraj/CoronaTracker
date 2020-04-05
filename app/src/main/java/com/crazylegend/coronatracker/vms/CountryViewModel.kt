package com.crazylegend.coronatracker.vms

import android.annotation.SuppressLint
import android.app.Application
import com.crazylegend.coronatracker.abstracts.AbstractAVM
import com.crazylegend.coronatracker.consts.PRIMARY_URL
import com.crazylegend.kotlinextensions.rx.ioThreadScheduler
import com.crazylegend.kotlinextensions.rx.mainThreadScheduler
import com.crazylegend.kotlinextensions.rx.singleFrom
import io.reactivex.rxkotlin.addTo
import org.jsoup.Jsoup


/**
 * Created by crazy on 4/5/20 to long live and prosper !
 */
class CountryViewModel(application: Application, private val country: String) : AbstractAVM(application) {


    @SuppressLint("DefaultLocale")
    fun fetchData() {
        singleFrom {
            Jsoup.connect("$PRIMARY_URL/country/${country.toLowerCase()}")
                    .timeout(10000)
                    .get()
        }.subscribeOn(ioThreadScheduler)
                .observeOn(mainThreadScheduler)
                .subscribe({

                }, {
                    it.printStackTrace()
                }).addTo(compositeDisposable)
    }


}