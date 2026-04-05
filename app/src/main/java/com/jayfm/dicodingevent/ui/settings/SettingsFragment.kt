package com.jayfm.dicodingevent.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jayfm.dicodingevent.R
import com.jayfm.dicodingevent.databinding.FragmentSettingsBinding
import com.jayfm.dicodingevent.ui.ThemeViewModel
import com.jayfm.dicodingevent.ui.ViewModelFactory
import com.jayfm.dicodingevent.utils.ThemePreferences
import com.jayfm.dicodingevent.utils.dataStore

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
