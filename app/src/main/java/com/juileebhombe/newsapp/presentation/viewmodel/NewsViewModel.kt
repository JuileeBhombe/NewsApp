package com.juileebhombe.newsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juileebhombe.newsapp.domain.model.Article
import com.juileebhombe.newsapp.domain.usecase.GetTopHeadlinesUseCase
import com.juileebhombe.newsapp.util.ConnectivityObserver
import com.juileebhombe.newsapp.util.Constants
import com.juileebhombe.newsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase,
    private val connectivityObserver: ConnectivityObserver,
) : ViewModel() {

    private val _newsState = MutableStateFlow<NewsState>(NewsState.Loading)
    val newsState: StateFlow<NewsState> = _newsState.asStateFlow()

    private val _currentPage = MutableStateFlow(1)
    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    private val _isLoadingMore = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)
    private val _isConnected = MutableStateFlow(true)
    private val _hasMoreItems = MutableStateFlow(true)
    val hasMoreItems: StateFlow<Boolean> = _hasMoreItems.asStateFlow()

    init {
        observeConnectivity()
        loadNews()
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            connectivityObserver.isConnected.collectLatest { isConnected ->
                _isConnected.value = isConnected
                if (!isConnected && _articles.value.isEmpty()) {
                    _newsState.value = NewsState.Error(Constants.Strings.NO_INTERNET_CONNECTION)
                }
            }
        }
    }

    fun loadNews() {
        if (!_isConnected.value) {
            _newsState.value = NewsState.Error(Constants.Strings.NO_INTERNET_CONNECTION)
            return
        }

        viewModelScope.launch {
            _newsState.value = NewsState.Loading
            _error.value = null

            when (val result = getTopHeadlinesUseCase(page = 1, pageSize = 10)) {
                is Resource.Success -> {
                    result.data?.let { articles ->
                        _articles.value = articles
                        _currentPage.value = 1
                        _hasMoreItems.value = articles.size >= 10
                        _newsState.value = if (articles.isEmpty()) {
                            NewsState.Empty
                        } else {
                            NewsState.Success(articles)
                        }
                    } ?: run {
                        _newsState.value = NewsState.Error(Constants.Strings.FAILED_TO_LOAD_NEWS)
                    }
                }

                is Resource.Error -> {
                    _error.value = result.message
                    _newsState.value =
                        NewsState.Error(result.message ?: Constants.Strings.UNKNOWN_ERROR_OCCURRED)
                }

                is Resource.Loading -> {
                    _newsState.value = NewsState.Loading
                }
            }
        }
    }

    fun loadMoreNews() {
        if (_isLoadingMore.value || !_isConnected.value) return

        viewModelScope.launch {
            _isLoadingMore.value = true

            when (val result =
                getTopHeadlinesUseCase(page = _currentPage.value + 1, pageSize = 10)) {
                is Resource.Success -> {
                    result.data?.let { newArticles ->
                        if (newArticles.isNotEmpty()) {
                            val updatedArticles = _articles.value + newArticles
                            _articles.value = updatedArticles
                            _currentPage.value += 1
                            _hasMoreItems.value = newArticles.size >= 10
                            _newsState.value = NewsState.Success(updatedArticles)
                        } else {
                            _hasMoreItems.value = false
                        }
                    } ?: run {
                        _hasMoreItems.value = false
                    }
                }

                is Resource.Error -> {
                    _error.value = result.message
                }

                is Resource.Loading -> {
                }
            }

            _isLoadingMore.value = false
        }
    }

    fun retry() {
        loadNews()
    }

    sealed class NewsState {
        data object Loading : NewsState()
        data object Empty : NewsState()
        data class Success(val articles: List<Article>) : NewsState()
        data class Error(val message: String) : NewsState()
    }
}
