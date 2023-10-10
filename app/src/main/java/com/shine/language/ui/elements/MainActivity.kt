package com.shine.language.ui.elements

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.shine.language.data.model.English
import com.shine.language.ui.intent.EnglishIntent
import com.shine.language.ui.state.EnglishUiState
import com.shine.language.ui.theme.ShineLanguageTheme
import com.shine.language.ui.viewmodel.EnglishViewModel
import kotlinx.coroutines.flow.Flow
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
                    }

                    is EnglishUiState.EnglishList -> {
                        renderList(it.englishListPagingData)
                    }

                    is EnglishUiState.Error -> {
                        Toast.makeText(this@MainActivity, it.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun renderList(englishList: Flow<PagingData<English>>) {
        refreshEnglishList(englishList)
    }

    private fun setContent() {
        setContent {
            ShineLanguageTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainView()
                }
            }
        }
    }

    private fun refreshEnglishList(englishList: Flow<PagingData<English>>) {
        setContent {
            ShineLanguageTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    WordList(englishList)
                }
            }
        }
    }

    @Composable
    fun MainView() {
        Row(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "加载中")
        }
    }

    @Composable
    fun WordList(englishListPagingData: Flow<PagingData<English>>) {
        val pagingItems = englishListPagingData.collectAsLazyPagingItems()
        LazyColumn(Modifier.fillMaxWidth()) {
            itemsIndexed(pagingItems) { _, it ->
                val english = it ?: English()
                WordRow(english)
            }
        }
    }

    @Composable
    fun WordRow(english: English) {
        Card(
            Modifier
                .padding(5.dp)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(start = 5.dp)) {
                Text(text = english.word, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 5.dp))
                val accent = "${english.britishAccent}\n${english.americanAccent}"
                Text(text = accent, modifier = Modifier.padding(top = 5.dp))
                val explain = "${english.explain}"
                Text(text = explain, modifier = Modifier.padding(top = 5.dp, bottom = 5.dp))
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        ShineLanguageTheme {
            MainView()
        }
    }
}

