package com.theevilroot.mybsuir.common

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.theevilroot.mybsuir.R
import kotlinx.android.synthetic.main.a_main.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware, NavController.OnDestinationChangedListener {

    override val kodein: Kodein by kodein()
    private val store: CredentialsStore by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_main)
        initNavigation()
    }

    private fun initNavigation() {
        val controller = findNavController(R.id.navigation_host)
        controller.addOnDestinationChangedListener(this)
        NavigationUI.setupWithNavController(navigation_bar, controller)
    }

    override fun onDestinationChanged(controller: NavController, dest: NavDestination, args: Bundle?) {
        if (dest.id == R.id.fragment_login) {
            navigation_bar.visibility = View.GONE
        } else {
            navigation_bar.visibility = View.VISIBLE
        }
    }

    fun setScheduleBadge(value: Int?) {
        navigation_bar.getOrCreateBadge(R.id.fragment_home_schedule).apply {
            value?.let { number = it } ?: clearNumber()
        }
    }

}