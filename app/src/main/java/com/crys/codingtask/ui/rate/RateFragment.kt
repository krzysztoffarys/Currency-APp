package com.crys.codingtask.ui.rate

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.crys.codingtask.R
import com.crys.codingtask.databinding.RateFragmentBinding
import com.crys.codingtask.other.Converter.ratesToListOfCurrency
import com.crys.codingtask.other.InternetChecker
import com.crys.codingtask.other.Status
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class RateFragment : Fragment(R.layout.rate_fragment) {

    private lateinit var binding: RateFragmentBinding
    private val viewModel: RateViewModel by viewModels()
    @Inject
    lateinit var internetChecker: InternetChecker
    private lateinit var currencyAdapter: CurrencyItemAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RateFragmentBinding.bind(view)

        subscribeToObservers()
        setupRecyclerView()
        viewModel
    }

    //
    private fun subscribeToObservers() {
        internetChecker.observe(viewLifecycleOwner, {
            viewModel.isInternetConnection = it
        })
        viewModel.latestResponse.observe( viewLifecycleOwner, {
            val result = it.peekContent()
            when(result.status) {
                Status.SUCCESS -> {
                    Timber.d(result.data?.date)
                    val rates = result.data!!.rates
                    val items = ratesToListOfCurrency(rates!!, result.data.date!!)
                    currencyAdapter.currencyList = items

                }
                Status.ERROR -> {
                    it.getContentIfNotHandled()?.message?.let { message -> showSnackBar(message) }
                }
                Status.LOADING -> {
                    //
                }
            }
        })
    }

    private fun showSnackBar(text: String) {
        Snackbar.make(
            binding.root,
            text,
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun setupRecyclerView() = binding.rv.apply {
        currencyAdapter = CurrencyItemAdapter()
        adapter = currencyAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }
}