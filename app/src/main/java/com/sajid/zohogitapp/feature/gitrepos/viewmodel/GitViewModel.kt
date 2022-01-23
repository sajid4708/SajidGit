package com.sajid.zohogitapp.feature.gitrepos.viewmodel

import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sajid.zohogitapp.common.utils.OnSearchClickEvent
import com.sajid.zohogitapp.datasources.DataSourceRepository
import com.sajid.zohogitapp.datasources.model.GitRepo
import com.sajid.zohogitapp.datasources.model.Items
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GitViewModel @Inject constructor() : ViewModel(), OnSearchClickEvent {
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

}