package com.umt.memingoapp.data.repository

import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.umt.memingoapp.domain.model.uiModels.WordLearningContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeminiRepository(apiKey: String) {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.0-flash",
        apiKey = apiKey
    )

    suspend fun generateWordContent(
        word: String,
        language: String
    ): WordLearningContent = withContext(Dispatchers.IO) {
        try {
            Log.d("GeminiRepository", "Starting content generation for word: $word in $language")

            val prompt = """
    You are a language learning assistant. Generate comprehensive learning content for the word "$word" in $language.
    
    Return a JSON response with the following structure:
    {
        "word": "$word",
        "language": "$language",
        "translation": "English translation",
        "pronunciation": "IPA pronunciation",
        "article": "der/die/das (if applicable)",
        "category": "word category",
        "difficulty": "Beginner/Intermediate/Advanced",
        "sentences": [
            {
                "number": 1,
                "foreignLang": "$language",
                "foreignFlag": "🇩🇪",
                "foreignText": "Sentence in target language",
                "englishFlag": "🇬🇧",
                "englishText": "English translation",
                "ipa": "IPA pronunciation"
            }
        ],
        "tips": [
            {
                "tipNumber": 1,
                "foreignLang": "$language",
                "foreignFlag": "🇩🇪",
                "foreignText": "Usage tip in target language",
                "englishFlag": "🇬🇧",
                "englishText": "English explanation"
            }
        ],
        "facts": [
            {
                "factNumber": 1,
                "foreignLang": "$language",
                "foreignFlag": "🇩🇪",
                "foreignText": "Cultural fact in target language",
                "englishFlag": "🇬🇧",
                "englishText": "English explanation"
            }
        ],
        "relatedWords": [
            {
                 "word": "A closely related word or synonym of '$word' in $language",
                 "translation": "Meaning of that word in English"
            }
        ]
    }
    
    Generate 5 example sentences, 5 usage tips, 5 cultural facts, and 5 related words.
    For German, include the correct article (der/die/das).
    For flags, use appropriate emoji flags (🇩🇪 for German, 🇬🇧 for English).
""".trimIndent()

            Log.d("GeminiRepository", "Sending prompt to Gemini:\n$prompt")

            val response = generativeModel.generateContent(prompt)
            val jsonResponse = response.text ?: throw Exception("Empty response from Gemini")

            Log.d("GeminiRepository", "Received response from Gemini:\n$jsonResponse")

            // Parse JSON response into WordLearningContent
            val parsedContent = parseGeminiResponse(jsonResponse)
            Log.d("GeminiRepository", "Successfully parsed content: $parsedContent")

            parsedContent
        } catch (e: Exception) {
            Log.e("GeminiRepository", "Error generating content", e)
            throw Exception("Failed to generate content: ${e.message}")
        }
    }

    private fun parseGeminiResponse(json: String): WordLearningContent {
        Log.d("GeminiRepository", "Attempting to parse JSON response")
        return try {
            // Remove Markdown code block markers if present
            val cleanJson = json
                .replaceFirst("```json", "")
                .replaceFirst("```", "")
                .trim()

            Log.d("GeminiRepository", "Cleaned JSON: $cleanJson")

            val result = kotlinx.serialization.json.Json.decodeFromString<WordLearningContent>(cleanJson)
            Log.d("GeminiRepository", "JSON parsing successful")
            result
        } catch (e: Exception) {
            Log.e("GeminiRepository", "Error parsing JSON response", e)
            throw e
        }
    }

    suspend fun generateTutorResponse(
        userMessage: String,
        courseLanguage: String
    ): String = withContext(Dispatchers.IO) {
        try {
            val prompt = """
            You are an AI language tutor for $courseLanguage. 
            The student can write in any language, but you must always respond in $courseLanguage. 
            Keep responses simple and conversational, as if chatting with a student. 
            If it's the first message, greet them warmly and ask what they want to learn today.
        """.trimIndent()

            val response = generativeModel.generateContent("$prompt\nStudent: $userMessage\nTutor:")
            response.text ?: "I'm ready to help you learn $courseLanguage!"
        } catch (e: Exception) {
            Log.e("GeminiRepository", "Error generating tutor response", e)
            "Sorry, I couldn’t process that. Try again!"
        }
    }

    suspend fun generateStarterMessage(
        courseLanguage: String
    ): String = withContext(Dispatchers.IO) {
        try {
            val prompt = """
            You are an AI language tutor for $courseLanguage. 
            Greet the student warmly and ask them what they would like to learn today. Keep it friendly and short.
        """.trimIndent()

            val response = generativeModel.generateContent(prompt)
            response.text ?: "Hello! Ready to start learning $courseLanguage?"
        } catch (e: Exception) {
            "Hello! Ready to start learning $courseLanguage?"
        }
    }

    suspend fun translateToEnglish(text: String, sourceLanguage: String): String = withContext(Dispatchers.IO) {
        try {
            val prompt = """
            Translate the following $sourceLanguage text to English. 
            Return only the translated text, no extra explanation:
            
            "$text"
        """.trimIndent()

            val response = generativeModel.generateContent(prompt)
            response.text?.trim() ?: "Translation unavailable"
        } catch (e: Exception) {
            Log.e("GeminiRepository", "Error translating text", e)
            "Translation failed"
        }
    }


}