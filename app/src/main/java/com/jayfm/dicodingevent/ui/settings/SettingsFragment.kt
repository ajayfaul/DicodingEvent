package com.jayfm.dicodingevent.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.work.*
import com.jayfm.dicodingevent.R
import com.jayfm.dicodingevent.databinding.FragmentSettingsBinding
import com.jayfm.dicodingevent.ui.ThemeViewModel
import com.jayfm.dicodingevent.ui.ViewModelFactory
import com.jayfm.dicodingevent.utils.ThemePreferences
import com.jayfm.dicodingevent.utils.dataStore
import com.jayfm.dicodingevent.worker.DailyReminderWorker
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)

        val pref = ThemePreferences(requireContext().dataStore)
        val themeViewModel = ViewModelProvider(this, ViewModelFactory.getThemeInstance(pref))[ThemeViewModel::class.java]

        themeViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            binding.switchTheme.isChecked = isDarkModeActive
        }

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            themeViewModel.saveThemeSetting(isChecked)
        }

        pref.getReminderSetting().asLiveData().observe(viewLifecycleOwner) { isReminderActive ->
            binding.switchReminder.isChecked = isReminderActive
        }

        binding.switchReminder.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isPressed) {
                lifecycleScope.launch {
                    pref.saveReminderSetting(isChecked)
                    if (isChecked) {
                        scheduleReminder()
                        Toast.makeText(
                            context,
                            "Daily reminder diaktifkan",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        WorkManager.getInstance(requireContext()).cancelUniqueWork("daily_reminder")
                        Toast.makeText(
                            context,
                            "Daily reminder dinonaktifkan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun scheduleReminder() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        
        val periodicWork = PeriodicWorkRequestBuilder<DailyReminderWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()
            
        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            "daily_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWork
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
