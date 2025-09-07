package com.juileebhombe.newsapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    val source: Source? = null,
    val author: String? = null,
    val title: String,
    val description: String? = null,
    val url: String,
    val urlToImage: String? = null,
    val publishedAt: String,
    val content: String? = null
) : Parcelable

@Parcelize
data class Source(
    val id: String? = null,
    val name: String
) : Parcelable
