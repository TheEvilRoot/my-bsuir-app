package com.theevilroot.mybsuir.profile.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.afollestad.materialdialogs.callbacks.onPreShow
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.common.adapters.SimpleRangedAdapter
import com.theevilroot.mybsuir.common.api.views.BaseDialog
import com.theevilroot.mybsuir.common.controller.CacheController
import com.theevilroot.mybsuir.profile.ProfileController
import com.theevilroot.mybsuir.profile.holders.SkillSuggestionViewHolder
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.d_skill_add.view.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@ExperimentalContracts
class SkillAddDialog (
        private val completionHandler: () -> Unit,
        private val cacheController: CacheController,
        private val controller: ProfileController
) : BaseDialog(R.layout.d_skill_add, R.string.skill_add_title) {

    private val compositeDisposable = CompositeDisposable()
    private val skillsSuggestionAdapter by lazy { SimpleRangedAdapter(R.layout.i_skill,
            ::SkillSuggestionViewHolder) }

    private fun View.handleSuggestions(text: String?) {
        if (text != null && compositeDisposable.size() == 0) {
            cacheController.preloadCacheAndCall(controller.suggestSkills(text, true))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ data ->
                        skill_add_hint.visibility = View.GONE
                        skillsSuggestionAdapter.setData(data)
                        compositeDisposable.clear()
                    }) { compositeDisposable.clear() }
                    .let(compositeDisposable::add)

        }
        skill_add_submit.isEnabled = text != null
    }

    private fun checkInput(text: String?): Boolean {
        contract {
            returns (true) implies (text != null)
        }
        return text != null
    }

    private fun View.updateSuggestions(text: String) {
        cacheController.preloadCacheAndCall(controller.submitSkill(text))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    dialog?.dismiss()
                    completionHandler()
                }) {
                    skill_add_hint.visibility = View.VISIBLE
                    skill_add_hint.text = it.localizedMessage
                    skill_add_submit.isEnabled = true
                    skillsSuggestionAdapter.setData(emptyList())
                }
    }

    override fun View.initView(dialog: Dialog, bundle: Bundle) {
        skill_add_suggestions.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false)
        skill_add_suggestions.adapter = skillsSuggestionAdapter

        skill_add_hint.visibility = View.VISIBLE

        skillsSuggestionAdapter.onItemClick = { skill_add_field.setText(it.name) }

        skill_add_submit.isEnabled = false
        skill_add_field.addTextChangedListener { handleSuggestions(it?.toString()) }
        skill_add_submit.setOnClickListener {
            val text = skill_add_field.text?.toString()
            if (!checkInput(text))
                return@setOnClickListener
            skill_add_submit.isEnabled = false
            updateSuggestions(text)
        }
    }

    override fun onCancel() {
        super.onCancel()
        compositeDisposable.dispose()
    }

    override fun newDialog(context: Context, bundle: Bundle): MaterialDialog {
        return MaterialDialog(context, BottomSheet(LayoutMode.MATCH_PARENT))
                .onPreShow { skillsSuggestionAdapter.setData(emptyList()) }
                .onDismiss { compositeDisposable.dispose() }
    }

}