package com.sajid.zohogitapp.datasources.remote.service

import com.sajid.zohogitapp.datasources.model.GitRepo
import retrofit2.http.GET
import retrofit2.http.Query

interface GitService {
    @GET("/search/repositories")
    suspend fun getRepoListWithSearch(@Query(value="page") page:Int,@Query(value="per_page") pageSize: Int,@Query(value = "q") searchQuery:String="s"):GitRepo

}