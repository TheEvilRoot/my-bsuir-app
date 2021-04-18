package com.theevilroot.mybsuir.profile.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.common.TextWatchWrapper
import com.theevilroot.mybsuir.common.api.views.BaseDialog
import com.theevilroot.mybsuir.common.controller.CacheController
import com.theevilroot.mybsuir.common.data.Reference
import com.theevilroot.mybsuir.profile.ProfileController
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.d_reference_add.view.*
import java.net.URL
import kotlin.contracts.contract

import kotlin.contracts.ExperimentalContracts

@ExperimentalContracts
class ReferenceAddDialog (
    val cacheController: CacheController,
    val controller: ProfileController,
    val updateHandler: (newList: List<Reference>) -> Unit,
    val failureHandler: (String) -> Unit
) : BaseDialog(R.layout.d_reference_add, R.string.reference_add_title) {

    private var namePresent: Boolean = false
    private var linkPresent: Boolean = false

    override fun View.initView(dialog: Dialog, bundle: Bundle) {
        val currentList = bundle.getParcelableArray("list")
                ?.mapNotNull { it as? Reference } ?: emptyList()

        ref_add_name.text?.clear()
        ref_add_link.text?.clear()
        ref_add_submit.isEnabled = false
        ref_add_name.addTextChangedListener(TextWatchWrapper(onTextChanged = { s, _, _, _ ->
            namePresent = s.isNotBlank()
            ref_add_submit.isEnabled = namePresent && linkPresent
        }))
        ref_add_link.addTextChangedListener(TextWatchWrapper(onTextChanged = { s, _, _, _ ->
            linkPresent = s.isNotBlank()
            ref_add_submit.isEnabled = namePresent && linkPresent
        }))
        ref_add_submit.setOnClickListener {
            val title = ref_add_name.text?.toString()
            val link = ref_add_link.text?.toString()

            // Hack to make contract work in this case.
            when (val result = checkInput(title, link)) {
                null -> {
                    onResult(currentList, mapResult(title, link))
                    dialog.dismiss()
                }
                else -> ref_add_message.text = result
            }
        }
    }

    override fun onCancel() {
        dialog = null
        namePresent = false
        linkPresent = false
    }

    override fun newDialog(context: Context, bundle: Bundle): MaterialDialog {
        return MaterialDialog(context, BottomSheet())
    }

    private fun onResult(list: List<Reference>, result: Reference) {
        val newList = list + result
        cacheController.preloadCacheAndCall(controller.setReferences(newList))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(updateHandler, { failureHandler(it.localizedMessage
                        ?: "Невозможно обновить ссылки из-за непредвиденной ошибки") })
    }

    /**
     * @return error message if data is not valid. null otherwise.
     */
    private fun checkInput(titleIn: String?, linkIn: String?): String? {
        contract {
            returns(null) implies (titleIn != null)
            returns(null) implies (linkIn != null)
        }

        val title = titleIn ?: return "Пожалуйста, введите название для ссылки"
        val link = linkIn ?: return "Пожалуйста, введите ссылку"

        if (runCatching { URL(link) }.isFailure)
            return "Введенная ссылка имеет неверный формат или не является ссылкой."

        if (title.isBlank())
            return "Название ссылки не может быть пустым или содержать только пробелы"
        return null
    }

    private fun mapResult(title: String, link: String): Reference {
        return Reference(null, title, link)
    }

}