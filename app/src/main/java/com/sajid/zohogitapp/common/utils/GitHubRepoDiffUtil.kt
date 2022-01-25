package com.sajid.zohogitapp.common.utils

import androidx.recyclerview.widget.DiffUtil
import com.sajid.zohogitapp.datasources.model.GitItems

class GitHubRepoDiffUtil(private val oldList:List<GitItems>, private val newList:List<GitItems>) : DiffUtil.Callback () {


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