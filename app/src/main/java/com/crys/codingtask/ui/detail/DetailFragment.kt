package com.crys.codingtask.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.crys.codingtask.R
import com.crys.codingtask.databinding.DetailFragmentBinding

class DetailFragment : Fragment(R.layout.detail_fragment) {

    private lateinit var binding: DetailFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DetailFragmentBinding.bind(view)
    }

}