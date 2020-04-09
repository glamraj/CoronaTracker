package com.crazylegend.coronatracker.abstracts

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.crazylegend.coronatracker.R
import com.crazylegend.coronatracker.adapters.NewsViewHolder
import com.crazylegend.coronatracker.consts.SCROLL_TO_TOP_VISIBILITY_THRESHOLD
import com.crazylegend.coronatracker.databinding.ItemviewNewsBinding
import com.crazylegend.coronatracker.dtos.NewsModel
import com.crazylegend.kotlinextensions.abstracts.AbstractViewBindingAdapter
import com.crazylegend.kotlinextensions.intent.openWebPage
import com.crazylegend.kotlinextensions.recyclerview.clickListeners.forItemClickListenerDSL
import com.crazylegend.kotlinextensions.recyclerview.getLastVisibleItemPosition
import com.crazylegend.kotlinextensions.recyclerview.smoothScrollTo
import com.crazylegend.kotlinextensions.rx.clearAndDispose
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown
import com.google.android.material.floatingactionbutton.FloatingActionButton
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

    @Inject
    lateinit var newsAdapter: AbstractViewBindingAdapter<NewsModel, NewsViewHolder, ItemviewNewsBinding>

    protected val notAvailableString get() = getString(R.string.not_available)

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    fun setupFabNewsClickListener(fabNews: FloatingActionButton, scrim: View) {
        fabNews.setOnClickListener {
            fabNews.isExpanded = true
        }
        scrim.setOnClickListener {
            fabNews.isExpanded = false
        }
    }

    fun setupCloseNewsClickListener(fabNews: FloatingActionButton, closeNews: AppCompatImageView) {
        closeNews.setOnClickListenerCooldown {
            if (fabNews.isExpanded) {
                fabNews.isExpanded = false
            }
        }
    }

    fun setupNews(news: List<NewsModel>, fabNews: FloatingActionButton) {
        if (news.isNullOrEmpty()) {
            fabNews.hide()
        } else {
            fabNews.show()
            newsAdapter.submitList(news)
        }
    }

    fun recyclerScrollBackToTop(backToTop: FloatingActionButton?, recycler: RecyclerView?, adapter: RecyclerView.Adapter<*>) {
        recycler?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                adapter.apply {
                    recyclerView.getLastVisibleItemPosition()?.apply {
                        if (this > SCROLL_TO_TOP_VISIBILITY_THRESHOLD) {
                            backToTop?.show()
                        } else {
                            backToTop?.hide()
                        }
                    }
                }
            }
        })
        backToTop?.setOnClickListener {
            recycler?.smoothScrollTo(0)
        }
    }

    fun setupForNewsItemClickListener(fabNews: FloatingActionButton) {
        newsAdapter.forItemClickListener = forItemClickListenerDSL { _, item, _ ->
            val url = item.sourceURL
            if (url.isNullOrEmpty()) return@forItemClickListenerDSL
            else {
                fabNews.isExpanded = false
                requireContext().openWebPage(url)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clearAndDispose()
    }

}