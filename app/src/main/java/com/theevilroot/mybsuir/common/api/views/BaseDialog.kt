package com.theevilroot.mybsuir.common.api.views

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onCancel
import com.afollestad.materialdialogs.callbacks.onShow
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView

abstract class BaseDialog (
    @LayoutRes
    private val customLayout: Int? = null,
    @StringRes
    private val title: Int? = null
) {

    protected var dialog: Dialog? = null

    open fun View.initView(dialog: Dialog, bundle: Bundle) { }

    open fun onCancel() { dialog = null }

    open fun createNew(context: Context, bundle: Bundle = Bundle.EMPTY) {
        dialog?.cancel()

        dialog = newDialog(context, bundle).let { dialog ->
            title?.let { dialog.title(it) }
            customLayout?.let { dialog.customView(it) }
            dialog.onShow {
                if (customLayout != null)
                    it.getCustomView().initView(it, bundle)
            }
            dialog.onCancel { onCancel() }
        }
        dialog?.show()
    }

    protected open fun newDialog(context: Context, bundle: Bundle): MaterialDialog {
        return MaterialDialog(context)
    }

}