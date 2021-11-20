package com.crys.codingtask.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.crys.codingtask.R
import com.crys.codingtask.adapters.CustomSpinnerAdapter
import com.crys.codingtask.databinding.DetailFragmentBinding
import com.crys.codingtask.model.CustomSpinnerItem

class DetailFragment : Fragment(R.layout.detail_fragment) {

    private lateinit var binding: DetailFragmentBinding
    private val args: DetailFragmentArgs by navArgs()
    private var currencyName = ""
    private var amount = 0.0
    private var date = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DetailFragmentBinding.bind(view)
        setupLayout()
    }

    private fun setupLayout() {
        currencyName = args.currency.name.uppercase()
        amount = args.currency.amount
        date = args.currency.date
        binding.apply {
            tvInfo.text = date
            ivCross.setOnClickListener {
                findNavController().navigate(
                    DetailFragmentDirections.actionDetailFragmentToRateFragment()
                )
            }
        }
        setupSpinner()
    }
    private fun setupSpinner() {

        val list = mutableListOf(
            CustomSpinnerItem(R.drawable.eur_flag, "EUR"),
            CustomSpinnerItem(R.drawable.pln_flag, "PLN"),
            CustomSpinnerItem(R.drawable.cny_flag, "CNY"),
            CustomSpinnerItem(R.drawable.usd_flag, "USD")
        )

        if (currencyName == "EUR" || currencyName == "PLN" || currencyName == "CNY" || currencyName == "USD") {
            list.add(CustomSpinnerItem(R.drawable.gbp_flag, "GBP"),)
        } else {
            val placeholder = requireContext().resources.getIdentifier("placeholder_flag",
                "drawable", requireContext().packageName)
            try {
                val image = requireContext().resources.getIdentifier("${currencyName.lowercase()}_flag",
                    "drawable", requireContext().packageName)
                if (image != 0) {
                    list.add(CustomSpinnerItem(image, currencyName))
                } else {
                    list.add(CustomSpinnerItem(placeholder, currencyName))
                }
            } catch (e: Exception) {
                list.add(CustomSpinnerItem(placeholder, currencyName))
            }
        }

        binding.spinnerLeft.apply {
            val customAdapter = CustomSpinnerAdapter(requireContext(), list)
            adapter = customAdapter

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem = parent?.getItemAtPosition(position) as CustomSpinnerItem
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //
                }
            }

        }

        binding.spinnerRight.apply {
            val customAdapter = CustomSpinnerAdapter(requireContext(), list)
            adapter = customAdapter

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem = parent?.getItemAtPosition(position) as CustomSpinnerItem
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //
                }
            }

        }
    }


}