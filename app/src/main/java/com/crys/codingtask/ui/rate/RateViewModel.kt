package com.crys.codingtask.ui.rate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crys.codingtask.data.model.LatestResponse
import com.crys.codingtask.data.model.SelectedDateResponse
import com.crys.codingtask.model.CurrencyRecyclerViewItem
import com.crys.codingtask.other.Converter.ratesToListOfCurrency
import com.crys.codingtask.other.Event
import com.crys.codingtask.other.Resource
import com.crys.codingtask.other.Status
import com.crys.codingtask.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class RateViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    private val _result = MutableLiveData<Event<Resource<List<CurrencyRecyclerViewItem>>>>()
    val result : LiveData<Event<Resource<List<CurrencyRecyclerViewItem>>>> = _result

    private val currencyList = mutableListOf<CurrencyRecyclerViewItem>()
    private var lastDate = ""

    fun getLatestResponse() = viewModelScope.launch {
        _result.postValue(Event(Resource.loading(null)))
        try {
            val response = repository.getLatestResponse()
            if (response.status == Status.ERROR) {
                _result.postValue(Event(Resource.error(response.message ?: "An unknown error occurred", null)))
            }
            handleSuccessfulLatestResponse(response)
        } catch (e: Exception) {
            _result.postValue(Event(Resource.error(e.message ?: "An unknown error occurred", null)))
        }
    }

    fun selectedDateResponse() = viewModelScope.launch {
        try {
            val response = repository.getSelectedDateResponse(dayBefore(lastDate))
            if (response.status == Status.ERROR) {
                _result.postValue(Event(Resource.error(response.message ?: "An unknown error occurred", null)))
            }
            handleSuccessfulSelectedResponse(response)
        } catch (e: Exception) {
            _result.postValue(Event(Resource.error(e.message ?: "An unknown error occurred", null)))
        }
    }

    private fun handleSuccessfulLatestResponse(response: Resource<LatestResponse>) {
       val listOfCurrency = response.data?.let { ratesToListOfCurrency(it.rates, response.data.date) }
        currencyList.addAll(listOfCurrency!!)
        _result.postValue(Event(Resource.success(currencyList)))
        lastDate = response.data.date
    }

    private fun handleSuccessfulSelectedResponse(response: Resource<SelectedDateResponse>) {
        val listOfCurrency = response.data?.let { ratesToListOfCurrency(it.rates, response.data.date) }
        currencyList.addAll(listOfCurrency!!)
        _result.postValue(Event(Resource.success(currencyList)))
        lastDate = response.data.date
    }

    //function to decrease the day by one day
    private fun dayBefore(dateString: String) : String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.parse(dateString)
        val calendar = Calendar.getInstance()
        calendar.time = date!!
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        return dateFormat.format(calendar.time)
    }
}