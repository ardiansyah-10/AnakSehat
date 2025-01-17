package com.telkom21.anaksehat.info

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.telkom21.anaksehat.R
import com.telkom21.anaksehat.databinding.ActivityInfoBinding

class InfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityInfoBinding.inflate(layoutInflater)
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

        binding.imgAlat.setColorFilter(ContextCompat.getColor(this, R.color.main_blue))
        binding.imgPembuat.setColorFilter(ContextCompat.getColor(this, R.color.main_blue))
        infoAlatStart()
        infoAplikasiStart()
        infoPembuatStart()
        setContentView(binding.root)
        binding.backBtn.setOnClickListener {
            finish()
        }

    }
    private fun infoAlatStart(){
        binding.cardAlat.setOnClickListener {
            startActivity(Intent(this, InfoAlatActivity::class.java))
        }
    }
    private fun infoAplikasiStart(){
        binding.cardApp.setOnClickListener {
            startActivity(Intent(this, InfoAplikasiActivity::class.java))
        }
    }
    private fun infoPembuatStart(){
        binding.cardPembuat.setOnClickListener {
            startActivity(Intent(this, InfoPembuatActivity::class.java))
        }
    }
}