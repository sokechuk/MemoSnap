package com.example.MemoSnap.data

import androidx.room.*

@Dao
interface PhotoDao {
    @Insert
    suspend fun insert(photo: Photo): Long

    @Query("SELECT * FROM photos WHERE albumId = :albumId ORDER BY timestamp DESC")
    suspend fun getByAlbum(albumId: Int): List<Photo>

    @Query("SELECT * FROM photos WHERE isSynced = 0")
    suspend fun getUnsynced(): List<Photo>

    @Update
    suspend fun update(photo: Photo)

    @Delete
    suspend fun delete(photo: Photo)
}