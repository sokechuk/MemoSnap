package com.example.MemoSnap.data

import androidx.room.*

@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(album: Album)

    @Update
    suspend fun update(album: Album)

    @Delete
    suspend fun delete(album: Album)

    @Query("SELECT * FROM albums ORDER BY createdAt DESC")
    suspend fun getAllAlbums(): List<Album>

    @Query("SELECT * FROM albums WHERE id = :albumId LIMIT 1")
    suspend fun getAlbumById(albumId: Int): Album?

    @Transaction
    @Query("SELECT * FROM albums ORDER BY createdAt DESC")
    suspend fun getAlbumsWithPhotos(): List<AlbumWithPhotos>
}
