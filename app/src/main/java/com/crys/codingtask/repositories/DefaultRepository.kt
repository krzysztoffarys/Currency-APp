package com.crys.codingtask.repositories

import com.crys.codingtask.data.CurrencyApi
import com.crys.codingtask.data.model.LatestResponse
import com.crys.codingtask.data.model.SelectedDateResponse
import com.crys.codingtask.other.Resource
import javax.inject.Inject

class DefaultRepository @Inject constructor(
    private val api: CurrencyApi
) : Repository {
    //the function to return the latest response, if the response is not successful or the body is empty, returns error
    override suspend fun getLatestResponse(): Resource<LatestResponse> {
        val response = api.getLatest()
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.success(result)
            }
            return Resource.error("Response body is empty", null)
        }
        return Resource.error(response.message(), null)
    }

    //the function to return the response at selected day, if the response is not successful or the body is empty, returns error
    override suspend fun getSelectedDateResponse(date: String): Resource<SelectedDateResponse> {
        val response = api.getHistorical(date = date)
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.success(result)
            }
            return Resource.error("Response body is empty", null)
        }
        return Resource.error(response.message(), null)
    }
}