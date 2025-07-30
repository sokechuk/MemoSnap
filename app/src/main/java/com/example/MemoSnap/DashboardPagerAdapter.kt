package com.example.MemoSnap.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
//import com.example.MemoSnap.ui.RecentFragment
import com.example.MemoSnap.ui.MyPhotosFragment
import com.example.MemoSnap.ui.MyGroupsFragment
import com.example.MemoSnap.RecentFragment
class DashboardPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RecentFragment()
            1 -> MyPhotosFragment()
            2 -> MyGroupsFragment()
            else -> throw IllegalArgumentException("Invalid position $position")
        }
    }
}