package com.jayfm.dicodingevent.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jayfm.dicodingevent.R
import com.jayfm.dicodingevent.data.remote.response.ListEventsItem
import com.jayfm.dicodingevent.databinding.ItemEventVerticalBinding

class EventVerticalAdapter(
    private val isFinished: Boolean = true,
    private val onItemClick: (ListEventsItem) -> Unit
) : ListAdapter<ListEventsItem, EventVerticalAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemEventVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event, isFinished)
        holder.itemView.setOnClickListener {
            onItemClick(event)
        }
    }

    class MyViewHolder(private val binding: ItemEventVerticalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem, isFinished: Boolean) {
            binding.tvEventName.text = event.name
            binding.tvOwnerName.text = event.ownerName

            if (isFinished) {
                binding.cardStatus.visibility = View.VISIBLE
                binding.tvStatus.text = binding.root.context.getString(R.string.completed)
            } else {
                // Sembunyikan atau ganti teks menjadi UPCOMING jika diinginkan
                binding.cardStatus.visibility = View.VISIBLE
                binding.tvStatus.text = binding.root.context.getString(R.string.upcoming)
            }

            Glide.with(binding.ivEventLogo.context)
                .load(event.imageLogo)
                .into(binding.ivEventLogo)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
