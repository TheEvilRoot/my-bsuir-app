package com.theevilroot.mybsuir

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.a_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_main)
        setSupportActionBar(toolbar)

        val controller = findNavController(R.id.navigation_host)
        NavigationUI.setupActionBarWithNavController(this, controller)
    }
}