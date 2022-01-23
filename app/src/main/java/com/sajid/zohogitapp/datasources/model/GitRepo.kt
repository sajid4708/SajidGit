package com.sajid.zohogitapp.datasources.model

import com.google.gson.annotations.SerializedName


data class GitRepo(
    @SerializedName("items") var items: MutableList<Items> = mutableListOf()

)

data class Items(
    @SerializedName("id") val id :Long=-1,
    @SerializedName("owner") val owner: Owner? = Owner(),
    @SerializedName("name") val name: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("stargazers_count") val stargazersCount: Int = 0,
    @SerializedName("language") val language: String? = null,

    )


data class Owner(

    @SerializedName("login") var login: String? = null,
    @SerializedName("avatar_url") var avatarUrl: String? = null,

    )

data class ErrorResponse(@SerializedName("message") var errorMessage: String? = null,)
