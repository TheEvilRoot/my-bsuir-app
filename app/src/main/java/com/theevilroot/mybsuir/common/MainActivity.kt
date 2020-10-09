package com.theevilroot.mybsuir.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.theevilroot.mybsuir.R

class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_main)
        initNavigation()
    }

    private fun initNavigation() {
        val controller = findNavController(R.id.navigation_host)
        controller.addOnDestinationChangedListener(this)
    }

    override fun onDestinationChanged(controller: NavController, dest: NavDestination, args: Bundle?) {

    }

}