package com.crys.codingtask.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.crys.codingtask.MainCoroutineRule
import com.crys.codingtask.getOrAwaitValueTest
import com.crys.codingtask.model.CurrencyRecyclerViewItem
import com.crys.codingtask.model.CustomSpinnerItem
import com.crys.codingtask.util.Converter
import com.crys.codingtask.util.Converter.roundCurrency
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@kotlinx.coroutines.ExperimentalCoroutinesApi
class DetailViewModelTest {

    private lateinit var viewModel: DetailViewModel
    private val number = 0.24131
    private val date = "2021-11-22"

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        Converter.popularCurrency.add(CurrencyRecyclerViewItem.Currency("cNY", 7.197715, date))
        Converter.popularCurrency.add(CurrencyRecyclerViewItem.Currency("eUR", 1.0, date))
        Converter.popularCurrency.add(CurrencyRecyclerViewItem.Currency("pLN", 4.715834, date))
        Converter.popularCurrency.add(CurrencyRecyclerViewItem.Currency("uSD", 1.128292, date))
        viewModel = DetailViewModel()
        viewModel.getCurrency(date, number)
        viewModel.addToList(CustomSpinnerItem(0, "AED"))
    }

    @Test
    fun `test calculate from currency`() {
        val amount = 3.0
        viewModel.calculate(amount)
        val result = viewModel.result.getOrAwaitValueTest()
        assertThat(result).isEqualTo("$amount AED = ${roundCurrency(1 / number * amount)} EUR")
    }

    @Test
    fun `test calculate to currency`() {
        val amount = 3.0
        viewModel.isRightMainSpinner = false
        viewModel.calculate(amount)
        val result = viewModel.result.getOrAwaitValueTest()
        assertThat(result).isEqualTo("$amount EUR = ${roundCurrency(number * amount)} AED")
    }


}