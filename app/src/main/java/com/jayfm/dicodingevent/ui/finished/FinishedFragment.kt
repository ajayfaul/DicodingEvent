package com.jayfm.dicodingevent.ui.finished

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jayfm.dicodingevent.databinding.FragmentFinishedBinding
import com.jayfm.dicodingevent.di.Injection
import com.jayfm.dicodingevent.ui.ViewModelFactory
import com.jayfm.dicodingevent.ui.detail.DetailActivity
import com.jayfm.dicodingevent.ui.home.EventVerticalAdapter

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FinishedViewModel by viewModels {
        ViewModelFactory.getInstance(Injection.provideRepository(requireContext()))
    }

    private lateinit var adapter: EventVerticalAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = EventVerticalAdapter(isFinished = true) { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_EVENT_ID, event.id.toString())
            startActivity(intent)
        }

        setupRecyclerView()
        setupSearch()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.rvFinished.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFinished.adapter = adapter
    }

    private fun setupSearch() {
        binding.searchLayout.editText?.addTextChangedListener { text ->
            val query = text.toString()
            if (query.isNotEmpty()) {
                viewModel.searchEvents(query)
            } else {
                viewModel.getFinishedEvents()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.eventResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is com.jayfm.dicodingevent.data.Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is com.jayfm.dicodingevent.data.Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.submitList(result.data)
                }
                is com.jayfm.dicodingevent.data.Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
