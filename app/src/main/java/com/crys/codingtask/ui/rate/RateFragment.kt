package com.crys.codingtask.ui.rate

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.crys.codingtask.R
import com.crys.codingtask.databinding.RateFragmentBinding

class RateFragment : Fragment(R.layout.rate_fragment) {
    private lateinit var binding: RateFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RateFragmentBinding.bind(view)
    }
}