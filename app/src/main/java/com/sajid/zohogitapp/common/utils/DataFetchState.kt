package com.sajid.zohogitapp.common.utils
/**
It is used to represent Data fetch State from Api Or Remote
REFRESH_DATA-> Delete all existing data and fetch new
 ADD_DATA->Fetches Data and add items to existing list
FETCH_FIRST_DATA->Fetches Data with and creates list and shows Progress bar
 */
enum class DataFetchState {
    REFRESH_DATA,ADD_DATA,FETCH_FIRST_DATA
}