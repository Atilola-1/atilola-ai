package com.example.data.local

import androidx.room.*
import com.example.data.model.VocabularyWord
import kotlinx.coroutines.flow.Flow

@Dao
interface VocabularyDao {
    @Query("SELECT * FROM vocabulary_words ORDER BY dateSaved DESC")
    fun getAllVocabulary(): Flow<List<VocabularyWord>>

    @Query("SELECT * FROM vocabulary_words WHERE word = :word LIMIT 1")
    suspend fun getWordByWord(word: String): VocabularyWord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVocabulary(word: VocabularyWord): Long

    @Delete
    suspend fun deleteVocabulary(word: VocabularyWord)

    @Query("SELECT * FROM vocabulary_words WHERE word LIKE :query OR meaning LIKE :query ORDER BY dateSaved DESC")
    fun searchVocabulary(query: String): Flow<List<VocabularyWord>>
}
