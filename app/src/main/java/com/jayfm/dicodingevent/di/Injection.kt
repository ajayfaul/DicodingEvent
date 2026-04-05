package com.jayfm.dicodingevent.di

import com.jayfm.dicodingevent.data.EventRepository
import com.jayfm.dicodingevent.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: android.content.Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = com.jayfm.dicodingevent.data.local.room.FavoriteDatabase.getDatabase(context)
        val dao = database.favoriteEventDao()
        return EventRepository.getInstance(apiService, dao)
    }
}
