package me.gefro.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "archives")
data class DownloadedFile(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val repo: String,
    val owner: String,
    val rate: Int,
    val fileName: String,
    val description: String
)