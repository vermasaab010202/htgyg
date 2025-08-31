package com.dime.android.model

data class SuggestedCategory(
    val name: String,
    val emoji: String
) {
    companion object {
        val expenses = listOf(
            SuggestedCategory("Food", "🍔"),
            SuggestedCategory("Transport", "🚆"),
            SuggestedCategory("Rent", "🏠"),
            SuggestedCategory("Subscriptions", "🔄"),
            SuggestedCategory("Groceries", "🛒"),
            SuggestedCategory("Family", "👨‍👩‍👦"),
            SuggestedCategory("Utilities", "💡"),
            SuggestedCategory("Fashion", "👔"),
            SuggestedCategory("Healthcare", "🚑"),
            SuggestedCategory("Pets", "🐕"),
            SuggestedCategory("Sneakers", "👟"),
            SuggestedCategory("Gifts", "🎁")
        )

        val incomes = listOf(
            SuggestedCategory("Paycheck", "💰"),
            SuggestedCategory("Allowance", "🤑"),
            SuggestedCategory("Part-Time", "💼"),
            SuggestedCategory("Investments", "💹"),
            SuggestedCategory("Gifts", "🧧"),
            SuggestedCategory("Tips", "🪙")
        )
    }
}
