package com.shine.language.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shine.language.data.repository.DeviceRepository
import com.shine.language.ui.intent.MainIntent
import com.shine.language.ui.state.MainUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class MainViewModel(private val repository: DeviceRepository) : ViewModel() {
    val userIntent = Channel<MainIntent>(Channel.UNLIMITED)
    private val _deviceUiState = MutableStateFlow(MainUiState())
    val deviceUiState: StateFlow<MainUiState> = _deviceUiState.asStateFlow()
    private var fetchJob: Job? = null

    fun fetchArticles(category: String) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try {
                val newsItems = repository.newsItemsForCategory(category)
                _deviceUiState.update {
//                    it.copy(newsItems)
                    it.copy()
                }
            } catch (ioe: IOException) {
                // Handle the error and notify the UI when appropriate.
                _deviceUiState.update {
//                    val messages = getMessagesFromThrowable(ioe)
//                    it.copy(userMessages = messages)
                    it.copy()
                }
            }
        }
    }
}