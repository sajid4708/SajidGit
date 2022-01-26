package com.sajid.zohogitapp.feature.gitrepos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sajid.zohogitapp.common.utils.DataFetchState
import com.sajid.zohogitapp.common.utils.DataSourceState
import com.sajid.zohogitapp.common.utils.OnNetworkRetryEvent
import com.sajid.zohogitapp.datasources.DataSourceRepository
import com.sajid.zohogitapp.datasources.model.GitItems
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

    /**
    Show bottom pagination loader based on this status
     */
    private val _loadersState: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }
    val loaderState: LiveData<Boolean>
        get() = _loadersState


    /**
    Shows Listed Data to Recycler View
     */
    private val _gitRepoSearchList: MutableLiveData<GitRepo> by lazy {
        MutableLiveData<GitRepo>()
    }

    val gitRepoSearchList: LiveData<GitRepo>
        get() = _gitRepoSearchList


    /**
    Holds data from Data Sources and Updates Live Data
     */
    private val gitRepoListFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Empty)

    val _gitRepoListFlow: StateFlow<ApiState> = gitRepoListFlow


    /**
    Current page Number of Data List
     */
    private var pageNo = GitReposConfig.INITIAL_PAGE_NUMBER


    /**
    Temporary List gets Data from Flow
    and Updates Live Data List
     */
    var tempList= mutableListOf<GitItems>()


    fun loadSearchData(
        dataFetchState: DataFetchState,
        searchQuery: String?,
        dataSourceState: DataSourceState
    ) {
        when (dataFetchState) {
            DataFetchState.FETCH_FIRST_DATA -> {
                pageNo = 1
                getRepoSearchData(searchQuery, dataSourceState = dataSourceState)
            }
            DataFetchState.ADD_DATA -> {
                pageNo++
                getRepoSearchData(
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

    private fun getRepoSearchData(
        searchQuery: String? = "",
        isAddData: Boolean = false,
        dataSourceState: DataSourceState
    ) = viewModelScope.launch {
        if (!searchQuery.isNullOrEmpty()) {
            gitRepoListFlow.value = ApiState.Loading
            _loadersState.value = true
            gitDataSourceRepository.getGitRepoSearchListData(
                pageNo, GitReposConfig.PAGE_SIZE, searchQuery, dataSourceState
            )
                .catch { e->
                    gitRepoListFlow.value = ApiState.Failure(e)
                    _loadersState.value = false
                    if(!isAddData) {
                        _gitRepoSearchList.value = null
                    }
                }
                .collect {data->
                    gitRepoListFlow.value = ApiState.Success(data)
                    _loadersState.value = false
                    if (isAddData) {
                        if(!data.items.isNullOrEmpty()){
                            tempList.addAll(data.items)
                            _gitRepoSearchList.value =data.apply {
                                //Coverted to Set then list because remote
                                // and Local data May conflict and have Duplicated
                                items=tempList.toSet().toMutableList()
                            }
                        }
                    } else {
                        tempList.clear()
                        tempList.addAll(data.items)
                        _gitRepoSearchList.value =data.apply {
                            //Coverted to Set then list because remote
                            // and Local data May conflict and have Duplicated
                            items=tempList.toSet().toMutableList()
                        }
                    }
                }
        }
        else{
            _gitRepoSearchList.value=null
        }
    }


    }