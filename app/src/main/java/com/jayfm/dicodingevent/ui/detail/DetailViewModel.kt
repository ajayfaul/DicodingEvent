package com.jayfm.dicodingevent.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jayfm.dicodingevent.data.EventRepository
import com.jayfm.dicodingevent.data.remote.response.ListEventsItem
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: EventRepository) : ViewModel() {

    private val _eventResult = MutableLiveData<com.jayfm.dicodingevent.data.Result<com.jayfm.dicodingevent.data.remote.response.ListEventsItem>>()
    val eventResult: LiveData<com.jayfm.dicodingevent.data.Result<com.jayfm.dicodingevent.data.remote.response.ListEventsItem>> = _eventResult

    fun getDetailEvent(id: String) {
        viewModelScope.launch {
            repository.getDetailEvent(id).collect { result ->
                _eventResult.value = result
            }
        }
    }

    fun getFavoriteById(id: Int) = repository.getFavoriteById(id)

    fun insertFavorite(event: com.jayfm.dicodingevent.data.local.entity.FavoriteEventEntity) {
        viewModelScope.launch {
            repository.insertFavorite(event)
        }
    }

    fun deleteFavorite(event: com.jayfm.dicodingevent.data.local.entity.FavoriteEventEntity) {
        viewModelScope.launch {
            repository.deleteFavorite(event)
        }
    }
}
