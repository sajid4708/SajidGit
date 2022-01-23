package com.sajid.zohogitapp.common.utils

import androidx.recyclerview.widget.DiffUtil
import com.sajid.zohogitapp.datasources.model.Items

class GitHubRepoDiffUtil(private val oldList:List<Items>, private val newList:List<Items>) : DiffUtil.Callback () {


    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].id == newList[newItemPosition].id)
    }


}