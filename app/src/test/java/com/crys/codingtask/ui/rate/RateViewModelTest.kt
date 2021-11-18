package com.crys.codingtask.ui.rate

import com.crys.codingtask.getOrAwaitValueTest
import com.crys.codingtask.other.Status
import com.crys.codingtask.repositories.FakeRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class RateViewModelTest {

    private lateinit var viewModel: RateViewModel
    private lateinit var fakeRepository: FakeRepository

    @Before
    fun setup() {
        fakeRepository = FakeRepository()
        viewModel = RateViewModel(fakeRepository)
    }

}