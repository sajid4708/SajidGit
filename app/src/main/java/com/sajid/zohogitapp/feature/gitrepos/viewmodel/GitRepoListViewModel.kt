package com.sajid.zohogitapp.feature.gitrepos.viewmodel

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
class GitRepoListViewModel @Inject constructor(private val gitDataSourceRepository: DataSourceRepository) : ViewModel() {
    private val _loadersState:MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val loaderState:LiveData<Boolean>
    get() = _loadersState

    private val _swipeLoadersState:MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val swipeLoaderState:LiveData<Boolean>
        get() = _swipeLoadersState

    private val _gitRepoList:MutableLiveData<GitRepo> by lazy {
        MutableLiveData<GitRepo>()
    }

    val gitRepoList: LiveData<GitRepo>
        get() = _gitRepoList

    private val gitRepoListFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Empty)

    val _gitRepoListFlow :StateFlow<ApiState> = gitRepoListFlow

    fun getRepoListData(pageNo:Int,pageSize:Int)=viewModelScope.launch {
        gitRepoListFlow.value = ApiState.Loading
        gitDataSourceRepository.getRepoListFromRemote(pageNo,pageSize)
            .catch {e->
                gitRepoListFlow.value = ApiState.Failure(e)
                _swipeLoadersState.value=false}
            .collect {data->
                gitRepoListFlow.value=ApiState.Success(data)
                _gitRepoList.value=data
                _swipeLoadersState.value=false}
    }

    fun  addRepoListData(pageNo:Int,pageSize:Int)=viewModelScope.launch {
        gitRepoListFlow.value = ApiState.Loading
        _loadersState.value=true
        gitDataSourceRepository.getRepoListFromRemote(pageNo,pageSize)
            .catch {e->
                gitRepoListFlow.value = ApiState.Failure(e)
                _loadersState.value=false
            }
            .collect {data->

                _loadersState.value=false
                gitRepoListFlow.value=ApiState.Success(data)
                _gitRepoList.value=_gitRepoList.value.apply {
                  if(this!=null){
                      items.addAll(data.items)
                  }
                }

            }
    }


}