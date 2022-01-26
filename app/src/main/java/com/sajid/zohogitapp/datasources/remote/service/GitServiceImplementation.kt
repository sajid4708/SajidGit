package com.sajid.zohogitapp.datasources.remote.service

import com.google.gson.Gson
import com.sajid.zohogitapp.common.utils.ApiService
import com.sajid.zohogitapp.datasources.model.GitRepo
import javax.inject.Inject

class GitServiceImplementation @Inject constructor(private val gitService: GitService) {
    suspend fun getRepoList(pageNo: Int, pageSize: Int)=
        ApiService.getApiResponse(gitService.getRepoListWithSearch(pageNo,pageSize))



    suspend fun getRepoSearchList(pageNo: Int, pageSize: Int, searchQuery: String)=
        ApiService.getApiResponse(gitService.getRepoListWithSearch(pageNo,pageSize,searchQuery))

}