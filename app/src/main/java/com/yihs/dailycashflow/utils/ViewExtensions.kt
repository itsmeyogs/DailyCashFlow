package com.yihs.dailycashflow.utils

import android.app.Activity
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.yihs.dailycashflow.R

fun Activity.showSnackBar(
    message: String,
    duration: Int = Snackbar.LENGTH_SHORT,
    actionText: String? = null,
    action: (() -> Unit)? = null
){
    val rootView : View = findViewById(android.R.id.content)
    val snackBar = Snackbar.make(rootView, message, duration)
        .setBackgroundTint(ContextCompat.getColor(this, R.color.black))
        .setTextColor(ContextCompat.getColor(this, R.color.white))


    if (actionText != null && action != null) {
        snackBar.setAction(actionText) { action() }
        snackBar.setActionTextColor(ContextCompat.getColor(this, R.color.white))
    }
    snackBar.show()
}