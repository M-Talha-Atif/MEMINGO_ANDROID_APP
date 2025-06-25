package com.umt.memingoapp.domain.model.response


import com.google.gson.*
import java.lang.reflect.Type


data class LessonResponse(
    val activities: List<Activity>,
    val course_id: String,
    val description: String,
    val difficulty: String,
    val id: String,
    val lesson_id: String,
    val order: Int,
    val prerequisites: List<String>,
    val title: String,
    val type: String,
    val user_progress: UserProgress
)

data class Activity(
    val activity_id: String,
    val content: ActivityContent,
    val points: Int,
    val type: String
)

sealed class ActivityContent {

    data class ListenAndSelect(
        val audio_url: String,
        val correct_answer: String,
        val explanation: String,
        val instruction: String,
        val options: List<String>
    ) : ActivityContent()

    data class PronunciationPractice(
        val instruction: String,
        val words: List<PronunciationWord>
    ) : ActivityContent()

    data class SentenceBuilder(
        val correct_order: List<String>,
        val instruction: String,
        val translation: String,
        val words: List<String>
    ) : ActivityContent()

    data class TypeTranslation(
        val alternative_answers: List<String>?,
        val correct_answer: String,
        val english_text: String,
        val hints: List<String>?,
        val instruction: String
    ) : ActivityContent()

    data class DragAndDrop(
        val correct_order: List<String>,
        val instruction: String,
        val translation: String,
        val words: List<String>
    ) : ActivityContent()

    data class MatchPairs(
        val instruction: String,
        val pairs: List<PairData>
    ) : ActivityContent()

    data class MultipleChoice(
        val correct_answer: String,
        val explanation: String,
        val instruction: String,
        val options: List<String>,
        val question: String
    ) : ActivityContent()

    data class FillInBlank(
        val correct_answer: String,
        val instruction: String,
        val options: List<String>,
        val sentence: String,
        val translation: String
    ) : ActivityContent()

}

data class PairData(
    val english: String,
    val german: String? = null,
    val spanish: String? = null,
    val italian: String? = null
)

data class PronunciationWord(
    val audio_url: String,
    val meaning: String,
    val phonetic: String,
    val word: String
)

data class UserProgress(
    val activity_scores: Map<String, Int>,
    val completed_activities: Int,
    val overall_score: Int,
    val status: String
)




class ActivityDeserializer : JsonDeserializer<Activity> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Activity {
        val obj = json.asJsonObject
        val type = obj.get("type")?.asString ?: throw JsonParseException("Missing type for activity")
        val activityId = obj.get("activity_id").asString
        val points = obj.get("points").asInt

        val contentElement = obj.get("content")
        val content = when (type) {
            "listen_and_select"    -> context.deserialize<ActivityContent.ListenAndSelect>(contentElement, ActivityContent.ListenAndSelect::class.java)
            "pronunciation_practice" -> context.deserialize<ActivityContent.PronunciationPractice>(contentElement, ActivityContent.PronunciationPractice::class.java)
            "sentence_builder"     -> context.deserialize<ActivityContent.SentenceBuilder>(contentElement, ActivityContent.SentenceBuilder::class.java)
            "type_translation"     -> context.deserialize<ActivityContent.TypeTranslation>(contentElement, ActivityContent.TypeTranslation::class.java)
            "drag_and_drop"        -> context.deserialize<ActivityContent.DragAndDrop>(contentElement, ActivityContent.DragAndDrop::class.java)
            "match_pairs" -> context.deserialize<ActivityContent.MatchPairs>(contentElement, ActivityContent.MatchPairs::class.java)
            "multiple_choice" -> context.deserialize<ActivityContent.MultipleChoice>(contentElement, ActivityContent.MultipleChoice::class.java)
            "fill_in_blank" -> context.deserialize<ActivityContent.FillInBlank>(contentElement, ActivityContent.FillInBlank::class.java)
            else -> throw JsonParseException("Unknown activity type: $type")
        }

        return Activity(
            activity_id = activityId,
            content = content,
            points = points,
            type = type
        )
    }
}
// Lesson Submit

data class UpdateLessonSubmitResponse(
    val message: String,
    val status: String,
    val overall_score: Int,
    val is_completed: Boolean
)