package com.umt.memingoapp.viewmodels

import androidx.lifecycle.ViewModel
import com.umt.memingoapp.lessons.GermanAlphabet
import com.umt.memingoapp.lessons.germanAlphabets
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AlphabetLessonViewModel : ViewModel() {
    // Lesson state
    private val _currentAlphabetIndex = MutableStateFlow(0)
    val currentAlphabetIndex: StateFlow<Int> = _currentAlphabetIndex.asStateFlow()

    private val _remainingLives = MutableStateFlow(3)
    val remainingLives: StateFlow<Int> = _remainingLives.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    private val _userSpeechResult = MutableStateFlow("")
    val userSpeechResult: StateFlow<String> = _userSpeechResult.asStateFlow()

    private val _isUserCorrect = MutableStateFlow<Boolean?>(null)
    val isUserCorrect: StateFlow<Boolean?> = _isUserCorrect.asStateFlow()

    private val _showFailureDialog = MutableStateFlow(false)
    val showFailureDialog: StateFlow<Boolean> = _showFailureDialog.asStateFlow()

    private val _isLessonComplete = MutableStateFlow(false)
    val isLessonComplete: StateFlow<Boolean> = _isLessonComplete.asStateFlow()

    // Lesson constants
    val totalAlphabets = 7
    private var currentLessonAlphabets = germanAlphabets.shuffled().take(totalAlphabets)

    val currentAlphabet: GermanAlphabet
        get() = currentLessonAlphabets[_currentAlphabetIndex.value]

    fun playAlphabetSound() {
        // Implement sound playback
    }

    fun startSpeechRecognition() {
        _isListening.value = true
    }

    fun processSpeechResult(result: String) {
        _isListening.value = false
        _userSpeechResult.value = result

        val isCorrect = result.equals(currentAlphabet.name, ignoreCase = true) ||
                result.equals(currentAlphabet.lowercase, ignoreCase = true) ||
                result.equals(currentAlphabet.uppercase, ignoreCase = true)

        _isUserCorrect.value = isCorrect

        if (isCorrect) {
            _score.value += 10
            moveToNextAlphabet()
        } else {
            _remainingLives.value -= 1
            if (_remainingLives.value <= 0) {
                _showFailureDialog.value = true
            }
        }
    }

    private fun moveToNextAlphabet() {
        if (_currentAlphabetIndex.value + 1 >= totalAlphabets) {
            _isLessonComplete.value = true
        } else {
            _currentAlphabetIndex.value += 1
            _userSpeechResult.value = ""
            _isUserCorrect.value = null
        }
    }

    fun resetLesson() {
        _currentAlphabetIndex.value = 0
        _remainingLives.value = 3
        _score.value = 0
        _userSpeechResult.value = ""
        _isUserCorrect.value = null
        _showFailureDialog.value = false
        _isLessonComplete.value = false
        // Reshuffle alphabets for new attempt
        currentLessonAlphabets = germanAlphabets.shuffled().take(totalAlphabets)
    }
}