package com.example.MemoSnap.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.MemoSnap.databinding.FragmentMyGroupsBinding

class MyGroupsFragment : Fragment() {
    private var _binding: FragmentMyGroupsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyGroupsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateGroupListDisplay()

        binding.btnCreateGroup.setOnClickListener {
            showCreateGroupDialog()
        }

        binding.btnInviteFriend.setOnClickListener {
            showInviteDialog()
        }
    }

    private fun updateGroupListDisplay() {
        val groupContainer = binding.groupListLayout
        groupContainer.removeAllViews()

        val groups = getSavedGroups()
        if (groups.isEmpty()) {
            val emptyView = TextView(requireContext()).apply {
                text = "No groups created yet."
                textSize = 16f
            }
            groupContainer.addView(emptyView)
            return
        }

        for (group in groups) {
            val groupView = TextView(requireContext()).apply {
                text = "• $group"
                textSize = 18f
                setPadding(8, 12, 8, 12)
                setOnClickListener {
                    Toast.makeText(requireContext(), "Enter group: $group", Toast.LENGTH_SHORT).show()
                    // TODO: 跳转到群组详情页面
                }
            }
            groupContainer.addView(groupView)
        }
    }

    private fun showCreateGroupDialog() {
        val existingGroups = getSavedGroups().toList()

        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 20, 40, 10)
        }

        val groupNameInput = EditText(requireContext()).apply {
            hint = "Enter new group name"
        }
        layout.addView(groupNameInput)

        val groupListView = TextView(requireContext()).apply {
            text = if (existingGroups.isNotEmpty())
                "Existing Groups:\n${existingGroups.joinToString("\n")}"
            else
                "No groups created yet."
            setPadding(0, 20, 0, 0)
        }
        layout.addView(groupListView)

        AlertDialog.Builder(requireContext())
            .setTitle("Create New Group")
            .setView(layout)
            .setPositiveButton("Create") { _, _ ->
                val newGroup = groupNameInput.text.toString().trim()
                if (newGroup.isNotEmpty()) {
                    saveGroup(newGroup)
                    Toast.makeText(requireContext(), "Group '$newGroup' created", Toast.LENGTH_SHORT).show()
                    updateGroupListDisplay()
                } else {
                    Toast.makeText(requireContext(), "Group name cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showInviteDialog() {
        val groups = getSavedGroups().toList()

        if (groups.isEmpty()) {
            Toast.makeText(requireContext(), "Please create a group first.", Toast.LENGTH_SHORT).show()
            return
        }

        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 20, 40, 10)
        }

        val emailInput = EditText(requireContext()).apply {
            hint = "Friend's Email"
        }

        val spinner = Spinner(requireContext())
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, groups)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        layout.addView(spinner)
        layout.addView(emailInput)

        AlertDialog.Builder(requireContext())
            .setTitle("Invite Friend")
            .setView(layout)
            .setPositiveButton("Invite") { _, _ ->
                val selectedGroup = spinner.selectedItem.toString()
                val email = emailInput.text.toString().trim()
                if (email.isNotEmpty()) {
                    saveInviteToGroup(selectedGroup, email)
                    Toast.makeText(requireContext(), "Invited $email to $selectedGroup", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Email cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun getSavedGroups(): MutableSet<String> {
        val prefs = requireContext().getSharedPreferences("groups", 0)
        return prefs.getStringSet("group_list", mutableSetOf()) ?: mutableSetOf()
    }

    private fun saveGroup(groupName: String) {
        val prefs = requireContext().getSharedPreferences("groups", 0)
        val groups = getSavedGroups()
        groups.add(groupName)
        prefs.edit().putStringSet("group_list", groups).apply()
    }

    private fun saveInviteToGroup(groupName: String, email: String) {
        val prefs = requireContext().getSharedPreferences("group_invites", 0)
        val key = "members_$groupName"
        val members = prefs.getStringSet(key, mutableSetOf()) ?: mutableSetOf()
        members.add(email)
        prefs.edit().putStringSet(key, members).apply()
    }
}