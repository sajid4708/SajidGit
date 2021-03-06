package com.sajid.zohogitapp.datasources.local.dao

import androidx.room.*
import com.bumptech.glide.Glide
import com.sajid.zohogitapp.datasources.model.GitItems


@Dao
interface GitLocalDao {
    @Query("SELECT * FROM git_items limit :limit offset :offset")
   suspend fun getGitLocalList(offset:Int,limit:Int):MutableList<GitItems>

    @Query("SELECT * FROM git_items where name like '%'||:searchQuery||'%' or description like '%'||:searchQuery||'%' or language like '%'||:searchQuery||'%' or login like '%'||:searchQuery||'%' limit :limit offset :offset ")
    suspend fun getGitLocalListWithSearch(offset:Int,limit:Int,searchQuery:String):MutableList<GitItems>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGitItem(gitItems: GitItems)

    @Query("SELECT EXISTS (SELECT 1 FROM git_items)")
    suspend fun checkIfDataExist():Int

    suspend fun insertAll(list: MutableList<GitItems>){
        for(item in list){
            insertGitItem(item)

        }
    }
}