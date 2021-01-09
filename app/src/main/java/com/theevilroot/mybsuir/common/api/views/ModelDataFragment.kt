package com.theevilroot.mybsuir.common.api.views

import android.view.View
import androidx.annotation.LayoutRes
import androidx.navigation.findNavController
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.common.controller.CacheController
import com.theevilroot.mybsuir.common.data.InternalException
import com.theevilroot.mybsuir.common.data.NoCredentialsException
import com.theevilroot.mybsuir.common.data.ReAuthRequiredException
import com.theevilroot.mybsuir.profile.ProfileFragment
import com.theevilroot.mybsuir.profile.data.ProfileInfo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import org.kodein.di.generic.instance
import java.net.UnknownHostException

abstract class ModelDataFragment<State, MT>(@LayoutRes layoutRes: Int) : BaseFragment<State>(layoutRes) {

    private val cacheController by instance<CacheController>()

    protected abstract fun getLoadingState(): State
    protected abstract fun getFilledState(it: MT): State
    protected abstract fun getErrorState(msg: String, retryAction: View.() -> Unit): State

    protected abstract fun getDataUpdate(): Single<MT>

    protected fun View.updateData(useCurrentCredentials: Boolean) {
        applyState(getLoadingState())
        cacheController.preloadCacheAndCall(getDataUpdate()
                .observeOn(AndroidSchedulers.mainThread()), useCurrentCredentials)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ dataUpdateHandler(it) }) {
                    dataUpdateErrorHandler(it) }
    }

    private fun View.dataUpdateHandler(it: MT) =
            applyState(getFilledState(it))

    private fun View.dataUpdateErrorHandler(it: Throwable) {
        it.printStackTrace()
        applyState(getErrorState(when (it) {
                    is InternalException ->
                        it.msg
                    is NoCredentialsException ->
                        return findNavController().navigate(R.id.fragment_login)
                    is ReAuthRequiredException ->
                        return updateData(false)
                    is UnknownHostException ->
                        context.getString(R.string.no_internet_error)
                    else -> context.getString(R.string.unexpected_error,
                            it.javaClass.simpleName, it.localizedMessage)
                }) { view?.updateData(true) })
    }

}