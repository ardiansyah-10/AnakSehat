package com.telkom21.anaksehat.user

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.telkom21.anaksehat.R
import com.telkom21.anaksehat.databinding.ActivityUserMainBinding

class UserMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserMainBinding
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUserMainBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewPager = binding.userFragmentContainer
        viewPager.adapter = ViewPagerAdapter(this)

        val botNav = binding.userBotNav
        botNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.user_home_nav -> viewPager.currentItem = 0
                R.id.user_profil_nav -> viewPager.currentItem = 1
            }
            true
        }

        // Sync ViewPager with Bottom Navigation
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> botNav.selectedItemId = R.id.user_home_nav
                    1 -> botNav.selectedItemId = R.id.user_profil_nav
                }
            }
        })

        onBackPressedDispatcher.addCallback(this) {
            if (viewPager.currentItem != 0) {
                viewPager.currentItem = 0 // Navigate to home fragment
            } else {
                finish() // Close activity
            }
        }
    }

    private inner class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        private val fragmentList = listOf(
            UserHomeFragment(),
            UserProfileFragment()
        )

        override fun getItemCount(): Int = fragmentList.size
        override fun createFragment(position: Int): Fragment = fragmentList[position]
    }
}