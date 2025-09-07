package com.juileebhombe.newsapp.repository

import com.juileebhombe.newsapp.BuildConfig
import com.juileebhombe.newsapp.data.api.NewsApiService
import com.juileebhombe.newsapp.data.model.toDomain
import com.juileebhombe.newsapp.domain.model.Article
import com.juileebhombe.newsapp.util.Constants
import com.juileebhombe.newsapp.util.Resource
import retrofit2.HttpException
import java.io.IOException

class NewsRepositoryImpl(
    private val newsApiService: NewsApiService
) : NewsRepository {

    override suspend fun getTopHeadlines(
        country: String,
        page: Int,
        pageSize: Int
    ): Resource<List<Article>> {
        return try {
            val response = newsApiService.getTopHeadlines(
                country = country,
                apiKey = BuildConfig.NEWS_API_KEY,
                page = page,
                pageSize = pageSize
            )

            if (response.isSuccessful) {
                response.body()?.let { articleResponse ->
                    Resource.Success(articleResponse.toDomain())
                } ?: Resource.Error("Response body is null")
            } else {
                Resource.Error("${Constants.Strings.API_ERROR_PREFIX}${response.message()}")
            }
        } catch (e: HttpException) {
            Resource.Error("${Constants.Strings.NETWORK_ERROR_PREFIX}${e.localizedMessage ?: "Unknown error"}")
        } catch (e: IOException) {
            Resource.Error(Constants.Strings.NETWORK_CONNECTION_ERROR)
        } catch (e: Exception) {
            Resource.Error("${Constants.Strings.UNEXPECTED_ERROR_PREFIX}${e.localizedMessage ?: "Unknown error"}")
        }
    }
}
