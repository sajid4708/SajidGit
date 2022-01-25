package com.sajid.zohogitapp.feature.gitrepos.viewmodel

import android.content.Context
import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sajid.zohogitapp.common.utils.DataSourceState
import com.sajid.zohogitapp.common.utils.OnSearchClickEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GitViewModel @Inject constructor() : ViewModel(), OnSearchClickEvent {
    private val _isInternetConnected :MutableLiveData<DataSourceState> by lazy {
        MutableLiveData<DataSourceState>()
    }
    val isInternetConnected:LiveData<DataSourceState>
        get() = _isInternetConnected

    private val _searchQuery: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }
    val searchQuery:LiveData<String>
    get() = _searchQuery

    private val _searchState: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    val searchState: LiveData<Boolean>
        get() = _searchState


    override fun onSearchClicked() {
        _searchState.value = true
        _searchQuery.value = ""
    }


    override fun onBackClicked() {
        _searchState.value = false
        _searchQuery.value = ""
    }
    fun onTextChange(editable: Editable?) {
        _searchQuery.value=editable.toString()
        Log.i("searcher",editable.toString())
    }

    fun isInternetConnected(isInternetConnected:DataSourceState){
        _isInternetConnected.value=isInternetConnected
    }


}