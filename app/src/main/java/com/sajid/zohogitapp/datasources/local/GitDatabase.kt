package com.sajid.zohogitapp.datasources.local

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sajid.zohogitapp.datasources.local.dao.GitLocalDao
import com.sajid.zohogitapp.datasources.model.GitItems

@Database(entities = [GitItems::class],version = 1,exportSchema = false)
abstract class GitDatabase :RoomDatabase() {
    abstract fun gitLocalDao():GitLocalDao
    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: GitDatabase? = null
        private val lock = Any()

        fun getInstance(context: Context): GitDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        GitDatabase::class.java, "zoho-git"
                    ).addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            Log.d("database-name", "Database created")
                            super.onCreate(db)
                        }

                        override fun onOpen(db: SupportSQLiteDatabase) {
                            Log.d("database-name", "Database opened")
                            super.onOpen(db)
                        }
                    })
                        .allowMainThreadQueries()
                        .build()
                }

            }
            return INSTANCE!!
        }
}}