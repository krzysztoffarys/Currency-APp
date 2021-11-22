package com.crys.codingtask.ui.rate

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crys.codingtask.R
import com.crys.codingtask.adapters.CurrencyItemAdapter
import com.crys.codingtask.databinding.RateFragmentBinding
import com.crys.codingtask.other.Constants.AMOUNT_ITEM_TO_PAGINATION
import com.crys.codingtask.other.Constants.NEXT_PAGINATION_TRY
import com.crys.codingtask.other.Status
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RateFragment : Fragment(R.layout.rate_fragment) {

    private lateinit var binding: RateFragmentBinding
    private val viewModel: RateViewModel by viewModels()
    private lateinit var currencyAdapter: CurrencyItemAdapter




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RateFragmentBinding.bind(view)
        //if we come back from detail_fragment it should not load again
        //it should get the latest response, only if was some error or it is the first try
        if (!viewModel.wasAnySuccessfulResponse) {
            viewModel.getLatestResponse()
        }
        subscribeToObservers()
        setupRecyclerView()
        //navigate to detail fragment with anim
        currencyAdapter.setOnItemClickListener {
            val navOptions =  NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_right)
                .setExitAnim(R.anim.slide_out_left)
                .setPopEnterAnim(R.anim.slide_in_left)
                .setPopExitAnim(R.anim.slide_out_right)
                .build()
            findNavController().navigate(
                RateFragmentDirections.actionRateFragmentToDetailFragment(it),
                navOptions
            )
        }
        //the button is only visible, if it was no successful response, then we can manually try to load again
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
            //we use the event class to prevent the same message from being displayed twice when turning the screen
            val result = event.peekContent()
            when(result.status) {
                Status.SUCCESS -> {
                    //if is the first successful loading the animation needs to start manually
                    if (!viewModel.wasAnySuccessfulResponse) {
                        binding.rv.startLayoutAnimation()
                    }
                    viewModel.isLoading = false
                    binding.progressBar.visibility = View.GONE
                    binding.btnReload.visibility = View.GONE
                    currencyAdapter.items = result.data!!
                    //we tell the adapter to update ui
                    currencyAdapter.notifyItemRangeChanged(viewModel.itemAmount, result.data.size - 1)
                    viewModel.itemAmount = result.data.size
                    //if we have a successful response
                    viewModel.wasAnySuccessfulResponse = true
                }
                Status.ERROR -> {
                    //if was no successful response
                    if (!viewModel.wasAnySuccessfulResponse) {
                        binding.btnReload.visibility = View.VISIBLE
                    } else {
                        //if is problem with pagination
                        viewLifecycleOwner.lifecycleScope.launch {
                            delay(NEXT_PAGINATION_TRY)
                            viewModel.isLoading = false
                        }

                    }


                    binding.progressBar.visibility = View.GONE
                    //we show the error
                    event.getContentIfNotHandled()?.message?.let { message -> showSnackBar(message) }
                }
                Status.LOADING -> {
                    viewModel.isLoading = true
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
            if (totalItemCount - firstVisibleItemPosition < AMOUNT_ITEM_TO_PAGINATION && !viewModel.isLoading) {
                viewModel.pagination()
            }
        }
    }
}