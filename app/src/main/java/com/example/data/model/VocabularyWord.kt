package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vocabulary_words")
data class VocabularyWord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val word: String,
    val originalLanguage: String, // e.g., English, Arabic, Hausa, Yoruba
    val meaning: String, // Dynamic translation/dictionary meaning
    val partOfSpeech: String,
    val exampleSentence: String,
    val pronunciation: String,
    val contextExplanation: String, // AI context-specific explanation of how the word was used
    val contextSentence: String, // The actual sentence where the word was selected
    val dateSaved: Long = System.currentTimeMillis()
)
