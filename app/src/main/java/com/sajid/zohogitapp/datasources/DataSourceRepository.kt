package com.sajid.zohogitapp.datasources

import com.sajid.zohogitapp.datasources.remote.service.GitServiceImplementation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DataSourceRepository @Inject constructor(private val gitServiceImplementation: GitServiceImplementation) {
    fun getRepoListFromRemote(pageNo:Int,pageSize:Int)= flow{
        emit(gitServiceImplementation.getRepoList(pageNo,pageSize))
    }.flowOn(Dispatchers.IO)

    fun getRepoSearchedListFromRemote(pageNo: Int,pageSize: Int,searchQuery:String)=flow{
        emit(gitServiceImplementation.getRepoSearchList(pageNo,pageSize,searchQuery))
    }.flowOn(Dispatchers.IO)

}