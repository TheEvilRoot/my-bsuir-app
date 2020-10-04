package com.theevilroot.mybsuir.api

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.theevilroot.mybsuir.App
import com.theevilroot.mybsuir.api.event.Event
import com.theevilroot.mybsuir.api.event.EventBus
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

open class BaseFragment (
    @LayoutRes
    val layoutRes: Int
) : Fragment(), KodeinAware {

    override val kodein: Kodein by kodein()

    private val systemEventBus: EventBus by instance("System")

    private var sysBusDisposable: Disposable? = null

    private fun debug(message: String) = Log.d(this.javaClass.simpleName, message)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        debug("OnCreateView")
        return inflater.inflate(layoutRes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        debug("onViewCreated")
        view.onView()
    }

    protected open fun View.onView() { }

    protected open fun onSystemEvent(event: Event) { }

    open fun isSystemBusAllowed(): Boolean = true

    protected fun subscribeOnSysBus() {
        if (sysBusDisposable == null && isSystemBusAllowed()) {
            sysBusDisposable = systemEventBus.observable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::onSystemEvent)
        }
    }

    protected fun unsubscribeOnSysBus() {
        sysBusDisposable?.dispose()
        sysBusDisposable = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        debug("onAttach")
    }

    override fun onDestroy() {
        super.onDestroy()
        debug("onDestroy")
        unsubscribeOnSysBus()
    }

    override fun onDetach() {
        super.onDetach()
        debug("onDetach")
        unsubscribeOnSysBus()
    }

    override fun onPause() {
        super.onPause()
        debug("onPause")
        unsubscribeOnSysBus()
    }

    override fun onResume() {
        super.onResume()
        debug("onResume")
        subscribeOnSysBus()
    }

    override fun onStart() {
        super.onStart()
        debug("onStart")
        subscribeOnSysBus()
    }

    override fun onStop() {
        super.onStop()
        debug("onStop")
        unsubscribeOnSysBus()
    }
}

