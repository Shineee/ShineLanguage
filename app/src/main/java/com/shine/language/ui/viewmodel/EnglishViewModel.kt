package com.shine.language.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shine.language.domain.EnglishUseCase
import com.shine.language.ui.intent.EnglishIntent
import com.shine.language.ui.state.EnglishUiState
import com.shine.language.util.log.e
import com.shine.language.util.log.i
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class EnglishViewModel : ViewModel() {
    private val englishUseCase = EnglishUseCase()
    val englishIntent = Channel<EnglishIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<EnglishUiState>(EnglishUiState.Idle)
    val state: StateFlow<EnglishUiState>
        get() = _state

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            englishIntent.consumeAsFlow().collect {
                when (it) {
                    is EnglishIntent.FetchEnglishList -> fetchUser()
                }
            }
        }
    }

    private fun fetchUser() {
        viewModelScope.launch {
            "start".i()
            _state.value = EnglishUiState.Loading
            _state.value = try {
                val englishList = englishUseCase.getEnglishList()
                "englishList.size=${englishList.size}".i()
                EnglishUiState.EnglishList(englishList)
            } catch (e: Exception) {
                e.e()
                EnglishUiState.Error(e.localizedMessage)
            }
        }
    }
}