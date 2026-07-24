package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "documents")
data class Document(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val fileType: String, // PDF, TXT, DOCX, PNG, etc.
    val language: String, // detected: English, Arabic, Hausa, Yoruba, etc.
    val isFavorite: Boolean = false,
    val readingProgress: Float = 0.0f,
    val lastReadPosition: Int = 0,
    val lastReadTimestamp: Long = System.currentTimeMillis()
)
