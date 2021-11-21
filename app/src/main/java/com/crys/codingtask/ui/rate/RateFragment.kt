package com.crys.codingtask.ui.rate

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crys.codingtask.R
import com.crys.codingtask.adapters.CurrencyItemAdapter
import com.crys.codingtask.databinding.RateFragmentBinding
import com.crys.codingtask.other.Constants.AMOUNT_ITEM_TO_PAGINATION
import com.crys.codingtask.other.Status
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RateFragment : Fragment(R.layout.rate_fragment) {

    private lateinit var binding: RateFragmentBinding
    private val viewModel: RateViewModel by viewModels()
    private lateinit var currencyAdapter: CurrencyItemAdapter

    //variables for pagination
    private var isLoading = false
    private var itemAmount = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RateFragmentBinding.bind(view)
        //if we come back from detail_fragment it should not load again
        //it should get the latest response, only if was some error or it is the first try
        if (!viewModel.wasLatestResponse) {
            viewModel.getLatestResponse()
        }
        subscribeToObservers()
        setupRecyclerView()
        currencyAdapter.setOnItemClickListener {
            findNavController().navigate(
                RateFragmentDirections.actionRateFragmentToDetailFragment(it)
            )
        }

        binding.btnReload.apply {
            setOnClickListener {
                if (visibility == View.VISIBLE) {
                    viewModel.getLatestResponse()
                }
            }
        }

    }

    //
    private fun subscribeToObservers() {
        viewModel.result.observe(viewLifecycleOwner, { event ->
            val result = event.peekContent()
            when(result.status) {
                Status.SUCCESS -> {
                    //if is the first successful loading
                    if (!viewModel.wasLatestResponse) {
                        binding.rv.startLayoutAnimation()
                    }
                    isLoading = false
                    binding.progressBar.visibility = View.GONE
                    binding.btnReload.visibility = View.GONE
                    currencyAdapter.items = result.data!!
                    //we tell the adapter to ui update
                    currencyAdapter.notifyItemRangeChanged(itemAmount, result.data.size - 1)
                    itemAmount = result.data.size
                    viewModel.wasLatestResponse = true
                }
                Status.ERROR -> {
                    //if was an error for latest response
                    if (!viewModel.wasLatestResponse) {
                        binding.btnReload.visibility = View.VISIBLE
                    }
                    isLoading = false
                    binding.progressBar.visibility = View.GONE
                    event.getContentIfNotHandled()?.message?.let { message -> showSnackBar(message) }
                }
                Status.LOADING -> {
                    isLoading = true
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnReload.visibility = View.GONE
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
        currencyAdapter = CurrencyItemAdapter(requireContext())
        adapter = currencyAdapter
        layoutManager = LinearLayoutManager(requireContext())
        addOnScrollListener(scrollListener)
    }

    //pagination
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val totalItemCount = layoutManager.itemCount
            if (totalItemCount - firstVisibleItemPosition < AMOUNT_ITEM_TO_PAGINATION && !isLoading) {
                isLoading = true
                viewModel.selectedDateResponse()
            }
        }
    }
}