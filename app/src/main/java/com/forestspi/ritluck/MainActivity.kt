package com.forestspi.ritluck

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.forestspi.ritluck.databinding.ActivityMainBinding
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat



class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long = 3000 // 3 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT)
    }
}
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager


        viewPager.adapter = ViewPagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            val tabView = LayoutInflater.from(this).inflate(R.layout.custom_tab, null)
            val tabIcon = tabView.findViewById<ImageView>(R.id.tabIcon)
            val tabText = tabView.findViewById<TextView>(R.id.tabText)

            when (position) {
                0 -> {
                    tabIcon.setImageResource(R.drawable.ic_clock)
                    tabText.text = "Clock"
                }
                1 -> {
                    tabIcon.setImageResource(R.drawable.ic_stopwatch)
                    tabText.text = "Stopwatch"
                }
                2 -> {
                    tabIcon.setImageResource(R.drawable.ic_timer)
                    tabText.text = "Timer"
                }
            }
            tab.customView = tabView
        }.attach()

        // Set initial tab colors
        updateTabColors(tabLayout)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    it.customView?.findViewById<ImageView>(R.id.tabIcon)?.setColorFilter(
                        ContextCompat.getColor(this@MainActivity, R.color.unselected)
                    )
                    it.customView?.findViewById<TextView>(R.id.tabText)?.setTextColor(
                        ContextCompat.getColor(this@MainActivity, R.color.unselected)
                    )
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.let {
                    it.customView?.findViewById<ImageView>(R.id.tabIcon)?.setColorFilter(
                        ContextCompat.getColor(this@MainActivity, R.color.white)
                    )
                    it.customView?.findViewById<TextView>(R.id.tabText)?.setTextColor(
                        ContextCompat.getColor(this@MainActivity, R.color.white)
                    )
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Do nothing
            }
        })
    }

    private fun updateTabColors(tabLayout: TabLayout) {
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            if (tab?.isSelected == true) {
                tab.customView?.findViewById<ImageView>(R.id.tabIcon)?.setColorFilter(
                    ContextCompat.getColor(this, R.color.unselected)
                )
                tab.customView?.findViewById<TextView>(R.id.tabText)?.setTextColor(
                    ContextCompat.getColor(this, R.color.unselected)
                )
            } else {
                if (tab != null) {
                    tab.customView?.findViewById<ImageView>(R.id.tabIcon)?.setColorFilter(
                        ContextCompat.getColor(this, R.color.unselected)
                    )
                }
                if (tab != null) {
                    tab.customView?.findViewById<TextView>(R.id.tabText)?.setTextColor(
                        ContextCompat.getColor(this, R.color.unselected)
                    )
                }
            }
        }

        // Set color for the initially selected tab
        val selectedTab = tabLayout.getTabAt(tabLayout.selectedTabPosition)
        selectedTab?.let {
            it.customView?.findViewById<ImageView>(R.id.tabIcon)?.setColorFilter(
                ContextCompat.getColor(this, R.color.white)
            )
            it.customView?.findViewById<TextView>(R.id.tabText)?.setTextColor(
                ContextCompat.getColor(this, R.color.white)
            )
        }
    }
}
