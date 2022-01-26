package com.sajid.zohogitapp.common.utils

import com.sajid.zohogitapp.datasources.model.GitRepo
import retrofit2.Response

object ApiService {
    fun getApiResponse(apiData: Response<GitRepo>): GitRepo {
        return if (apiData.isSuccessful) {
            apiData.body().apply {
                this?.errorCode = apiData.code()
            } ?: GitRepo(
                message = apiData.errorBody()?.byteStream().toString(),
                isError = true,
                errorCode = apiData.code()
            )
        } else {
            GitRepo(
                message = apiData.errorBody()?.byteStream().toString(),
                isError = true,
                errorCode = apiData.code()
            )
        }
    }
}