package com.example.myfirstapp.utils

import java.text.NumberFormat
import java.util.Locale

class PriceFormatter {
    companion object {
        fun formatPrice(price: Double): String {
            val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
            return numberFormat.format(price)
        }
    }
}