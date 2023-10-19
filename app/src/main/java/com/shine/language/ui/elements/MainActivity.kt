package com.shine.language.ui.elements

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.shine.language.util.log.i
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
                        setContent()
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
                .fillMaxHeight(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "加载中")
        }
    }

    @Composable
    fun WordList(englishListPagingData: Flow<PagingData<English>>) {
        val pagingItems = englishListPagingData.collectAsLazyPagingItems()
        LazyColumn(Modifier.fillMaxWidth()) {
            itemsIndexed(pagingItems) { _, it ->
                if (it != null) {
                    WordRow(it)
                }
            }
        }
    }

    @Composable
    fun WordRow(english: English) {
        Card(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
        ) {
            var isVisibleAccent by rememberSaveable { mutableStateOf(false) }

            Column(modifier = Modifier.padding(start = 5.dp, end = 5.dp)) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
                ) {
                    val word = "${english.id}.${english.word}"
                    Text(
                        text = word, fontWeight = FontWeight.Bold, modifier = Modifier
                            .padding(top = 0.dp)
                    )
                    Text(text = "音标", modifier = Modifier.clickable { isVisibleAccent = !isVisibleAccent })
                }

                if (isVisibleAccent) {
                    val accent = "${english.britishAccent}\n${english.americanAccent}"
                    Text(text = accent, modifier = Modifier.padding(top = 5.dp))
                }

                val explain = "${english.paraphrase}"
                Text(text = explain, modifier = Modifier.padding(top = 5.dp, bottom = 5.dp))

            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        ShineLanguageTheme {
            val english = English(1)
            english.word = "a"
            english.britishAccent = "英[aaa]"
            english.americanAccent = "美[aaa]"
            english.paraphrase = "一个一个一个一个一个一个"
            WordRow(english)
        }
    }
}

