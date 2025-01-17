package com.telkom21.anaksehat.admin

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.telkom21.anaksehat.R
import com.telkom21.anaksehat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPager: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPager = binding.fragmentContainer
        viewPager.adapter = ViewPagerAdapter(this)

        val botNav = binding.bottomNav
        botNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home_nav -> viewPager.currentItem = 0
                R.id.weight_nav -> viewPager.currentItem = 1
                R.id.profil_nav -> viewPager.currentItem = 2
            }
            true
        }

        // Sync ViewPager with Bottom Navigation
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> botNav.selectedItemId = R.id.home_nav
                    1 -> botNav.selectedItemId = R.id.weight_nav
                    2 -> botNav.selectedItemId = R.id.profil_nav
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
            homeFragment(),
            timbangFragment(),
            profilFragment()
        )

        override fun getItemCount(): Int = fragmentList.size
        override fun createFragment(position: Int): Fragment = fragmentList[position]
    }

}
