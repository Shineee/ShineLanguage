package com.shine.language.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
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
                val englishListPaging = englishUseCase.getEnglishList()
                val englishListPagingData = Pager(PagingConfig(pageSize = 10)) {
                    englishListPaging
                }.flow.cachedIn(viewModelScope)
                EnglishUiState.EnglishList(englishListPagingData)
            } catch (e: Exception) {
                e.e()
                EnglishUiState.Error(e.localizedMessage)
            }
        }
    }
}