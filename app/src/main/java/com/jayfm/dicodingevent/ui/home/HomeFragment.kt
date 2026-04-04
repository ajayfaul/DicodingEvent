package com.jayfm.dicodingevent.ui.home

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jayfm.dicodingevent.R
import com.jayfm.dicodingevent.databinding.FragmentHomeBinding
import com.jayfm.dicodingevent.di.Injection
import com.jayfm.dicodingevent.ui.ViewModelFactory
import com.jayfm.dicodingevent.ui.detail.DetailActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(Injection.provideRepository())
    }

    private lateinit var upcomingAdapter: EventHorizontalAdapter
    private lateinit var finishedAdapter: EventVerticalAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        upcomingAdapter = EventHorizontalAdapter { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_EVENT_ID, event.id.toString())
            startActivity(intent)
        }
        
        // Di Home, kategori bawah adalah Finished
        finishedAdapter = EventVerticalAdapter(isFinished = true) { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_EVENT_ID, event.id.toString())
            startActivity(intent)
        }

        binding.tvViewAllUpcoming.setOnClickListener {
            val navOptions = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setRestoreState(true)
                .setPopUpTo(findNavController().graph.startDestinationId, false, saveState = true)
                .build()
            
            findNavController().navigate(R.id.navigation_upcoming, null, navOptions)
        }

        setupRecyclerView()
        setupSearchBar()
        observeViewModel()
    }

    private fun setupSearchBar() {
        val editText = binding.searchLayout.editText
        editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchEvents(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupRecyclerView() {
        binding.rvUpcoming.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = upcomingAdapter
        }
        binding.rvFinished.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = finishedAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            upcomingAdapter.submitList(events)
        }

        viewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            finishedAdapter.submitList(events)
        }

        viewModel.isLoadingUpcoming.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarUpcoming.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.isLoadingFinished.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarFinished.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (message != null) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
