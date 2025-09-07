package com.juileebhombe.newsapp.presentation.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.juileebhombe.newsapp.domain.model.Article
import com.juileebhombe.newsapp.presentation.ui.components.ArticleHeader
import com.juileebhombe.newsapp.presentation.ui.components.OpenInWebButton
import com.juileebhombe.newsapp.util.Constants
import com.juileebhombe.newsapp.util.DateFormatUtils

@Composable
fun ArticleDetailScreen(
    article: Article,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        ArticleHeader(
            article = article,
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            article.author?.let { author ->
                    Text(
                        text = "${Constants.Strings.BY_AUTHOR_PREFIX}$author",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
            }

            Text(
                text = DateFormatUtils.formatPublishedDate(article.publishedAt),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            article.description?.let { description ->
                    Text(
                        text = Constants.Strings.DESCRIPTION,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = MaterialTheme.typography.bodyLarge.fontSize * 1.4,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            article.content?.let { content ->
                    Text(
                        text = Constants.Strings.CONTENT,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = MaterialTheme.typography.bodyLarge.fontSize * 1.4,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        OpenInWebButton(url = article.url)
    }
}
