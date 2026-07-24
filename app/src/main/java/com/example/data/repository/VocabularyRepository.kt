package com.example.data.repository

import com.example.data.local.VocabularyDao
import com.example.data.model.VocabularyWord
import kotlinx.coroutines.flow.Flow

class VocabularyRepository(private val vocabularyDao: VocabularyDao) {
    val allVocabulary: Flow<List<VocabularyWord>> = vocabularyDao.getAllVocabulary()

    suspend fun getWordByWord(word: String): VocabularyWord? = vocabularyDao.getWordByWord(word)

    suspend fun insertVocabulary(word: VocabularyWord): Long = vocabularyDao.insertVocabulary(word)

    suspend fun deleteVocabulary(word: VocabularyWord) = vocabularyDao.deleteVocabulary(word)

    fun searchVocabulary(query: String): Flow<List<VocabularyWord>> = vocabularyDao.searchVocabulary("%$query%")
}
