package com.crys.codingtask.ui.rate

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.crys.codingtask.MainCoroutineRule
import com.crys.codingtask.getOrAwaitValueTest
import com.crys.codingtask.other.Status
import com.crys.codingtask.repositoreis.FakeRepository
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@kotlinx.coroutines.ExperimentalCoroutinesApi
class RateViewModelTest {

    private lateinit var viewModel: RateViewModel
    private lateinit var fakeRepository: FakeRepository

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        fakeRepository = FakeRepository()
        viewModel = RateViewModel(fakeRepository)
    }


    //testing day before
    @Test
    fun `first day of month returns valid date`() {
        val date = "2021-11-01"
        val result = viewModel.dayBefore(date)
        assertThat(result).isEqualTo("2021-10-31")
    }

    @Test
    fun `first day of year returns valid date`() {
        val date = "2021-01-01"
        val result = viewModel.dayBefore(date)
        assertThat(result).isEqualTo("2020-12-31")
    }


    @Test
    fun successfulLatestResponse() {
        fakeRepository.setShouldReturnNetworkError(false)
        viewModel.getLatestResponse()
        val resultStatus = viewModel.result.getOrAwaitValueTest().peekContent().status
        assertThat(resultStatus).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun errorOnLatestResponse() {
        fakeRepository.setShouldReturnNetworkError(true)
        viewModel.getLatestResponse()
        val resultStatus = viewModel.result.getOrAwaitValueTest().peekContent().status
        assertThat(resultStatus).isEqualTo(Status.ERROR)
    }

    @Test
    fun errorOnSelectedResponse() {
        fakeRepository.setShouldReturnNetworkError(false)
        viewModel.getLatestResponse()
        viewModel.result.getOrAwaitValueTest()
        val latestSize = viewModel.currencyList.size
        //
        fakeRepository.setShouldReturnNetworkError(true)
        viewModel.pagination()
        viewModel.result.getOrAwaitValueTest()
        assertThat(viewModel.currencyList.size).isEqualTo(latestSize)
    }

    @Test
    fun successfulSelectedResponse() {
        //on success response, it should paginate
        fakeRepository.setShouldReturnNetworkError(false)
        viewModel.getLatestResponse()
        viewModel.result.getOrAwaitValueTest()
        val latestSize = viewModel.currencyList.size
        //
        fakeRepository.setShouldReturnNetworkError(false)
        viewModel.pagination()
        viewModel.result.getOrAwaitValueTest()
        assertThat(viewModel.currencyList.size).isNotEqualTo(latestSize)
    }


}