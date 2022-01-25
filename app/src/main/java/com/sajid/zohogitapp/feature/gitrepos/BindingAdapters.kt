package com.sajid.zohogitapp.feature.gitrepos

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.sajid.zohogitapp.R
import com.sajid.zohogitapp.datasources.model.GitRepo
import com.sajid.zohogitapp.feature.gitrepos.view.GitRepoListAdapter

@BindingAdapter("gitRepoListAdapter")
fun bindForecastListAdapter(
    recyclerView: RecyclerView,
    gitRepos: GitRepo?
): GitRepoListAdapter {

return getOrCreateAdapter(recyclerView,gitRepos)
}
private fun getOrCreateAdapter(recyclerView: RecyclerView,gitRepos: GitRepo?): GitRepoListAdapter {
    return if (recyclerView.adapter != null && recyclerView.adapter is GitRepoListAdapter) {
        (recyclerView.adapter as GitRepoListAdapter).apply {
            setData(gitRepos?.items?: mutableListOf())
        }
    } else {
        val bindableAdapter = GitRepoListAdapter().apply {
            setData(gitRepos?.items?: mutableListOf())
        }
        recyclerView.adapter = bindableAdapter
        val divider = DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL).apply {
            setDrawable( ContextCompat.getDrawable(
                recyclerView.context, R.drawable.line_seperator
            )!!)
        }
        recyclerView.addItemDecoration(divider)
        bindableAdapter
    }
}



@BindingAdapter("imageUrl")
fun bindImageUrlAndShowImage(imageView: ImageView,imageUrl:String?){
    if(!imageUrl.isNullOrEmpty()){
        try {


        Glide
            .with(imageView.context)
            .asBitmap()
            .override(48,48)
            .load(imageUrl)
            .fitCenter()
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(imageView);
        }
        catch (e:GlideException){
            Log.d("Failed To Load Image",imageUrl)
        }
    }

}

@BindingAdapter("isRefreshing")
fun bindSwipeRefreshState(swipeRefreshLayout: SwipeRefreshLayout,isRefreshing:Boolean=false){
    if(!isRefreshing){
    swipeRefreshLayout.isRefreshing=isRefreshing
    }
}

