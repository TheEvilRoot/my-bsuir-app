package com.theevilroot.mybsuir.api

import android.content.Context
import android.view.View

data class ViewGroup (
    val views: Set<View>
) {

    fun visible() =
        views.forEach { it.visibility = View.VISIBLE }

    fun gone() =
        views.forEach { it.visibility = View.GONE }

    fun invisible() =
        views.forEach { it.visibility = View.INVISIBLE }

}

fun View.groupOf(set: Set<Int>): ViewGroup =
    ViewGroup(set.map { findViewById<View>(it) }.toSet())

fun groupOf(vararg views: View) =
    ViewGroup(views.toSet())