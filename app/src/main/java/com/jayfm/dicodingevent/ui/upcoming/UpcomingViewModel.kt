package com.jayfm.dicodingevent.ui.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jayfm.dicodingevent.data.EventRepository
import com.jayfm.dicodingevent.data.remote.response.ListEventsItem
import kotlinx.coroutines.launch

class UpcomingViewModel(private val repository: EventRepository) : ViewModel() {

    private val _eventResult = MutableLiveData<com.jayfm.dicodingevent.data.Result<List<ListEventsItem>>>()
    val eventResult: LiveData<com.jayfm.dicodingevent.data.Result<List<ListEventsItem>>> = _eventResult

    init {
        getUpcomingEvents()
    }

    fun getUpcomingEvents() {
        viewModelScope.launch {
            repository.getUpcomingEvents().collect { result ->
                _eventResult.value = result
            }
        }
    }

    fun searchEvents(query: String) {
        viewModelScope.launch {
            // Note: Since searchEvents in repository hasn't been updated to Flow yet,
            // this part would need similar handling. Assuming standard call for now
            // until repository search function is also updated to Flow.
            try {
                val response = repository.searchEvents(query)
                _eventResult.value = com.jayfm.dicodingevent.data.Result.Success(response.listEvents)
            } catch (e: Exception) {
                _eventResult.value = com.jayfm.dicodingevent.data.Result.Error(e.message.toString())
            }
        }
    }
}
