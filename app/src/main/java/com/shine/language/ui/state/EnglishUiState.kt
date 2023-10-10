package com.shine.language.ui.state

import androidx.paging.PagingData
import com.shine.language.data.model.English
import kotlinx.coroutines.flow.Flow

sealed class EnglishUiState {
    object Idle : EnglishUiState()
    object Loading : EnglishUiState()
    data class EnglishList(val englishListPagingData: Flow<PagingData<English>>) : EnglishUiState()
    data class Error(val error: String?) : EnglishUiState()
}