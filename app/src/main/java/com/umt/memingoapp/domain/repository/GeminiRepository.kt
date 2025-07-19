package com.umt.memingoapp.domain.repository

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
}