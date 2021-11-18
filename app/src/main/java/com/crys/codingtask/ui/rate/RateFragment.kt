package com.crys.codingtask.ui.rate

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.crys.codingtask.R
import com.crys.codingtask.adapters.CurrencyItemAdapter
import com.crys.codingtask.databinding.RateFragmentBinding
import com.crys.codingtask.other.InternetChecker
import com.crys.codingtask.other.Status
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
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
        viewModel.getLatestResponse()
        subscribeToObservers()
        setupRecyclerView()

    }

    //
    private fun subscribeToObservers() {
        viewModel.result.observe(viewLifecycleOwner, { event ->
            val result = event.peekContent()
            when(result.status) {
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    currencyAdapter.currencyList = result.data!!
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    event.getContentIfNotHandled()?.message?.let { message -> showSnackBar(message) }
                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
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