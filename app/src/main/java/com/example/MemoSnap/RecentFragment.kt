package com.example.MemoSnap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecentFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_recent_photos, container, false)
        recyclerView = root.findViewById(R.id.recyclerRecentPhotos)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        return root
    }

    override fun onResume() {
        super.onResume()
        loadRecentPhotos()
    }

    private fun loadRecentPhotos() {
        val prefs = requireContext().getSharedPreferences("recent_photos", 0)
        val photoPaths = prefs.getStringSet("recent_list", setOf())?.toList() ?: emptyList()
        recyclerView.adapter = RecentPhotosAdapter(requireContext(), photoPaths)
    }
}