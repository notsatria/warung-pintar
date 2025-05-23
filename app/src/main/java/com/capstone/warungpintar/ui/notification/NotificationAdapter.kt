package com.capstone.warungpintar.ui.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.warungpintar.R
import com.capstone.warungpintar.data.local.entities.NotificationEntity
import com.capstone.warungpintar.data.local.entities.NotificationType
import com.capstone.warungpintar.databinding.ItemNotificationRowBinding

class NotificationAdapter :
    ListAdapter<NotificationEntity, NotificationAdapter.NotificationViewHolder>(
        DIFF_CALLBACK
    ) {

    companion object {

        private const val TAG = "NotificationAdapter"

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NotificationEntity>() {
            override fun areItemsTheSame(
                oldItem: NotificationEntity,
                newItem: NotificationEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: NotificationEntity,
                newItem: NotificationEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemNotificationRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NotificationViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val message = getItem(position)
        if (message != null) {
            holder.bind(message)
        }
    }

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(message: NotificationEntity) {
            if (message.tipe == NotificationType.LOW_STOCK.toString()) {
                itemView.findViewById<TextView>(R.id.tv_title_notification_item).text =
                    message.judul
                itemView.findViewById<TextView>(R.id.tv_description_notification_item).text =
                    message.isi
                itemView.findViewById<ImageView>(R.id.item_notification_img)
                    .setImageResource(R.drawable.img_notif_lows_stock)
            } else {
                itemView.findViewById<TextView>(R.id.tv_title_notification_item).text =
                    message.judul
                itemView.findViewById<TextView>(R.id.tv_description_notification_item).text =
                    message.isi
                itemView.findViewById<ImageView>(R.id.item_notification_img)
                    .setImageResource(R.drawable.img_notif_expired_stock)
            }
        }
    }
}