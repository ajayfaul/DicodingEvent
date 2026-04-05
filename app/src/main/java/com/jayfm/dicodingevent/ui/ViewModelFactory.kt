package com.jayfm.dicodingevent.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jayfm.dicodingevent.data.EventRepository
import com.jayfm.dicodingevent.ui.detail.DetailViewModel
import com.jayfm.dicodingevent.ui.finished.FinishedViewModel
import com.jayfm.dicodingevent.ui.home.HomeViewModel
import com.jayfm.dicodingevent.ui.upcoming.UpcomingViewModel

class ViewModelFactory(private val repository: EventRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(UpcomingViewModel::class.java) -> {
                UpcomingViewModel(repository) as T
            }
            modelClass.isAssignableFrom(FinishedViewModel::class.java) -> {
                FinishedViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }
            modelClass.isAssignableFrom(com.jayfm.dicodingevent.ui.favorite.FavoriteViewModel::class.java) -> {
                com.jayfm.dicodingevent.ui.favorite.FavoriteViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(repository: EventRepository): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(repository)
            }.also { instance = it }
    }
}
