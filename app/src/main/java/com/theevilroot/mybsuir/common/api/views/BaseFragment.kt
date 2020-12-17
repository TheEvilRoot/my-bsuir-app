package com.theevilroot.mybsuir.common.api.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

abstract class BaseFragment<State> (
    @LayoutRes
    val layoutRes: Int
) : Fragment(), KodeinAware {

    override val kodein: Kodein by kodein()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutRes, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.onView()
    }

    open fun View.onView() { }

    protected abstract fun View.applyState(newState: State)
}