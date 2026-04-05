package com.jayfm.dicodingevent.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jayfm.dicodingevent.data.local.entity.FavoriteEventEntity
import com.jayfm.dicodingevent.databinding.ItemEventVerticalBinding

class FavoriteAdapter(
    private val onItemClick: (FavoriteEventEntity) -> Unit
) : ListAdapter<FavoriteEventEntity, FavoriteAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemEventVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
        holder.itemView.setOnClickListener {
            onItemClick(event)
        }
    }

    class MyViewHolder(private val binding: ItemEventVerticalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: FavoriteEventEntity) {
            binding.tvEventName.text = event.name
            binding.tvOwnerName.text = event.cityName
            
            binding.cardStatus.visibility = android.view.View.VISIBLE
            val isFinished = com.jayfm.dicodingevent.utils.DateUtils.isEventFinished(event.beginTime)
            binding.tvStatus.text = if (isFinished) 
                binding.root.context.getString(com.jayfm.dicodingevent.R.string.completed)
            else 
                binding.root.context.getString(com.jayfm.dicodingevent.R.string.upcoming)

            Glide.with(binding.ivEventLogo.context)
                .load(event.imageLogo)
                .into(binding.ivEventLogo)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteEventEntity>() {
            override fun areItemsTheSame(oldItem: FavoriteEventEntity, newItem: FavoriteEventEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FavoriteEventEntity, newItem: FavoriteEventEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}
