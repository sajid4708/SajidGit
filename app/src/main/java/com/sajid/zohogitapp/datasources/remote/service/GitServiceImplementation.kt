package com.sajid.zohogitapp.datasources.remote.service

import javax.inject.Inject

class GitServiceImplementation @Inject constructor(private val gitService: GitService) {
    suspend fun getRepoList(pageNo: Int, pageSize: Int) =
        gitService.getRepoListWithSearch(pageNo, pageSize)

    suspend fun getRepoSearchList(pageNo: Int, pageSize: Int, searchQuery: String) =
        gitService.getRepoListWithSearch(pageNo, pageSize, searchQuery)

}