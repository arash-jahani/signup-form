package com.arashjahani.sign_up_form.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arashjahani.sig_up_form.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}