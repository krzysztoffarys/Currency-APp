package com.crys.codingtask.model

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.crys.codingtask.R
import com.crys.codingtask.databinding.ItemCurrencyBinding
import com.crys.codingtask.databinding.ItemDateBinding
import com.crys.codingtask.other.Converter.roundCurrency
import com.crys.codingtask.other.Converter.stringToDate

sealed class CurrencyRecyclerViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    class CurrencyViewHolder(private val binding: ItemCurrencyBinding, private val context: Context, private var onItemClickListener: ((CurrencyRecyclerViewItem.Currency) -> Unit)?) : CurrencyRecyclerViewHolder(binding){
        fun bind(currency: CurrencyRecyclerViewItem.Currency) {

            binding.apply {

                val name = currency.name.uppercase()
                tvName.text = name
                tvAmount.text = roundCurrency(currency.amount).toString()
                try {
                    val image = context.resources.getIdentifier("${currency.name.lowercase()}_flag", "drawable", context.packageName)
                    //if image found
                    if (image != 0) {
                        ivFlag.setImageResource(image)
                    } else {
                        ivFlag.setImageResource(R.drawable.placeholder_flag)
                    }

                } catch (e: Exception) {
                    ivFlag.setImageResource(R.drawable.placeholder_flag)
                }

                root.setOnClickListener {
                    onItemClickListener?.let { click ->
                        click(currency)
                    }
                }
            }


        }
    }

    class DateViewHolder(private val binding: ItemDateBinding, private val context: Context) : CurrencyRecyclerViewHolder(binding){
        fun bind(date: CurrencyRecyclerViewItem.Date) {
            binding.tvDate.text = stringToDate(date.date, context)
        }
    }

}
