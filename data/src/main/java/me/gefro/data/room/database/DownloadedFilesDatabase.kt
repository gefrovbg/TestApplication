package me.gefro.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.gefro.data.room.models.DownloadedFile
import me.gefro.data.room.repository.DownloadedFilesDAO

@Database(entities = [DownloadedFile::class], version = 1)
abstract class DownloadedFilesDatabase : RoomDatabase() {
    abstract fun downloadedFilesDAO(): DownloadedFilesDAO

    companion object {
        @Volatile
        private var INSTANCE: DownloadedFilesDatabase? = null

        fun getDatabase(context: Context): DownloadedFilesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DownloadedFilesDatabase::class.java,
                    "downloaded_files_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}