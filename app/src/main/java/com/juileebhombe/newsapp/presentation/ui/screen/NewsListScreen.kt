package com.juileebhombe.newsapp.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.juileebhombe.newsapp.domain.model.Article
import com.juileebhombe.newsapp.presentation.ui.components.EmptyView
import com.juileebhombe.newsapp.presentation.ui.components.ErrorView
import com.juileebhombe.newsapp.presentation.ui.components.LoadingView
import com.juileebhombe.newsapp.presentation.ui.components.NewsCard
import com.juileebhombe.newsapp.presentation.viewmodel.NewsViewModel
import com.juileebhombe.newsapp.util.Constants
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListScreen(
    onArticleClick: (Article) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NewsViewModel = hiltViewModel(),
) {
    val newsState by viewModel.newsState.collectAsState()
    val hasMoreItems by viewModel.hasMoreItems.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .collect { lastVisibleIndex ->
                val totalItems = listState.layoutInfo.totalItemsCount
                if (lastVisibleIndex != null &&
                    lastVisibleIndex >= totalItems - 3 &&
                    hasMoreItems &&
                    newsState !is NewsViewModel.NewsState.Loading
                ) {
                    viewModel.loadMoreNews()
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = Constants.Strings.NEWS_HEADLINES,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = newsState) {
                is NewsViewModel.NewsState.Loading -> {
                    LoadingView()
                }

                is NewsViewModel.NewsState.Empty -> {
                    EmptyView()
                }

                is NewsViewModel.NewsState.Error -> {
                    ErrorView(
                        message = state.message,
                        onRetry = { viewModel.retry() }
                    )
                }

                is NewsViewModel.NewsState.Success -> {
                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 16.dp,
                            bottom = 16.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.articles) { article ->
                            NewsCard(
                                article = article,
                                onClick = { onArticleClick(article) }
                            )
                        }

                        // Pagination loader
                        if (hasMoreItems && state.articles.isNotEmpty()) {
                            item {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
