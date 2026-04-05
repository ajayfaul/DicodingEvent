package com.jayfm.dicodingevent.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jayfm.dicodingevent.data.EventRepository
import com.jayfm.dicodingevent.data.remote.response.ListEventsItem
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: EventRepository) : ViewModel() {

    private val _event = MutableLiveData<ListEventsItem>()
    val event: LiveData<ListEventsItem> = _event

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun getDetailEvent(id: String) {
        _isLoading.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            try {
                val response = repository.getDetailEvent(id)
                _event.value = response.event
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
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
