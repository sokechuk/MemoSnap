package com.example.MemoSnap.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.MemoSnap.R

class MyGroupsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_my_groups, container, false)
    }
}