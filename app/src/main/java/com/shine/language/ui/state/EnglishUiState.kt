package com.shine.language.ui.state

import com.shine.language.data.model.English

sealed class EnglishUiState {
    object Idle : EnglishUiState()
    object Loading : EnglishUiState()
    data class EnglishList(val englishList: List<English>) : EnglishUiState()
    data class Error(val error: String?) : EnglishUiState()
}