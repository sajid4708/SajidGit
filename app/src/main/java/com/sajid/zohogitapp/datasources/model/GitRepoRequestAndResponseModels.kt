package com.sajid.zohogitapp.datasources.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


data class GitRepo(
    @SerializedName("items") var items: MutableList<GitItems> = mutableListOf()

)
@Entity(tableName = "git_items")
data class GitItems(
    @PrimaryKey
    @SerializedName("id")
    val id :Long=-1,
    @Embedded
    @SerializedName("owner")
    val owner: Owner? = Owner(),
    @ColumnInfo
    @SerializedName("name")
    val name: String? = null,
    @ColumnInfo
    @SerializedName("description")
    val description: String? = null,
    @ColumnInfo
    @SerializedName("stargazers_count")
    val stargazersCount: Int = 0,
    @ColumnInfo
    @SerializedName("language")
    val language: String? = null,
    )


data class Owner(
    @ColumnInfo
    @SerializedName("login")
    var login: String? = null,
    @ColumnInfo
    @SerializedName("avatar_url")
    var avatarUrl: String? = null,
    )

data class ErrorResponse(@SerializedName("message") var errorMessage: String? = null,)
