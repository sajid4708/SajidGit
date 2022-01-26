package com.sajid.zohogitapp.common.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
/**
It is used by RecylerView to see if current item is last item and fetches new data
 to show
 */
abstract class RecyclerEndScrollListner :RecyclerView.OnScrollListener(){
    abstract var canLoad:Boolean

    abstract fun loadMoreData()
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        //Not needed to Implement
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if(canLoad){
        if ((recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() >= (recyclerView.layoutManager as LinearLayoutManager).itemCount - 1 ){
            loadMoreData()
        }
        }
    }


}