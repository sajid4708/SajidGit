package com.sajid.zohogitapp.dbtest

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sajid.zohogitapp.datasources.DataSourceRepository
import com.sajid.zohogitapp.datasources.local.GitDatabase
import com.sajid.zohogitapp.datasources.local.dao.GitLocalDao
import com.sajid.zohogitapp.datasources.model.GitItems
import com.sajid.zohogitapp.datasources.model.Owner
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class GitDbTest @Inject constructor(private val gitLocalDao: GitLocalDao) {


}