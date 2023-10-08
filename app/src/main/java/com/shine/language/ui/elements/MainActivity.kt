package com.shine.language.ui.elements

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.shine.language.data.model.English
import com.shine.language.domain.EnglishUseCase
import com.shine.language.ui.intent.EnglishIntent
import com.shine.language.ui.state.EnglishUiState
import com.shine.language.ui.theme.ShineLanguageTheme
import com.shine.language.ui.viewmodel.EnglishViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val englishViewModel by viewModels<EnglishViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent()
        initData()
        observeViewModel()
    }

    private fun initData() {
        lifecycleScope.launch {
            englishViewModel.englishIntent.send(EnglishIntent.FetchEnglishList)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            englishViewModel.state.collect {
                when (it) {
                    is EnglishUiState.Idle -> {

                    }

                    is EnglishUiState.Loading -> {
//                        buttonFetchUser.visibility = View.GONE
//                        progressBar.visibility = View.VISIBLE
                    }

                    is EnglishUiState.EnglishList -> {
//                        progressBar.visibility = View.GONE
//                        buttonFetchUser.visibility = View.GONE
                        renderList(it.englishList)
                    }

                    is EnglishUiState.Error -> {
//                        progressBar.visibility = View.GONE
//                        buttonFetchUser.visibility = View.VISIBLE
//                        Toast.makeText(this@MainActivity, it.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun renderList(englishList: List<English>) {
        refreshEnglishList(englishList)
    }

    private fun setContent() {
        setContent {
            ShineLanguageTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    mainView()
                }
            }
        }
    }

    private fun refreshEnglishList(englishList: List<English>) {
        setContent {
            ShineLanguageTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    wordList(englishList)
                }
            }
        }
    }

    @Composable
    fun mainView() {

    }

    @Composable
    fun wordList(englishList: List<English>) {
        LazyColumn(
            modifier = Modifier
                .background(Color.LightGray)
        )
        {
            items(englishList) {
                wordRow(it)
            }
        }
    }

    @Composable
    fun wordRow(english: English) {
        Card {
            Text(text = english.word)
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        ShineLanguageTheme {
            mainView()
        }
    }
}

