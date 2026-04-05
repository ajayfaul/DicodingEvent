package com.jayfm.dicodingevent.data

import com.jayfm.dicodingevent.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow

class EventRepository private constructor(
    private val apiService: ApiService,
    private val favoriteEventDao: com.jayfm.dicodingevent.data.local.room.FavoriteEventDao
) {
    fun getUpcomingEvents(): Flow<Result<List<com.jayfm.dicodingevent.data.remote.response.ListEventsItem>>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getEvents(active = 1)
            emit(Result.Success(response.listEvents))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getFinishedEvents(): Flow<Result<List<com.jayfm.dicodingevent.data.remote.response.ListEventsItem>>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getEvents(active = 0)
            emit(Result.Success(response.listEvents))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getDetailEvent(id: String): Flow<Result<com.jayfm.dicodingevent.data.remote.response.ListEventsItem>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailEvent(id)
            emit(Result.Success(response.event))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
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
