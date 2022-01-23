package com.sajid.zohogitapp.feature.gitrepos.viewmodel

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sajid.zohogitapp.datasources.DataSourceRepository
import com.sajid.zohogitapp.datasources.model.GitRepo
import com.sajid.zohogitapp.datasources.remote.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class GitRepoSearchViewModel @Inject constructor(private val gitDataSourceRepository: DataSourceRepository) : ViewModel() {
    private val _loadersState:MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }
    val loaderState:LiveData<Boolean>
        get() = _loadersState

    private val _gitRepoSearchList: MutableLiveData<GitRepo> by lazy {
        MutableLiveData<GitRepo>()
    }

    val gitRepoSearchList: LiveData<GitRepo>
        get() = _gitRepoSearchList

    private val gitRepoListFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Empty)

    val _gitRepoListFlow : StateFlow<ApiState> = gitRepoListFlow

    fun getRepoSearchListData(pageNo:Int,pageSize:Int,searchQuery:String?="")=viewModelScope.launch {
        if(!searchQuery.isNullOrEmpty()) {
            gitRepoListFlow.value= ApiState.Loading
            _loadersState.value=true
            gitDataSourceRepository.getRepoSearchedListFromRemote(pageNo, pageSize, searchQuery)
                .catch { e ->
                    gitRepoListFlow.value = ApiState.Failure(e)
                    _gitRepoSearchList.value = null
                    _loadersState.value=false
                }
                .collect { data ->
                    _gitRepoSearchList.value = data
                    gitRepoListFlow.value = ApiState.Success(data)
                    _loadersState.value=false
                }
        }
    }



}