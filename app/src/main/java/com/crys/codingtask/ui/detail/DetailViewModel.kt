package com.crys.codingtask.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.crys.codingtask.R
import com.crys.codingtask.model.CustomSpinnerItem
import com.crys.codingtask.util.Converter.popularCurrency
import com.crys.codingtask.util.Converter.roundCurrency
import timber.log.Timber

class DetailViewModel : ViewModel() {

    private val _result = MutableLiveData<String>()
    val result: LiveData<String> = _result

    //beginning list for spinner
    private val list = mutableListOf(
        CustomSpinnerItem(R.drawable.eur_flag, "EUR"),
        CustomSpinnerItem(R.drawable.pln_flag, "PLN"),
        CustomSpinnerItem(R.drawable.cny_flag, "CNY"),
        CustomSpinnerItem(R.drawable.usd_flag, "USD")
    )

    //list what we will use to calculate
    private val currencyRates = mutableListOf(
        0.0,
        0.0,
        0.0,
        0.0
    )

    var selectedSpinnerItem = 0

    private lateinit var item: CustomSpinnerItem
    var isRightMainSpinner = true

    fun addToList(item: CustomSpinnerItem) {
        this.item = item
    }

    fun provideList(isMain: Boolean) : List<CustomSpinnerItem> {

        return if (isMain) {
            list
        } else {
            listOf(item)
        }
    }

    fun calculate(number: Double) {
        //if we convert to
        if (isRightMainSpinner) {
            val result = number * currencyRates[selectedSpinnerItem]
            _result.postValue("$number ${item.name} = ${roundCurrency(result)} ${list[selectedSpinnerItem].name}")
        } else {
            //if we convert from
            if (currencyRates[selectedSpinnerItem] == 0.0) {
                return
            }
            val result = number / currencyRates[selectedSpinnerItem]
            _result.postValue("$number ${list[selectedSpinnerItem].name} = ${roundCurrency(result)} ${item.name}")
        }
    }

    //preparing the currency
    fun getCurrency(date: String, cur: Double) {
        for (i in popularCurrency) {
            if (i.date == date) {
                when(i.name.uppercase()) {
                    list[0].name -> currencyRates[0] = i.amount / cur
                    list[1].name -> currencyRates[1] = i.amount / cur
                    list[2].name -> currencyRates[2] = i.amount / cur
                    list[3].name -> currencyRates[3] = i.amount / cur
                }
            }
        }
    }
}