package com.jayfm.dicodingevent.ui.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jayfm.dicodingevent.data.EventRepository
import com.jayfm.dicodingevent.data.remote.response.ListEventsItem
import kotlinx.coroutines.launch

class FinishedViewModel(private val repository: EventRepository) : ViewModel() {

    private val _eventResult = MutableLiveData<com.jayfm.dicodingevent.data.Result<List<ListEventsItem>>>()
    val eventResult: LiveData<com.jayfm.dicodingevent.data.Result<List<ListEventsItem>>> = _eventResult

    init {
        getFinishedEvents()
    }

    fun getFinishedEvents() {
        viewModelScope.launch {
            repository.getFinishedEvents().collect { result ->
                _eventResult.value = result
            }
        }
    }

    fun searchEvents(query: String) {
        viewModelScope.launch {
            try {
                val response = repository.searchEvents(query)
                _eventResult.value = com.jayfm.dicodingevent.data.Result.Success(response.listEvents)
            } catch (e: Exception) {
                _eventResult.value = com.jayfm.dicodingevent.data.Result.Error(e.message.toString())
            }
        }
    }
}
