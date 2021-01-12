package com.theevilroot.mybsuir.profile

import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.LayoutParams.*
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.common.SharedModel
import com.theevilroot.mybsuir.common.adapters.SimpleAdapter
import com.theevilroot.mybsuir.common.api.views.ModelDataFragment
import com.theevilroot.mybsuir.profile.data.ProfileInfo
import com.theevilroot.mybsuir.common.data.*
import com.theevilroot.mybsuir.common.asVisibility
import com.theevilroot.mybsuir.profile.holders.ReferenceViewHolder
import com.theevilroot.mybsuir.profile.holders.SkillsViewHolder
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
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

    private val model: SharedModel by instance(tag="shared")

    private val controller by lazy { ProfileController(model) }

    private val skillsAdapter by lazy { SimpleAdapter(R.layout.i_skill, ::SkillsViewHolder) }
    private val referencesAdapter by lazy { SimpleAdapter(R.layout.i_reference, ::ReferenceViewHolder) }

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

        profile_app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBar, offset ->
            if (appBar.totalScrollRange == 0) return@OnOffsetChangedListener
            val value = abs(offset).toFloat() / appBar.totalScrollRange.toFloat()
            profile_title_name.alpha = value
            profile_header_content.referencedIds.forEach { findViewById<View>(it).alpha = 1 - value }
        })

        button_papers.setOnClickListener {
            findNavController().navigate(R.id.fragment_papers)
        }

        updateData(true)
    }

    override fun onDataUpdated(data: ProfileInfo) {
        controller.updatePapersCount().observeOn(AndroidSchedulers.mainThread()).subscribe({
            if (it == 0)
                clearPapersBadge()
            else setPapersBadge(it)
        }, {
            it.printStackTrace()
            clearPapersBadge()
        })
    }

    override fun getDataUpdate(): Single<ProfileInfo> =
            controller.updateProfileInfo(false)

    override fun getLoadingState(): ProfileViewState =
            ProfileViewState.ProfileLoading

    override fun getErrorState(msg: String, retryAction: View.() -> Unit): ProfileViewState =
            ProfileViewState.ProfileError(msg, retryAction)

    override fun getFilledState(it: ProfileInfo): ProfileViewState =
            ProfileViewState.ProfileFilled(it)

    override fun View.applyState(newState: ProfileViewState) = with(newState) {
        profile_header_content.visibility = headerContentVisibility.asVisibility()
        profile_title_name.visibility = headerContentVisibility.asVisibility()
        profile_progress.visibility = headerProgressVisibility.asVisibility()
        profile_error.visibility = headerErrorVisibility.asVisibility()

        arrayOf(button_papers, button_exam_sheets, button_settings)
            .forEach { it.isEnabled = buttonsAvailable }

        val params = profile_collapsing_toolbar.layoutParams as AppBarLayout.LayoutParams
        if (headerCanCollapse) {
            params.scrollFlags =
                SCROLL_FLAG_EXIT_UNTIL_COLLAPSED or SCROLL_FLAG_SNAP or SCROLL_FLAG_SCROLL
        } else {
            params.scrollFlags = 0
            profile_app_bar.setExpanded(true, true)
        }

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

                profile_summary.text = summary ?: getString(R.string.no_summary)
            }
        }

        if (this is ProfileViewState.ProfileError) {
            profile_error_message.text = message
            profile_refresh.setOnClickListener(retryHandler)
        }
    }

    private fun setPapersBadge(count: Int) {
        papers_badge.visibility = View.VISIBLE
        papers_badge.text = "$count"
    }

    private fun clearPapersBadge() {
        papers_badge.visibility = View.GONE
    }

}
