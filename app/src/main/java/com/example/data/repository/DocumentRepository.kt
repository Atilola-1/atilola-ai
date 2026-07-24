package com.example.data.repository

import com.example.data.local.DocumentDao
import com.example.data.model.Document
import kotlinx.coroutines.flow.Flow

class DocumentRepository(private val documentDao: DocumentDao) {
    val allDocuments: Flow<List<Document>> = documentDao.getAllDocuments()
    val favoriteDocuments: Flow<List<Document>> = documentDao.getFavoriteDocuments()
    val recentDocuments: Flow<List<Document>> = documentDao.getRecentDocuments()

    suspend fun getDocumentById(id: Int): Document? = documentDao.getDocumentById(id)

    suspend fun insertDocument(document: Document): Long = documentDao.insertDocument(document)

    suspend fun updateDocument(document: Document) = documentDao.updateDocument(document)

    suspend fun deleteDocument(document: Document) = documentDao.deleteDocument(document)

    suspend fun updateReadingProgress(id: Int, position: Int, progress: Float) {
        documentDao.updateReadingProgress(id, position, progress)
    }
}
