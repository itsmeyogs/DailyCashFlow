package com.yihs.dailycashflow.utils

import com.yihs.dailycashflow.R

enum class RangeDateFilter(val filterId: Int ) {
    DAILY(R.string.range_date_filter_daily),
    WEEKLY(R.string.range_date_filter_weekly),
    MONTHLY(R.string.range_date_filter_monthly)
}