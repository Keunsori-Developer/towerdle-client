package com.keunsori.domain.repository

interface MainRepository {
    suspend fun test(test: String): String
}