package com.example.appfinanceiro.utils

import android.content.Context
import androidx.fragment.app.FragmentActivity

fun Context.findFragmentActivity(): FragmentActivity? {
    return this as? FragmentActivity
}