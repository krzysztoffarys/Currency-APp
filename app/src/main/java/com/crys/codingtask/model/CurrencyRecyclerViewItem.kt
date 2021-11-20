package com.crys.codingtask.model

import java.io.Serializable

sealed class CurrencyRecyclerViewItem {
    data class Currency(
        val name: String,
        val amount: Double,
        val date: String
    ) : Serializable, CurrencyRecyclerViewItem()

    data class Date(
        val date: String
    ) : CurrencyRecyclerViewItem()
}
