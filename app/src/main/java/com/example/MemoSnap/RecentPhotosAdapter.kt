package com.example.MemoSnap

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecentPhotosAdapter(
    private val context: Context,
    private val photoPaths: List<String>
) : RecyclerView.Adapter<RecentPhotosAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.imgThumbnail)
        val commentButton: ImageButton = view.findViewById(R.id.btnComment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_recent_photo, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photoPath = photoPaths[position]

        Glide.with(context)
            .load(photoPath)
            .centerCrop()
            .into(holder.image)

        holder.commentButton.setOnClickListener {
            showCommentDialog(photoPath)
        }
    }

    override fun getItemCount(): Int = photoPaths.size

    private fun showCommentDialog(photoPath: String) {
        val input = EditText(context)
        input.hint = "Enter your comment"

        AlertDialog.Builder(context)
            .setTitle("Comment on Photo")
            .setView(input)
            .setPositiveButton("Save") { _, _ ->
                val comment = input.text.toString().trim()
                if (comment.isNotEmpty()) {
                    val prefs = context.getSharedPreferences("photo_comments", 0)
                    prefs.edit().putString(photoPath, comment).apply()
                    Toast.makeText(context, "Comment saved", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}