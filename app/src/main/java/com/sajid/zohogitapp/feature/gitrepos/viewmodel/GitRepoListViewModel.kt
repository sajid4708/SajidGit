package com.sajid.zohogitapp.feature.gitrepos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
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
class GitRepoListViewModel @Inject constructor(private val gitDataSourceRepository: DataSourceRepository) : ViewModel(),OnNetworkRetryEvent {
    /**
    Show Network Error view if error state is true
     */
    private val _isErrorState:MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val isErrorState:LiveData<Boolean>
    get() = _isErrorState


    /**
    Show bottom pagination loader based on this status
     */
    private val _loadersState:MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val loaderState:LiveData<Boolean>
    get() = _loadersState


    /**
    Hides Swipe Loader Based on this status
     */
    private val _swipeLoadersState:MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val swipeLoaderState:LiveData<Boolean>
        get() = _swipeLoadersState


    /**
    Shows Listed Data to Recycler View
     */
    private val _gitRepoList:MutableLiveData<GitRepo> by lazy {
        MutableLiveData<GitRepo>()
    }

    val gitRepoList: LiveData<GitRepo>
        get() = _gitRepoList


    /**
    Holds data from Data Sources and Updates Live Data
     */
    private val gitRepoListFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Empty)

    val _gitRepoListFlow :StateFlow<ApiState> = gitRepoListFlow


    /**
    Current page Number of Data List
     */
    private var _pageNo=GitReposConfig.INITIAL_PAGE_NUMBER


    /**
    show Initial Data fetch Loader Based ont his status
     */
    private val _initialLoader:MutableLiveData<Boolean> by lazy{
        MutableLiveData<Boolean>()
    }

    val initialLoader:LiveData<Boolean>
    get() = _initialLoader



    /**
    Temporary List gets Data from Flow
     and Updates Live Data List
     */
    var tempList= mutableListOf<GitItems>()


    fun loadGitRepoListData(dataFetchState: DataFetchState,dataSourceState: DataSourceState){
        when(dataFetchState){
          DataFetchState.FETCH_FIRST_DATA->{
              if( _gitRepoList.value?.items.isNullOrEmpty()){
                  _initialLoader.value=true
                  _pageNo=1
                  getRepoListData(_pageNo,GitReposConfig.PAGE_SIZE,dataSourceState)

              }
          }
            DataFetchState.REFRESH_DATA->{
                _pageNo=1
                getRepoListData(GitReposConfig.INITIAL_PAGE_NUMBER,GitReposConfig.PAGE_SIZE,dataSourceState)
            }
            DataFetchState.ADD_DATA->{
                _pageNo++
                addRepoListData(_pageNo, GitReposConfig.PAGE_SIZE,dataSourceState)
            }
        }
    }


    private fun getRepoListData(pageNo:Int,pageSize:Int,dataSourceState: DataSourceState)=viewModelScope.launch {
        _swipeLoadersState.value=true
        gitRepoListFlow.value = ApiState.Loading
        gitDataSourceRepository.getGitRepoListData(pageNo,pageSize,dataSourceState)
            .catch { e ->
                gitRepoListFlow.value = ApiState.Failure(e)
                _swipeLoadersState.value = false
                _initialLoader.value=false
            }
            .collect { data ->
                gitRepoListFlow.value = ApiState.Success(data)
                tempList.clear()
                tempList.addAll(data.items)
                _gitRepoList.value =data.apply {
                    items=tempList.toSet().toMutableList()
                }
                _swipeLoadersState.value = false
                _initialLoader.value=false
                if(dataSourceState==DataSourceState.ONLINE_MODE){
                _isErrorState.value=false
                }

            }

    }


    private fun  addRepoListData(pageNo:Int,pageSize:Int,dataSourceState: DataSourceState)=viewModelScope.launch {
        gitRepoListFlow.value = ApiState.Loading
        _loadersState.value=true
        gitDataSourceRepository.getGitRepoListData(pageNo,pageSize,dataSourceState)
            .catch {e->
                gitRepoListFlow.value = ApiState.Failure(e)
                _loadersState.value=false
            }
            .collect {data->
                tempList.addAll(data.items)
                _gitRepoList.value =data.apply {
                    items=tempList.toSet().toMutableList()
                }
                _loadersState.value=false
                gitRepoListFlow.value=ApiState.Success(data)



            }
    }

suspend fun setErrorState(isError:Boolean){
    gitDataSourceRepository.checkIfOfflineDataAvailable()
        .collect { data ->
            _isErrorState.value=isError && !data

        }
}
    override fun onNetworkRetryClicked() {
        getRepoListData(GitReposConfig.INITIAL_PAGE_NUMBER,GitReposConfig.PAGE_SIZE,DataSourceState.ONLINE_MODE)
    }
}