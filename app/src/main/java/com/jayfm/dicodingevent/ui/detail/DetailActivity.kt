package com.jayfm.dicodingevent.ui.detail

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.jayfm.dicodingevent.R
import com.jayfm.dicodingevent.data.remote.response.ListEventsItem
import com.jayfm.dicodingevent.databinding.ActivityDetailBinding
import com.jayfm.dicodingevent.di.Injection
import com.jayfm.dicodingevent.ui.ViewModelFactory
import com.jayfm.dicodingevent.utils.DateUtils

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance(Injection.provideRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventId = intent.getStringExtra(EXTRA_EVENT_ID)
        if (eventId != null && viewModel.event.value == null) {
            viewModel.getDetailEvent(eventId)
        }

        binding.btnBack.setOnClickListener { finish() }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.event.observe(this) { event ->
            displayDetail(event)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(this) { message ->
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayDetail(event: ListEventsItem) {
        binding.tvEventName.text = event.name
        binding.tvOwnerName.text = event.ownerName
        
        val startTime = getString(R.string.start_time, DateUtils.formatToReadableDate(event.beginTime))
        val endTime = getString(R.string.end_time, DateUtils.formatToReadableDate(event.endTime))
        binding.tvTime.text = getString(R.string.event_schedule, startTime, endTime)

        val remaining = event.quota - event.registrants
        binding.tvQuota.text = getString(R.string.quota_display, remaining, event.quota)

        binding.tvDescription.text = Html.fromHtml(event.description, Html.FROM_HTML_MODE_COMPACT)

        Glide.with(this)
            .load(event.mediaCover)
            .into(binding.ivCover)

        binding.btnRegister.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, event.link.toUri())
            startActivity(intent)
        }
    }

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
    }
}
