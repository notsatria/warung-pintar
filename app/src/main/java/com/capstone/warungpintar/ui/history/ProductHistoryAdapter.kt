package com.capstone.warungpintar.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.warungpintar.R
import com.capstone.warungpintar.data.remote.model.response.HistoryResponse
import com.capstone.warungpintar.databinding.ItemHistoryProductRowBinding
import com.capstone.warungpintar.utils.toStrDate

class ProductHistoryAdapter :
    ListAdapter<HistoryItem, ProductHistoryAdapter.ProductHistoryViewHolder>(DIFF_CALLBACK) {

    companion object {

        private const val TAG = "ProductHistoryAdapter"

        private val DIFF_CALLBACK: DiffUtil.ItemCallback<HistoryItem> =
            object : DiffUtil.ItemCallback<HistoryItem>() {
                override fun areItemsTheSame(
                    oldItem: HistoryItem,
                    newItem: HistoryItem
                ): Boolean {
                    return oldItem.historyId == newItem.historyId
                }

                override fun areContentsTheSame(
                    oldItem: HistoryItem,
                    newItem: HistoryItem
                ): Boolean {
                    return oldItem == newItem
                }

            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHistoryViewHolder {
        val binding = ItemHistoryProductRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductHistoryViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class ProductHistoryViewHolder(val binding: ItemHistoryProductRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: HistoryItem) {
            if (data.tipe == HistoryType.KELUAR) {
                with(binding) {
                    imgHistoryProduct.setImageResource(R.drawable.img_product_out)
                    tvTitleHistoryItem.text = data.namaBarang
                    tvPriceItem.text = "Harga Jual Rp.${data.harga}"
                    tvDateItem.text = "Tanggal Keluar ${data.tanggal.toStrDate()}"
                    tvAmountItem.text = data.jumlah.toString()
                }
            } else {
                with(binding) {
                    imgHistoryProduct.setImageResource(R.drawable.img_product_in)
                    tvTitleHistoryItem.text = data.namaBarang
                    tvPriceItem.text = "Harga Beli Rp.${data.harga}"
                    tvDateItem.text = "Tanggal Masuk ${data.tanggal.toStrDate()}"
                    tvAmountItem.text = data.jumlah.toString()
                }
            }
        }

    }
}

data class HistoryItem(
    val historyId: Int,
    val namaBarang: String,
    val harga: String,
    val jumlah: Int,
    val tanggal: Long,
    val tipe: HistoryType
)

enum class HistoryType {
    MASUK,
    KELUAR
}
