package com.jayfm.dicodingevent.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jayfm.dicodingevent.data.EventRepository
import com.jayfm.dicodingevent.data.remote.response.ListEventsItem
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: EventRepository) : ViewModel() {

    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvents: LiveData<List<ListEventsItem>> = _upcomingEvents

    private val _finishedEvents = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents: LiveData<List<ListEventsItem>> = _finishedEvents

    private val _isLoadingUpcoming = MutableLiveData<Boolean>()
    val isLoadingUpcoming: LiveData<Boolean> = _isLoadingUpcoming

    private val _isLoadingFinished = MutableLiveData<Boolean>()
    val isLoadingFinished: LiveData<Boolean> = _isLoadingFinished

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private var allUpcomingEvents: List<ListEventsItem> = emptyList()
    private var allFinishedEvents: List<ListEventsItem> = emptyList()

    init {
        loadEvents()
    }

    private fun loadEvents() {
        _errorMessage.value = null
        
        viewModelScope.launch {
            _isLoadingUpcoming.value = true
            try {
                val upcoming = repository.getUpcomingEvents()
                allUpcomingEvents = upcoming.listEvents
                _upcomingEvents.value = allUpcomingEvents.take(5)
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e("HomeViewModel", "Error loading upcoming events: ${e.message}")
            } finally {
                _isLoadingUpcoming.value = false
            }
        }

        viewModelScope.launch {
            _isLoadingFinished.value = true
            try {
                val finished = repository.getFinishedEvents()
                allFinishedEvents = finished.listEvents
                _finishedEvents.value = allFinishedEvents.take(5)
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e("HomeViewModel", "Error loading finished events: ${e.message}")
            } finally {
                _isLoadingFinished.value = false
            }
        }
    }

    fun searchEvents(query: String) {
        if (query.isEmpty()) {
            _upcomingEvents.value = allUpcomingEvents.take(5)
            _finishedEvents.value = allFinishedEvents.take(5)
        } else {
            _upcomingEvents.value = allUpcomingEvents.filter {
                it.name.contains(query, ignoreCase = true)
            }
            _finishedEvents.value = allFinishedEvents.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }
    }
}
