package com.crys.codingtask.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.crys.codingtask.model.CustomSpinnerItem
import com.crys.codingtask.databinding.CustomSpinnerItemBinding

class CustomSpinnerAdapter(context: Context, customSpinnerItemList: List<CustomSpinnerItem>)
    : ArrayAdapter<CustomSpinnerItem>(context, 0, customSpinnerItemList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, parent)
    }

    private fun initView(position: Int, parent: ViewGroup): View {

        val curItem = getItem(position)

        val binding = CustomSpinnerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        if (curItem != null) {
            binding.tvName.text = curItem.name
            binding.ivFlag.setImageResource(curItem.image)
        }
        return binding.root
    }
}