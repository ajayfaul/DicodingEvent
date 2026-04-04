package com.jayfm.dicodingevent.data

import com.jayfm.dicodingevent.data.remote.retrofit.ApiService

class EventRepository private constructor(
    private val apiService: ApiService
) {
    suspend fun getUpcomingEvents() = apiService.getEvents(active = 1)
    suspend fun getFinishedEvents() = apiService.getEvents(active = 0)
    suspend fun getDetailEvent(id: String) = apiService.getDetailEvent(id)
    suspend fun searchEvents(query: String) = apiService.getEvents(active = -1, query = query)

    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(apiService: ApiService): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService)
            }.also { instance = it }
    }
}
