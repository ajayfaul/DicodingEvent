package com.jayfm.dicodingevent.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jayfm.dicodingevent.data.EventRepository
import com.jayfm.dicodingevent.ui.detail.DetailViewModel
import com.jayfm.dicodingevent.ui.favorite.FavoriteViewModel
import com.jayfm.dicodingevent.ui.finished.FinishedViewModel
import com.jayfm.dicodingevent.ui.home.HomeViewModel
import com.jayfm.dicodingevent.ui.upcoming.UpcomingViewModel
import com.jayfm.dicodingevent.utils.ThemePreferences

class ViewModelFactory(
    private val eventRepository: EventRepository? = null,
    private val themePreferences: ThemePreferences? = null
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(eventRepository!!) as T
            }
            modelClass.isAssignableFrom(UpcomingViewModel::class.java) -> {
                UpcomingViewModel(eventRepository!!) as T
            }
            modelClass.isAssignableFrom(FinishedViewModel::class.java) -> {
                FinishedViewModel(eventRepository!!) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(eventRepository!!) as T
            }
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(eventRepository!!) as T
            }
            modelClass.isAssignableFrom(ThemeViewModel::class.java) -> {
                ThemeViewModel(themePreferences!!) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: EventRepository): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(context)
            }.also { instance = it }
            
        fun getThemeInstance(pref: ThemePreferences): ViewModelFactory =
            ViewModelFactory(themePreferences = pref)
    }
}
