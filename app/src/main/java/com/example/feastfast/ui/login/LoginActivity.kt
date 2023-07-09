package com.example.feastfast.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.feastfast.R
import com.example.feastfast.databinding.ActivityLoginBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener


class LoginActivity : AppCompatActivity() {

    private var tabLayout: TabLayout? = null
    private var viewPager2: ViewPager2? = null
    private var adapter: LoginAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tabLayout = findViewById(R.id.tabLayout)
        viewPager2 = findViewById(R.id.viewPager)
        tabLayout?.addTab(tabLayout?.newTab()!!.setText("Login"))
        tabLayout?.addTab(tabLayout?.newTab()!!.setText("Register"))
        adapter = LoginAdapter(this)
        viewPager2!!.adapter = adapter
        tabLayout!!.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager2!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        viewPager2!!.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabLayout!!.selectTab(tabLayout!!.getTabAt(position))
            }
        })

    }
}

