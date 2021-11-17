package com.crys.codingtask.data

import com.crys.codingtask.data.model.LatestResponse
import com.crys.codingtask.data.model.SelectedDateResponse
import com.crys.codingtask.other.Key.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CurrencyApi {
    @GET("/latest")
    suspend fun getLatest(
        @Query("access_key")
        key: String = API_KEY
    ) : Response<LatestResponse>


    @GET("/{date}")
    suspend fun getHistorical(
        @Path("date")
        date: String,
        @Query("access_key")
        key: String = API_KEY
    ) : Response<SelectedDateResponse>

}