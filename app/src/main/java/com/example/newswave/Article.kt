package com.example.newswave

class Article(
    private val source: Source?,
    private val author: String?,
    private val title: String?,
    private val description: String?,
    private val url: String?,
    private val urlToImage: String?,
    private val publishedAt: String?,
    private val content: String?
)  {

    fun getSource(): Source? {
        return source
    }

    fun getAuthor(): String? {
        return author
    }

    fun getDescription(): String? {
        return description
    }

    fun getUrl(): String? {
        return url
    }

    fun getUrlToImage(): String? {
        return urlToImage
    }

    fun getPublishedAt(): String? {
        return publishedAt
    }

    fun getContent(): String? {
        return content
    }

    fun getTitle(): String? {
        return title
    }
}