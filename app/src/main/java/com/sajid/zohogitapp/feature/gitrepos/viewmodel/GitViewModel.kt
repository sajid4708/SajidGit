package com.sajid.zohogitapp.feature.gitrepos.viewmodel

import android.content.Context
import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sajid.zohogitapp.common.utils.DataSourceState
import com.sajid.zohogitapp.common.utils.OnSearchClickEvent
import com.sajid.zohogitapp.datasources.DataSourceRepository
import com.sajid.zohogitapp.datasources.remote.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class GitViewModel @Inject constructor(private val gitDataSourceRepository: DataSourceRepository) : ViewModel(), OnSearchClickEvent {
    private val _isInternetConnected :MutableLiveData<DataSourceState> by lazy {
        MutableLiveData<DataSourceState>()
    }
    val isInternetConnected:LiveData<DataSourceState>
        get() = _isInternetConnected

    val _searchQuery: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    private val _searchState: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    val searchState: LiveData<Boolean>
        get() = _searchState

    private val _canShowSearch: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    val canShowSearch: LiveData<Boolean>
        get() = _canShowSearch


    override fun onSearchClicked() {
        _searchQuery.value = ""
        _searchState.value = true
    }


    override fun onBackClicked() {
        _searchState.value = false
        _searchQuery.value = ""
    }


    fun isInternetConnected(isInternetConnected:DataSourceState){
        _isInternetConnected.value=isInternetConnected

    }

   suspend fun checkIfOfflineDataExist(){
        gitDataSourceRepository.checkIfOfflineDataAvailable()
            .collect { data ->
                _canShowSearch.value = data
            }
    }


}