package com.dime.android.util

import java.text.NumberFormat
import java.util.*

object CurrencyFormatter {
    fun format(amount: Double, currencyCode: String = "USD", showCents: Boolean = true): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
        
        try {
            formatter.currency = Currency.getInstance(currencyCode)
        } catch (e: Exception) {
            formatter.currency = Currency.getInstance("USD")
        }
        
        if (!showCents) {
            formatter.maximumFractionDigits = 0
            formatter.minimumFractionDigits = 0
        }
        
        return formatter.format(amount)
    }
    
    fun formatWithSign(amount: Double, isIncome: Boolean, currencyCode: String = "USD", showCents: Boolean = true): String {
        val formattedAmount = format(amount, currencyCode, showCents)
        return if (isIncome) "+$formattedAmount" else "-$formattedAmount"
    }
}
