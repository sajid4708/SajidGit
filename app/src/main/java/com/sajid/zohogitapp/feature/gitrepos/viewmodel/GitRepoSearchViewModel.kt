package com.sajid.zohogitapp.feature.gitrepos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sajid.zohogitapp.common.utils.DataFetchState
import com.sajid.zohogitapp.common.utils.DataSourceState
import com.sajid.zohogitapp.common.utils.OnNetworkRetryEvent
import com.sajid.zohogitapp.datasources.DataSourceRepository
import com.sajid.zohogitapp.datasources.model.GitRepo
import com.sajid.zohogitapp.datasources.remote.ApiState
import com.sajid.zohogitapp.feature.gitrepos.GitReposConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GitRepoSearchViewModel @Inject constructor(private val gitDataSourceRepository: DataSourceRepository) :
    ViewModel(){
    private val _loadersState: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }
    val loaderState: LiveData<Boolean>
        get() = _loadersState

    private val _gitRepoSearchList: MutableLiveData<GitRepo> by lazy {
        MutableLiveData<GitRepo>()
    }

    val gitRepoSearchList: LiveData<GitRepo>
        get() = _gitRepoSearchList

    private val gitRepoListFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Empty)

    val _gitRepoListFlow: StateFlow<ApiState> = gitRepoListFlow

    private var pageNo = GitReposConfig.INITIAL_PAGE_NUMBER

    fun loadSearchData(
        dataFetchState: DataFetchState,
        searchQuery: String?,
        dataSourceState: DataSourceState
    ) {
        when (dataFetchState) {
            DataFetchState.FETCH_FIRST_DATA -> {
                pageNo = 1
                getRepoSearchListData(searchQuery, dataSourceState = dataSourceState)
            }
            DataFetchState.ADD_DATA -> {
                pageNo++
                getRepoSearchListData(
                    searchQuery,
                    isAddData = true,
                    dataSourceState = dataSourceState
                )
            }
            DataFetchState.REFRESH_DATA -> {
                //Not current search Implementation
            }

        }
    }

    private fun getRepoSearchListData(
        searchQuery: String? = "",
        isAddData: Boolean = false,
        dataSourceState: DataSourceState
    ) = viewModelScope.launch {
        if (!searchQuery.isNullOrEmpty()) {
            gitRepoListFlow.value = ApiState.Loading
            _loadersState.value = true
            if (dataSourceState == DataSourceState.ONLINE_MODE) {
                gitDataSourceRepository.getRepoSearchedListFromRemote(
                    pageNo,
                    GitReposConfig.PAGE_SIZE,
                    searchQuery
                )
                    .catch { e ->
                        gitRepoListFlow.value = ApiState.Failure(e)
                        _loadersState.value = false
                    }
                    .collect { data ->
                        gitRepoListFlow.value = ApiState.Success(data)
                        _loadersState.value = false
                        if (isAddData) {
                            _gitRepoSearchList.value = _gitRepoSearchList.value.apply {
                                if (this != null) {
                                    items.addAll(data.items)
                                }
                            }
                        } else {
                            _gitRepoSearchList.value = data
                        }
                    }
            } else {
                gitDataSourceRepository.getRepoSearchedListFromLocal(
                    pageNo,
                    GitReposConfig.PAGE_SIZE,
                    searchQuery
                )
                    .catch { e ->
                        gitRepoListFlow.value = ApiState.Failure(e)
                        _loadersState.value = false
                    }
                    .collect { data ->
                        gitRepoListFlow.value = ApiState.Success(data)
                        _loadersState.value = false
                        if (isAddData) {
                            _gitRepoSearchList.value = _gitRepoSearchList.value.apply {
                                if (this != null) {
                                    items.addAll(data.items)
                                }
                            }
                        } else {
                            _gitRepoSearchList.value = data
                        }
                    }
            }

        }
        else{
            _gitRepoSearchList.value=null
        }
    }


}