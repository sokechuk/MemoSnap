package com.example.MemoSnap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.MemoSnap.adapters.DashboardPagerAdapter

class DashboardActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)

        val adapter = DashboardPagerAdapter(this)
        viewPager.adapter = adapter
        tabLayout.addTab(tabLayout.newTab().setText("Recent / Most Viewed"))
        tabLayout.addTab(tabLayout.newTab().setText("My Photos"))
        tabLayout.addTab(tabLayout.newTab().setText("My Groups"))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }
}