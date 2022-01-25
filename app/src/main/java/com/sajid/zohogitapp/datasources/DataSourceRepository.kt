package com.sajid.zohogitapp.datasources

import android.util.Log
import com.sajid.zohogitapp.datasources.local.dao.GitLocalDao
import com.sajid.zohogitapp.datasources.model.GitRepo
import com.sajid.zohogitapp.datasources.remote.service.GitServiceImplementation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class DataSourceRepository @Inject constructor(private val gitServiceImplementation: GitServiceImplementation,private val gitLocalDao: GitLocalDao) {
    fun getRepoListFromRemote(pageNo:Int,pageSize:Int)= flow{
        val data=gitServiceImplementation.getRepoList(pageNo,pageSize)
        emit(data)
        gitLocalDao.insertAll(data.items)
    }.flowOn(Dispatchers.IO)

    fun getRepoSearchedListFromRemote(pageNo: Int,pageSize: Int,searchQuery:String)=flow{
        val data=gitServiceImplementation.getRepoSearchList(pageNo,pageSize,searchQuery)
        emit(data)
        gitLocalDao.insertAll(data.items)
    }.flowOn(Dispatchers.IO)

    fun getRepoListFromLocal(pageNo:Int,pageSize:Int)=flow{
        val data=GitRepo(gitLocalDao.getGitLocalList(pageNo-1,pageSize))
        emit(data)
    }.flowOn(Dispatchers.IO)

    fun getRepoSearchedListFromLocal(pageNo: Int,pageSize: Int,searchQuery:String)=flow{
        val data=GitRepo(gitLocalDao.getGitLocalListWithSearch(pageNo,pageSize,searchQuery))
        emit(data)
    }.flowOn(Dispatchers.IO)

    fun checkIfOfflineDataAvailable()= flow {
        emit(gitLocalDao.checkIfDataExist() != 0)
    }


}