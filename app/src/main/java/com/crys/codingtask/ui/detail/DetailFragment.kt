package com.crys.codingtask.ui.detail

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.crys.codingtask.R
import com.crys.codingtask.adapters.CustomSpinnerAdapter
import com.crys.codingtask.databinding.DetailFragmentBinding
import com.crys.codingtask.model.CustomSpinnerItem
import com.crys.codingtask.util.Converter.stringToDate

class DetailFragment : Fragment(R.layout.detail_fragment) {

    private lateinit var binding: DetailFragmentBinding
    private val args: DetailFragmentArgs by navArgs()
    private var currencyName = ""
    private var amount = 0.0
    private var date = ""
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var customAdapterLeft: CustomSpinnerAdapter
    private lateinit var customAdapterRight: CustomSpinnerAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DetailFragmentBinding.bind(view)
        setupLayout()
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        viewModel.result.observe(viewLifecycleOwner, { text ->
            binding.tvResult.text = text
        })
    }


    private fun setupLayout() {
        currencyName = args.currency.name.uppercase()
        amount = args.currency.amount
        date = args.currency.date

        binding.apply {
            tvDate.text = stringToDate(date, requireContext().resources.getStringArray(R.array.months))
            //return back
            ivCross.setOnClickListener {
                findNavController().popBackStack()
            }

            ivChange.setOnClickListener {
                swapSpinner()
            }

            btnConvert.setOnClickListener {
                try {
                    if (etAmount.text.isEmpty()) {
                        val emptyMessage = getString(R.string.empty_field)
                        Toast.makeText(requireContext(), emptyMessage, Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    val number = etAmount.text.toString().toDouble()
                    viewModel.calculate(number)
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                }
            }

        }
        setupSpinner()
        //setup the view model
        viewModel.getCurrency(date, amount)
        viewModel.calculate(1.0)
    }
    private fun setupSpinner() {

        //prepare the spinner item for current currency
        val placeholder = requireContext().resources.getIdentifier("placeholder_flag",
            "drawable", requireContext().packageName)
        try {
            val image = requireContext().resources.getIdentifier("${currencyName.lowercase()}_flag",
                "drawable", requireContext().packageName)
            if (image != 0) {
                viewModel.addToList(CustomSpinnerItem(image, currencyName))
            } else {
                viewModel.addToList(CustomSpinnerItem(placeholder, currencyName))
            }
        } catch (e: Exception) {
            viewModel.addToList(CustomSpinnerItem(placeholder, currencyName))
        }


        binding.spinnerLeft.apply {
            //only one item, current currency on top
            customAdapterLeft = CustomSpinnerAdapter(requireContext(), viewModel.provideList(false))
            adapter = customAdapterLeft

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (!viewModel.isRightMainSpinner) {
                        viewModel.selectedSpinnerItem = position
                        if (binding.etAmount.text.isEmpty()) {
                            viewModel.calculate(1.0)
                        } else {
                            val number = binding.etAmount.text.toString().toDouble()
                            viewModel.calculate(number)
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        }

        binding.spinnerRight.apply {
            //list of popular currencies
            customAdapterRight = CustomSpinnerAdapter(requireContext(), viewModel.provideList(true))
            adapter = customAdapterRight
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (viewModel.isRightMainSpinner) {
                        viewModel.selectedSpinnerItem = position
                        if (binding.etAmount.text.isEmpty()) {
                            viewModel.calculate(1.0)
                        } else {
                            val number = binding.etAmount.text.toString().toDouble()
                            viewModel.calculate(number)
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        }
    }
    private fun swapSpinner() {
        rotate()
        if (viewModel.isRightMainSpinner) {
            binding.spinnerLeft.adapter = customAdapterRight
            binding.spinnerRight.adapter = customAdapterLeft
            binding.spinnerLeft.setSelection(viewModel.selectedSpinnerItem)
            viewModel.isRightMainSpinner = false
        } else {
            binding.spinnerLeft.adapter = customAdapterLeft
            binding.spinnerRight.adapter = customAdapterRight
            binding.spinnerRight.setSelection(viewModel.selectedSpinnerItem)
            viewModel.isRightMainSpinner = true
        }
    }

    private fun rotate() {
        val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate)
        binding.ivChange.startAnimation(anim)
    }

}