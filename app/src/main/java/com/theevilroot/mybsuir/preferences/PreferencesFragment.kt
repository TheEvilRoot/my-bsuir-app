package com.theevilroot.mybsuir.preferences

import android.view.View
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.GridLayoutManager
import com.theevilroot.mybsuir.R
import com.theevilroot.mybsuir.common.adapters.SimpleAdapter
import com.theevilroot.mybsuir.common.api.views.ModelDataFragment
import com.theevilroot.mybsuir.preferences.adapters.PreferencesAdapter
import com.theevilroot.mybsuir.preferences.data.PreferenceEntry
import com.theevilroot.mybsuir.preferences.data.PreferenceType
import com.theevilroot.mybsuir.preferences.data.ProfilePreferences
import com.theevilroot.mybsuir.preferences.holders.CheckablePreferenceViewHolder
import com.theevilroot.mybsuir.profile.IProfileModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import kotlinx.android.synthetic.main.f_preferences.view.*
import org.kodein.di.generic.instance

class PreferencesFragment : ModelDataFragment<PreferencesFragment.PreferencesViewState, ProfilePreferences>(R.layout.f_preferences) {

    sealed class PreferencesViewState {
        abstract val loadingVisibility: Boolean
        abstract val errorVisibility: Boolean

        object Loading : PreferencesViewState() {
            override val loadingVisibility: Boolean = true
            override val errorVisibility: Boolean = false
        }

        class Failure(@DrawableRes val image: Int, val message: String, val retry: View.() -> Unit) : PreferencesViewState() {
            override val loadingVisibility: Boolean = false
            override val errorVisibility: Boolean = true

        }

        class Filled (val preferences: ProfilePreferences) : PreferencesViewState() {
            override val loadingVisibility: Boolean = false
            override val errorVisibility: Boolean = false

        }
    }

    private val profileModel by instance<IProfileModel>()
    private val model by instance<IPreferencesModel>()
    private val controller by lazy { PreferencesController(profileModel, model) }

    private val accessPreferencesAdapter by lazy { PreferencesAdapter(::onAccessPreferenceClick) }
    private val clickableType = mutableMapOf<PreferenceType, Boolean>()

    override fun View.onView() {
        with(preferences_access_view) {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = accessPreferencesAdapter
        }

        updateData(true)
    }

    override fun getLoadingState(): PreferencesViewState =
        PreferencesViewState.Loading

    override fun getFilledState(it: ProfilePreferences): PreferencesViewState =
        PreferencesViewState.Filled(it)

    override fun getErrorState(
        it: Throwable,
        msg: String,
        retryAction: View.() -> Unit
    ): PreferencesViewState =
        PreferencesViewState.Failure(getImageForError(it), msg, retryAction)

    override fun getDataUpdate(forceUpdate: Boolean): Single<ProfilePreferences> =
        controller.getProfilePreferences()

    override fun View.applyState(newState: PreferencesViewState) = with(newState) {
        if (this is PreferencesViewState.Filled) {
            accessPreferencesAdapter.setData(preferences.asAccessEntries())
        }
    }

    private fun onAccessPreferenceClick(pref: PreferenceEntry) {
        if (clickableType[pref.type] == false)
            return
        accessPreferencesAdapter.toggleByType(pref.type)
        clickableType[pref.type] = false

        controller.togglePreference(pref.type, !pref.value)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view?.let { Toast.makeText(it.context, "Настройка ${resources.getString(pref.name)} обновлена", Toast.LENGTH_SHORT).show() }
                clickableType.remove(pref.type)
            })
            { e ->
                view?.let { Toast.makeText(it.context, e.localizedMessage ?: "Ошибка изменения настройки", Toast.LENGTH_SHORT).show() }
                clickableType.remove(pref.type)
            }
    }

    private fun ProfilePreferences.asAccessEntries(): List<PreferenceEntry> =
        listOf(
            PreferenceEntry(PreferenceType.RATING, R.string.access_preference_rating, R.drawable.ic_round_visibility_24,
                R.drawable.ic_round_visibility_off_24, showRating, R.string.show_title, R.string.dont_show_title),
            PreferenceEntry(PreferenceType.PUBLISHED, R.string.access_preference_profile, R.drawable.ic_round_visibility_24,
                R.drawable.ic_round_visibility_off_24, published, R.string.show_title, R.string.dont_show_title),
            PreferenceEntry(PreferenceType.SEARCH_JOB, R.string.access_preference_job, R.drawable.ic_round_work_24,
                R.drawable.ic_round_work_off_24, searchJob, R.string.yes_title, R.string.no_title)
        )

}