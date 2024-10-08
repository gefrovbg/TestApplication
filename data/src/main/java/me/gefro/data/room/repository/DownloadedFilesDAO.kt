package me.gefro.data.room.repository

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import me.gefro.data.room.models.DownloadedFile

@Dao
interface DownloadedFilesDAO {

    @Insert
    suspend fun insert(file: DownloadedFile)

    @Update
    suspend fun update(file: DownloadedFile)

    @Delete
    suspend fun delete(file: DownloadedFile)

    @Query("SELECT * FROM archives")
    fun getAll(): LiveData<List<DownloadedFile>>

}