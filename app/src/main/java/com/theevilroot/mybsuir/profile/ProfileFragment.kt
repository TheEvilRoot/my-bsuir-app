package com.theevilroot.mybsuir.profile

import android.app.Dialog
import android.content.Context
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.edit
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.callbacks.onCancel
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.afollestad.materialdialogs.callbacks.onPreShow
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.LayoutParams.*
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.announcements.AnnouncementsController
import com.theevilroot.mybsuir.announcements.holders.AnnouncementViewHolder
import com.theevilroot.mybsuir.common.SharedModel
import com.theevilroot.mybsuir.common.TextWatchWrapper
import com.theevilroot.mybsuir.common.adapters.AddableAdapter
import com.theevilroot.mybsuir.common.adapters.SimpleAdapter
import com.theevilroot.mybsuir.common.adapters.SimpleRangedAdapter
import com.theevilroot.mybsuir.common.api.views.ModelDataFragment
import com.theevilroot.mybsuir.profile.data.ProfileInfo
import com.theevilroot.mybsuir.common.data.*
import com.theevilroot.mybsuir.common.utils.asVisibility
import com.theevilroot.mybsuir.profile.data.BadgeType
import com.theevilroot.mybsuir.profile.holders.ReferenceViewHolder
import com.theevilroot.mybsuir.profile.holders.SkillSuggestionViewHolder
import com.theevilroot.mybsuir.profile.holders.SkillsViewHolder
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.d_skill_add.view.*
import kotlinx.android.synthetic.main.f_profile.view.*
import kotlinx.android.synthetic.main.i_profile_content.*
import kotlinx.android.synthetic.main.i_profile_content.view.*
import kotlinx.android.synthetic.main.i_profile_header.view.*
import org.kodein.di.generic.instance
import kotlin.math.abs

class ProfileFragment : ModelDataFragment<ProfileFragment.ProfileViewState, ProfileInfo>(R.layout.f_profile) {

    sealed class ProfileViewState {

        abstract val headerContentVisibility: Boolean
        abstract val headerProgressVisibility: Boolean
        abstract val headerErrorVisibility: Boolean
        abstract val headerCanCollapse: Boolean
        abstract val buttonsAvailable: Boolean

        class ProfileFilled(val profileInfo: ProfileInfo): ProfileViewState() {
            override val headerContentVisibility: Boolean = true
            override val headerProgressVisibility: Boolean = false
            override val headerCanCollapse: Boolean = true
            override val headerErrorVisibility: Boolean = false
            override val buttonsAvailable: Boolean = true
        }

        class ProfileError(val message: String, val retryHandler: View.() -> Unit): ProfileViewState() {
            override val headerContentVisibility: Boolean = false
            override val headerProgressVisibility: Boolean = false
            override val headerCanCollapse: Boolean = false
            override val headerErrorVisibility: Boolean = true
            override val buttonsAvailable: Boolean = false
        }

        object ProfileLoading: ProfileViewState() {
            override val headerContentVisibility: Boolean = false
            override val headerProgressVisibility: Boolean = true
            override val headerCanCollapse: Boolean = false
            override val headerErrorVisibility: Boolean = false
            override val buttonsAvailable: Boolean = false
        }
    }

    private val model: SharedModel by instance()

    private val controller by lazy { ProfileController(model) }
    private val announcementsController by lazy { AnnouncementsController(model) }

    private val skillsAdapter by lazy { AddableAdapter(R.layout.i_skill, R.layout.i_skill_add,
            ::SkillsViewHolder, ::SkillsViewHolder, ::onSkillAddClick) }
    private val referencesAdapter by lazy { SimpleAdapter(R.layout.i_reference, ::ReferenceViewHolder) }
    private val skillsSuggestionAdapter by lazy { SimpleRangedAdapter(R.layout.i_skill,
        ::SkillSuggestionViewHolder) }
    private val announcementsAdapter by lazy { SimpleAdapter(R.layout.i_announcement,
            ::AnnouncementViewHolder) }
    private val summaryTextListener = TextWatchWrapper(onTextChanged = ::onSummaryEditChange)

    private var skillAddDialog: Dialog? = null
    private var summarySubmitDialog: Dialog? = null
    private var summaryHelpDialog: Dialog? = null

    override fun View.onView() {
        with(skills_view) {
            adapter = skillsAdapter
            layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false)
        }
        with(references_view) {
            adapter = referencesAdapter
            layoutManager = LinearLayoutManager(context)
        }

        with(announcements_view) {
            adapter = announcementsAdapter
            layoutManager = LinearLayoutManager(context)
        }

        profile_app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBar, offset ->
            if (appBar.totalScrollRange == 0) return@OnOffsetChangedListener
            val value = abs(offset).toFloat() / appBar.totalScrollRange.toFloat()
            profile_title_name.alpha = value
            profile_header_content.referencedIds.forEach { findViewById<View>(it).alpha = 1 - value }
        })

        button_papers.setOnClickListener {
            findNavController().navigate(R.id.fragment_papers)
        }

        button_exam_sheets.setOnClickListener {
            findNavController().navigate(R.id.fragment_sheets)
        }

        profile_logout.setOnClickListener {
            findNavController().navigate(R.id.action_profile_logout,
                    bundleOf("logout" to true))
        }

        info_hint.setOnClickListener {
            showSummaryHelpDialog()
        }

        updateAnnouncements(emptyList())
        updateData(true)
    }

    private fun viewUpdateData(useCurrentCredentials: Boolean, forceUpdate: Boolean) =
            view?.updateData(useCurrentCredentials, forceUpdate)

    private fun getCountUpdate(type: BadgeType, f: () -> Single<Int>) {
        f().observeOn(AndroidSchedulers.mainThread()).subscribe({
            if (it == 0)
                updateBadge(type)
            else updateBadge(type, it)
        }, { updateBadge(type) })
    }

    private fun getAnnouncementsUpdate() {
        announcementsController.updateAnnouncements(false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ view?.updateAnnouncements(it) }, {  })
    }

    override fun onDataUpdated(data: ProfileInfo) {
        getCountUpdate(BadgeType.PAPERS, controller::updatePapersCount)
        getCountUpdate(BadgeType.SHEETS, controller::updateSheetsCount)
        getAnnouncementsUpdate()
    }

    override fun getDataUpdate(forceUpdate: Boolean): Single<ProfileInfo> =
            controller.updateProfileInfo(forceUpdate)

    override fun getLoadingState(): ProfileViewState =
            ProfileViewState.ProfileLoading

    override fun getErrorState(it: Throwable, msg: String, retryAction: View.() -> Unit): ProfileViewState =
            ProfileViewState.ProfileError(msg, retryAction)

    override fun getFilledState(it: ProfileInfo): ProfileViewState =
            ProfileViewState.ProfileFilled(it)

    override fun View.applyState(newState: ProfileViewState) = with(newState) {
        profile_header_content.visibility = headerContentVisibility.asVisibility()
        profile_title_name.visibility = headerContentVisibility.asVisibility()
        profile_progress.visibility = headerProgressVisibility.asVisibility()
        profile_error.visibility = headerErrorVisibility.asVisibility()
        profile_summary.visibility = headerContentVisibility.asVisibility()

        arrayOf(button_papers, button_exam_sheets, button_settings, info_edit, reference_add)
            .forEach { it.isEnabled = buttonsAvailable }

        val params = profile_collapsing_toolbar.layoutParams as AppBarLayout.LayoutParams
        if (headerCanCollapse) {
            params.scrollFlags =
                SCROLL_FLAG_EXIT_UNTIL_COLLAPSED or SCROLL_FLAG_SNAP or SCROLL_FLAG_SCROLL
        } else {
            params.scrollFlags = 0
            profile_app_bar.setExpanded(true, true)
            profile_content_scroll.smoothScrollTo(0, 0)
        }

        // On every state update
        // we'll need to reset info edit state
        // i guess..
        setInfoViewState()

        if (this is ProfileViewState.ProfileFilled) {
            with(profileInfo) {
                profile_header_name.text = fullName
                profile_title_name.text = fullName

                profile_birth_date.text = birthDate
                profile_faculty.text = facultyString
                profile_rate_bar.progress = rate
                profile_rate_bar.max = 10

                Glide.with(context)
                    .load(photoUrl)
                    .into(profile_image)

                skillsAdapter.setData(skills)

                referencesAdapter.setData(references)
                profile_no_references.visibility = references.isEmpty().asVisibility()

                if (summary.isNullOrBlank()) {
                    profile_summary.text?.clear()
                    profile_summary.setHint(R.string.no_summary)
                } else {
                    profile_summary.setText(summary)
                }
                context.storeProfileSummary(summary ?: "")
            }

        }

        if (this is ProfileViewState.ProfileError) {
            profile_error_message.text = message
            profile_refresh.setOnClickListener(retryHandler)
        }

    }

    private fun updateBadge(type: BadgeType, value: Int? = null) = view?.run {
        val view = when (type) {
            BadgeType.PAPERS -> papers_badge
            BadgeType.SHEETS -> sheets_badge
        }
        if (value == null) {
            clearBadge(view)
        } else {
            setBadge(view, value)
        }
    }

    private fun setBadge(badge: TextView, count: Int) {
        badge.visibility = View.VISIBLE
        badge.text = "$count"
    }

    private fun clearBadge(badge: TextView) {
        badge.visibility = View.GONE
    }

    private fun setInfoEditState() {
        profile_summary.inputType =
                InputType.TYPE_TEXT_FLAG_MULTI_LINE or
                        InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
        profile_summary.setTextIsSelectable(true)
        profile_summary.setBackgroundResource(com.google.android.material.R.drawable.abc_edit_text_material)
        profile_summary.addTextChangedListener(summaryTextListener)

        info_edit.setImageResource(R.drawable.ic_round_check_24)
        info_edit.setOnClickListener { onSummarySubmit() }
    }

    private fun setInfoViewState() {
        profile_summary.inputType = InputType.TYPE_NULL
        profile_summary.setTextIsSelectable(false)
        profile_summary.setBackgroundResource(0)
        profile_summary.removeTextChangedListener(summaryTextListener)

        info_edit.setImageResource(R.drawable.ic_round_edit_24)
        info_edit.setOnClickListener { setInfoEditState() }
    }

    private fun onSkillAddClick(v: View) = view?.run {
        showSkillAddDialog()
    }

    private fun onSummaryEditChange(s: CharSequence, a: Int, b: Int, c: Int) {
        context?.getSharedPreferences("profile", Context.MODE_PRIVATE)?.edit {
            putString("edit_summary", "$s")
        }
    }

    private fun onSummarySubmit() = view?.run {
        val text = profile_summary.text?.toString() ?: ""
        val original = context?.getSharedPreferences("profile", Context.MODE_PRIVATE)
                ?.getString("summary", "") ?: ""

        if (text == original)
            return setInfoViewState()

        showSummarySubmitDialog(text, {
            applyState(ProfileViewState.ProfileLoading)
            controller.updateSummary(text)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(::onSummaryUpdated)
                    { onSummaryUpdateFailed(true) }
        }) {
            onSummaryUpdateFailed(false)
        }
    }

    private fun onSummaryUpdated() = view?.run {
        updateData(useCurrentCredentials = true, forceUpdate = true)
    }

    private fun onSummaryUpdateFailed(alert: Boolean) = view?.run {
        if (alert) {
            Toast.makeText(context, "Не удалось обновить информацию.", Toast.LENGTH_LONG).show()
        }
        updateData(useCurrentCredentials = true, forceUpdate = false)
    }

    private fun View.showSkillAddDialog() {
        skillAddDialog?.dismiss()

        val compositeDisposable = CompositeDisposable()
        skillAddDialog = MaterialDialog(context, BottomSheet(LayoutMode.MATCH_PARENT)).apply {
            customView(R.layout.d_skill_add)
            title(R.string.skill_add_title)
            onPreShow { skillsSuggestionAdapter.setData(emptyList()) }
            onDismiss { compositeDisposable.dispose() }
            show {
                getCustomView().initSkillAddDialog(this, compositeDisposable)
            }
        }
    }

    private fun View.initSkillAddDialog(dialog: MaterialDialog, compositeDisposable: CompositeDisposable) {
        skill_add_suggestions.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL, false)
        skill_add_suggestions.adapter = skillsSuggestionAdapter
        skill_add_hint.visibility = View.VISIBLE
        skillsSuggestionAdapter.onItemClick = {
            skill_add_field.setText(it.name) }
        skill_add_submit.isEnabled = false
        skill_add_field.addTextChangedListener {
            val text = it?.toString()
            if (text != null) {
                controller.suggestSkills(text, true)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ data ->
                        skill_add_hint.visibility = View.GONE
                        skillsSuggestionAdapter.setData(data)
                    }) { }.let(compositeDisposable::add)

            }
            skill_add_submit.isEnabled = text != null
        }
        skill_add_submit.setOnClickListener {
            val text = skill_add_field.text?.toString()
                ?: return@setOnClickListener
            skill_add_submit.isEnabled = false
            controller.submitSkill(text)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    dialog.dismiss()
                    this@ProfileFragment.view?.updateData(
                        useCurrentCredentials = true,
                        forceUpdate = true
                    )
                }) {
                    skill_add_hint.visibility = View.VISIBLE
                    skill_add_hint.text = it.localizedMessage
                    skill_add_submit.isEnabled = true
                    skillsSuggestionAdapter.setData(emptyList())
                }
        }
    }

    private fun View.showSummarySubmitDialog(text: String, onSubmit: View.(String) -> Unit, onCancel: View.() -> Unit) {
        summarySubmitDialog?.dismiss()
        summarySubmitDialog = MaterialDialog(context).apply {
            title(R.string.summary_submit_title)
            val displayText = if (text.isBlank()) "<b>&lt;пусто&gt;</b>" else text
            message(text=context.getString(R.string.summary_submit_message, displayText)) { html() }
            cancelable(true)
            positiveButton(R.string.summary_submit) {
                this@showSummarySubmitDialog.onSubmit(text)
            }
            negativeButton(R.string.cancel) {
                this@showSummarySubmitDialog.onCancel()
            }
            onCancel { this@showSummarySubmitDialog.onCancel() }
        }
        summarySubmitDialog?.show()
    }

    private fun View.showSummaryHelpDialog() {
        summaryHelpDialog?.dismiss()

        summaryHelpDialog = MaterialDialog(context).apply {
            title(R.string.info_title)
            message(R.string.info_help) { html() }
            cancelable(true)
            show()
        }
    }

    private fun View.updateAnnouncements(list: List<Announcement>) {
        announcementsAdapter.setData(list)
        announcements_placeholder.visibility = list.isEmpty().asVisibility()
    }

    private fun Context.storeProfileSummary(summary: String) {
        getSharedPreferences("profile", Context.MODE_PRIVATE).edit {
            putString("summary", summary)
        }
    }

}
