package com.sajid.zohogitapp.common.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerEndScrollListner :RecyclerView.OnScrollListener(){
    abstract var canLoad:Boolean

    abstract fun loadMoreData()
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if ((recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() >= (recyclerView.layoutManager as LinearLayoutManager).itemCount - 1 ){
            if(canLoad){
            loadMoreData()
        }
        }
    }


}