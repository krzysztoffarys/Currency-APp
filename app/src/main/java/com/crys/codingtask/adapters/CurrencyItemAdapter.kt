package com.crys.codingtask.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crys.codingtask.R
import com.crys.codingtask.databinding.ItemCurrencyBinding
import com.crys.codingtask.databinding.ItemDateBinding
import com.crys.codingtask.model.CurrencyRecyclerViewHolder
import com.crys.codingtask.model.CurrencyRecyclerViewItem

class CurrencyItemAdapter(val context: Context) : RecyclerView.Adapter<CurrencyRecyclerViewHolder>() {

    private var onItemClickListener: ((CurrencyRecyclerViewItem.Currency) -> Unit)? = null


    var items = listOf<CurrencyRecyclerViewItem>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyRecyclerViewHolder {
        return when(viewType) {
            R.layout.item_currency -> CurrencyRecyclerViewHolder.CurrencyViewHolder(
                ItemCurrencyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                context,
                onItemClickListener
            )
            R.layout.item_date -> CurrencyRecyclerViewHolder.DateViewHolder(
                ItemDateBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Invalid ViewType Provided")
        }
    }

    override fun onBindViewHolder(holder: CurrencyRecyclerViewHolder, position: Int) {
        when (holder){
            is CurrencyRecyclerViewHolder.CurrencyViewHolder -> holder.bind(items[position] as CurrencyRecyclerViewItem.Currency)
            is CurrencyRecyclerViewHolder.DateViewHolder -> holder.bind(items[position] as CurrencyRecyclerViewItem.Date)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when(items[position]){
            is CurrencyRecyclerViewItem.Currency -> R.layout.item_currency
            is CurrencyRecyclerViewItem.Date -> R.layout.item_date
        }
    }

    fun setOnItemClickListener(onItemClick: (CurrencyRecyclerViewItem.Currency) -> Unit) {
        this.onItemClickListener = onItemClick
    }
}