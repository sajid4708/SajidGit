package com.sajid.zohogitapp.datasources.remote



sealed class ApiState{
    object Loading:ApiState()
    class Failure(val msg:Throwable):ApiState()
    object Empty:ApiState()
    class Success<T>(val data:T):ApiState()
}


