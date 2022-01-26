package com.sajid.zohogitapp.datasources

import android.util.Log
import com.sajid.zohogitapp.common.utils.DataSourceState
import com.sajid.zohogitapp.datasources.local.dao.GitLocalDao
import com.sajid.zohogitapp.datasources.model.GitRepo
import com.sajid.zohogitapp.datasources.remote.service.GitServiceImplementation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class DataSourceRepository @Inject constructor(
    private val gitServiceImplementation: GitServiceImplementation,
    private val gitLocalDao: GitLocalDao
) {

    fun checkIfOfflineDataAvailable() = flow {
        emit(gitLocalDao.checkIfDataExist() != 0)
    }.flowOn(Dispatchers.IO)

    fun getGitRepoListData(pageNo: Int, pageSize: Int, dataSourceState: DataSourceState) =
        when (dataSourceState) {
            DataSourceState.ONLINE_MODE -> {
                getRepoListFromRemote(pageNo, pageSize)
            }
            DataSourceState.OFFLINE_MODE -> {
                getRepoListFromLocal(pageNo, pageSize)
            }

        }.flowOn(Dispatchers.IO)

    fun getGitRepoSearchListData(
        pageNo: Int,
        pageSize: Int,
        searchQuery: String,
        dataSourceState: DataSourceState
    ) = when (dataSourceState) {
        DataSourceState.ONLINE_MODE -> {
            getRepoSearchedListFromRemote(pageNo, pageSize, searchQuery)
        }
        DataSourceState.OFFLINE_MODE -> {
            getRepoSearchedListFromLocal(pageNo, pageSize, searchQuery)
        }
    }.flowOn(Dispatchers.IO)

    private fun getRepoListFromRemote(pageNo: Int, pageSize: Int) = flow {
        val data = gitServiceImplementation.getRepoList(pageNo, pageSize)
        if(data.isError){
            error(data.message)
        }
        else{
            emit(data)
        }
        gitLocalDao.insertAll(data.items)
    }.flowOn(Dispatchers.IO)

    private fun getRepoListFromLocal(pageNo: Int, pageSize: Int) = flow {
        val data = GitRepo(gitLocalDao.getGitLocalList(pageNo - 1, pageSize))
        if (data.items.isNullOrEmpty()) {
            error("No data available in local")
        } else {
            emit(data)
        }

    }.flowOn(Dispatchers.IO)

     fun getRepoSearchedListFromRemote(pageNo: Int, pageSize: Int, searchQuery: String) =
        flow {
            val data = gitServiceImplementation.getRepoSearchList(pageNo, pageSize, searchQuery)
            if(data.isError){
                error(data.message)
            }
            else{
                emit(data)
            }
            gitLocalDao.insertAll(data.items)
        }.flowOn(Dispatchers.IO)


     fun getRepoSearchedListFromLocal(pageNo: Int, pageSize: Int, searchQuery: String) =
        flow {
            val data =
                GitRepo(gitLocalDao.getGitLocalListWithSearch(pageNo - 1, pageSize, searchQuery))
            if (data.items.isNullOrEmpty()) {
                error("No data available in local")
            } else {
                emit(data)
            }
        }.flowOn(Dispatchers.IO)


}