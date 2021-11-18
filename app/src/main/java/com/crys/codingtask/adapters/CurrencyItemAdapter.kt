package com.crys.codingtask.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.crys.codingtask.data.model.Currency
import com.crys.codingtask.databinding.ItemCurrencyBinding
import timber.log.Timber

class CurrencyItemAdapter : RecyclerView.Adapter<CurrencyItemAdapter.CurrencyViewHolder>() {
    inner class CurrencyViewHolder(val binding: ItemCurrencyBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean {
            return oldItem.name == newItem.name && oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean {
            return oldItem.amount == newItem.amount && oldItem.date == newItem.date
        }
    }

    private var onItemClickListener: ((Currency) -> Unit)? = null

    private val differ = AsyncListDiffer(this, diffCallback)

    var currencyList: List<Currency>
        get() = differ.currentList
        set(value) = differ.submitList(value)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding = ItemCurrencyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val curCurrency = currencyList[position]
        holder.binding.apply {

            if(curCurrency.name == "date") {
                tvName.text = ""
                tvAmount.text = ""
                tvDate.text = curCurrency.date
            } else {
                tvName.text = curCurrency.name.uppercase()
                tvAmount.text = curCurrency.amount.toString()
                tvDate.text = ""
            }
        }

    }

    override fun getItemCount(): Int {
        return currencyList.size
    }

    fun setOnItemClickListener(onItemClick: (Currency) -> Unit) {
        this.onItemClickListener = onItemClick
    }
}