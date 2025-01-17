package com.telkom21.anaksehat.utils

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.telkom21.anaksehat.R
import com.telkom21.anaksehat.auth.LoginActivity
import com.telkom21.anaksehat.auth.RegisterActivity
import com.telkom21.anaksehat.databinding.ActivityFrontBinding
import com.telkom21.anaksehat.help.HelpActivity


class FrontActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFrontBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityFrontBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        val tutorialText = binding.tutorialText
        val registBtn = binding.registBtn
        val loginBtn = binding.loginBtn

        registBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        loginBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        tutorialText.setOnClickListener {
            startActivity(Intent(this, HelpActivity::class.java))
        }
    }
}