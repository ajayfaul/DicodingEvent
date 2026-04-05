package com.jayfm.dicodingevent.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jayfm.dicodingevent.data.EventRepository
import com.jayfm.dicodingevent.data.Result
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
            repository.getUpcomingEvents().collect { result ->
                when (result) {
                    is Result.Success -> {
                        allUpcomingEvents = result.data
                        _upcomingEvents.value = allUpcomingEvents.take(5)
                        _isLoadingUpcoming.value = false
                    }
                    is Result.Error -> {
                        _errorMessage.value = result.error
                        Log.e("HomeViewModel", "Error loading upcoming events: ${result.error}")
                        _isLoadingUpcoming.value = false
                    }
                    is Result.Loading -> {
                        _isLoadingUpcoming.value = true
                    }
                }
            }
        }

        viewModelScope.launch {
            _isLoadingFinished.value = true
            repository.getFinishedEvents().collect { result ->
                when (result) {
                    is Result.Success -> {
                        allFinishedEvents = result.data
                        _finishedEvents.value = allFinishedEvents.take(5)
                        _isLoadingFinished.value = false
                    }
                    is Result.Error -> {
                        _errorMessage.value = result.error
                        Log.e("HomeViewModel", "Error loading finished events: ${result.error}")
                        _isLoadingFinished.value = false
                    }
                    is Result.Loading -> {
                        _isLoadingFinished.value = true
                    }
                }
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
