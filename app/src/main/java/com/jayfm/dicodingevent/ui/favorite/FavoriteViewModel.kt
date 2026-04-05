package com.jayfm.dicodingevent.ui.favorite

import androidx.lifecycle.ViewModel
import com.jayfm.dicodingevent.data.EventRepository

class FavoriteViewModel(private val repository: EventRepository) : ViewModel() {
    fun getFavoriteEvents() = repository.getFavoriteEvents()
}
