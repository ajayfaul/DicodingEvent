package com.jayfm.dicodingevent.data

import com.jayfm.dicodingevent.data.remote.retrofit.ApiService

class EventRepository private constructor(
    private val apiService: ApiService,
    private val favoriteEventDao: com.jayfm.dicodingevent.data.local.room.FavoriteEventDao
) {
    suspend fun getUpcomingEvents() = apiService.getEvents(active = 1)
    suspend fun getFinishedEvents() = apiService.getEvents(active = 0)
    suspend fun getDetailEvent(id: String) = apiService.getDetailEvent(id)
    suspend fun searchEvents(query: String) = apiService.getEvents(active = -1, query = query)

    fun getFavoriteEvents() = favoriteEventDao.getAllFavoriteEvents()
    suspend fun insertFavorite(event: com.jayfm.dicodingevent.data.local.entity.FavoriteEventEntity) = favoriteEventDao.insert(event)
    suspend fun deleteFavorite(event: com.jayfm.dicodingevent.data.local.entity.FavoriteEventEntity) = favoriteEventDao.delete(event)
    fun getFavoriteById(id: Int) = favoriteEventDao.getFavoriteEventById(id)

    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(apiService: ApiService, favoriteEventDao: com.jayfm.dicodingevent.data.local.room.FavoriteEventDao): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, favoriteEventDao)
            }.also { instance = it }
    }
}
