package com.telkom21.anaksehat.info

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.telkom21.anaksehat.databinding.ActivityInfoPembuatBinding

class InfoPembuatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInfoPembuatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityInfoPembuatBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        back()
    }
    private fun back(){
        binding.backBtn.setOnClickListener {
            finish()
        }
    }
}