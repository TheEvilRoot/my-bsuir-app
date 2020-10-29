package com.theevilroot.mybsuir.common

import android.view.View

fun Boolean.visibility() = if (this) View.VISIBLE else View.GONE
