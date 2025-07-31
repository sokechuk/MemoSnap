package com.example.MemoSnap.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [User::class, Album::class, Photo::class],
    version = 1
)
abstract class MemoSnapDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun albumDao(): AlbumDao
    abstract fun photoDao(): PhotoDao

    companion object {
        @Volatile private var instance: MemoSnapDatabase? = null

        fun getDatabase(context: Context): MemoSnapDatabase {
            return instance ?: synchronized(this) {
                val inst = Room.databaseBuilder(
                    context.applicationContext,
                    MemoSnapDatabase::class.java,
                    "memosnap.db"
                ).fallbackToDestructiveMigration() // auto-wipe on schema changes
                    .build()
                instance = inst
                inst
            }
        }
    }
}
