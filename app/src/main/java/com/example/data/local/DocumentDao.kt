package com.example.data.local

import androidx.room.*
import com.example.data.model.Document
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentDao {
    @Query("SELECT * FROM documents ORDER BY lastReadTimestamp DESC")
    fun getAllDocuments(): Flow<List<Document>>

    @Query("SELECT * FROM documents WHERE id = :id")
    suspend fun getDocumentById(id: Int): Document?

    @Query("SELECT * FROM documents WHERE isFavorite = 1 ORDER BY lastReadTimestamp DESC")
    fun getFavoriteDocuments(): Flow<List<Document>>

    @Query("SELECT * FROM documents ORDER BY lastReadTimestamp DESC LIMIT 5")
    fun getRecentDocuments(): Flow<List<Document>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(document: Document): Long

    @Update
    suspend fun updateDocument(document: Document)

    @Delete
    suspend fun deleteDocument(document: Document)

    @Query("UPDATE documents SET lastReadPosition = :position, readingProgress = :progress, lastReadTimestamp = :timestamp WHERE id = :id")
    suspend fun updateReadingProgress(id: Int, position: Int, progress: Float, timestamp: Long = System.currentTimeMillis())
}
