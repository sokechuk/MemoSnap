package com.example.MemoSnap.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val uri: String,
    val description: String?,
    val albumId: Int, // foreign key reference to Album.id
    val timestamp: Long = System.currentTimeMillis(),
    val tags: String?, // comma-separated list of tags (for simplicity)
    val isSynced: Boolean = false // sync tracking
)
