package com.sajid.zohogitapp.datasources.remote

import com.sajid.zohogitapp.datasources.model.GitRepo


sealed class ApiState{
    object Loading:ApiState()
    class Failure(val msg:Throwable):ApiState()
    object Empty:ApiState()
    class Success(val data:GitRepo):ApiState()
}


