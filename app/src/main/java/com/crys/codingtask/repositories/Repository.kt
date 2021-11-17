package com.crys.codingtask.repositories

import com.crys.codingtask.data.model.LatestResponse
import com.crys.codingtask.data.model.SelectedDateResponse
import com.crys.codingtask.other.Resource

interface Repository {

    suspend fun getLatestResponse() : Resource<LatestResponse>

    suspend fun getSelectedDateResponse(date: String) : Resource<SelectedDateResponse>
}