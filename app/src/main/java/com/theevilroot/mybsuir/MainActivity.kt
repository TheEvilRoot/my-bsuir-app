package com.theevilroot.mybsuir

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.a_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_main)
        setSupportActionBar(toolbar)

        val controller = findNavController(R.id.navigation_host)
        val topLevelFragments = setOf(
            R.id.dialog_login, R.id.fragment_profile
        )
        val config = AppBarConfiguration(topLevelFragments)
        NavigationUI.setupActionBarWithNavController(this, controller, config)

        controller.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.dialog_login) {
                toolbar.visibility = View.GONE
                bottom_navigation.visibility = View.GONE
            } else {
                toolbar.visibility = View.VISIBLE
                bottom_navigation.visibility = View.VISIBLE
            }
        }
    }
}