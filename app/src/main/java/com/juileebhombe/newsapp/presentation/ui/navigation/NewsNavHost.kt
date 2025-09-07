package com.juileebhombe.newsapp.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.juileebhombe.newsapp.domain.model.Article
import com.juileebhombe.newsapp.presentation.ui.screen.ArticleDetailScreen
import com.juileebhombe.newsapp.presentation.ui.screen.NewsListScreen

@Composable
fun NewsNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.NEWS_LIST
    ) {
        composable(NavRoutes.NEWS_LIST) {
            NewsListScreen(
                onArticleClick = { article ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("article", article)
                    navController.navigate(NavRoutes.ARTICLE_DETAIL)
                }
            )
        }

        composable(NavRoutes.ARTICLE_DETAIL) { backStackEntry ->
            val article = navController.previousBackStackEntry?.savedStateHandle?.get<Article>("article")
            article?.let {
                ArticleDetailScreen(
                    article = it,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
