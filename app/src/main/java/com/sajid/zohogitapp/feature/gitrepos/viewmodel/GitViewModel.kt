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

    /**
    Holds Current Network Status and Updates Ui based on that
     */
    private val _isInternetConnected :MutableLiveData<DataSourceState> by lazy {
        MutableLiveData<DataSourceState>()
    }
    val isInternetConnected:LiveData<DataSourceState>
        get() = _isInternetConnected


    /**
    Holds String value of searched text in EditText
     and this value is observed by Search Fragment
     */
    val _searchQuery: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    /**
    Holds current Ui State
     "true" Search Fragment will be Shown
     "false"List Fragment will be shown
     */
    private val _searchState: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    val searchState: LiveData<Boolean>
        get() = _searchState

    /**
     * This Value is Updated Based on Network Status
     * and is Offline Data Available
    Holds search icons Ui state
     "true" search Icon visible
     "false" search Icon gone
     */
    private val _canShowSearch: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    val canShowSearch: LiveData<Boolean>
        get() = _canShowSearch

    /**
   executed when search icon clicked
     */
    override fun onSearchClicked() {
        _searchQuery.value = ""
        _searchState.value = true
    }

    /**
    executed when back icon clicked
     */
    override fun onBackClicked() {
        _searchState.value = false
        _searchQuery.value = ""
    }

    /**
    Used to update current Network Status from Activity
     */
    fun isInternetConnected(isInternetConnected:DataSourceState){
        _isInternetConnected.value=isInternetConnected

    }

    /**
    Used to check if Offline Data Exist
     */
   suspend fun checkIfOfflineDataExist(){
        gitDataSourceRepository.checkIfOfflineDataAvailable()
            .collect { data ->
                _canShowSearch.value = data
            }
    }


}