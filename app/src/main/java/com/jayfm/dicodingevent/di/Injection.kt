package com.jayfm.dicodingevent.di

import com.jayfm.dicodingevent.data.EventRepository
import com.jayfm.dicodingevent.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(): EventRepository {
        val apiService = ApiConfig.getApiService()
        return EventRepository.getInstance(apiService)
    }
}
