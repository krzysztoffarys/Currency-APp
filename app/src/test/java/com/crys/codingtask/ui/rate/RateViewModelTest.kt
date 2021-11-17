package com.crys.codingtask.ui.rate

import com.crys.codingtask.repositories.FakeRepository
import org.junit.Before
import org.junit.Test

class RateViewModelTest {

    private lateinit var viewModel: RateViewModel

    @Before
    fun setup() {
        viewModel = RateViewModel(FakeRepository())
    }


}