package com.crys.codingtask.ui.rate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crys.codingtask.data.model.LatestResponse
import com.crys.codingtask.other.Event
import com.crys.codingtask.other.Resource
import com.crys.codingtask.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RateViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    //we use the event class to handle error messages one time, not multiple times by screen rotation
    private val _latestResponse = MutableLiveData<Event<Resource<LatestResponse>>>()
    val latestResponse: LiveData<Event<Resource<LatestResponse>>> = _latestResponse

    private val _selectedDateResponse = MutableLiveData<Event<Resource<LatestResponse>>>()
    val selectedDateResponse: LiveData<Event<Resource<LatestResponse>>> = _selectedDateResponse

    var isInternetConnection = true
    lateinit var lastDate : String

    fun getLatestResponse() = viewModelScope.launch {
    }

    fun selectedDateResponse() = viewModelScope.launch {

    }
}