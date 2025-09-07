package com.juileebhombe.newsapp.data.model

import com.juileebhombe.newsapp.domain.model.Article
import com.juileebhombe.newsapp.domain.model.Source

fun ArticleDto.toDomain(): Article {
    return Article(
        source = source?.toDomain(),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content
    )
}

fun SourceDto.toDomain(): Source {
    return Source(
        id = id,
        name = name
    )
}

fun ArticleResponse.toDomain(): List<Article> {
    return articles.map { it.toDomain() }
}
