package com.dime.android.model

enum class FilterType {
    ALL,
    CATEGORY,
    DATE,
    WEEK,
    MONTH,
    INCOME,
    EXPENSE
}

enum class TimeFrame {
    TODAY,
    THIS_WEEK,
    THIS_MONTH,
    THIS_YEAR
}

enum class BudgetTimeFrame {
    DAILY,
    WEEKLY,
    MONTHLY
}

enum class ChartTimeFrame {
    WEEK,
    MONTH,
    YEAR
}
