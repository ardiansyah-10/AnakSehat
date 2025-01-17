package com.telkom21.anaksehat.info

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.telkom21.anaksehat.databinding.ActivityInfoAlatBinding

class InfoAlatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInfoAlatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityInfoAlatBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setContentView(binding.root)
        back()
    }

    private fun back(){
        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}