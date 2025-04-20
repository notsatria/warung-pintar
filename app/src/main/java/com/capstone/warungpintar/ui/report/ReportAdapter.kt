package com.capstone.warungpintar.ui.report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.warungpintar.data.remote.model.response.ReportResponse
import com.capstone.warungpintar.databinding.ItemReportRowBinding
import com.capstone.warungpintar.utils.toStrDate

class ReportAdapter : ListAdapter<ReportItem, ReportAdapter.ReportViewHolder>(DIFF_CALLBACK) {

    companion object {

        private const val TAG = "ReportAdapter"

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ReportItem>() {
            override fun areItemsTheSame(
                oldItem: ReportItem,
                newItem: ReportItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ReportItem,
                newItem: ReportItem
            ): Boolean {
                return oldItem == newItem
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val binding = ItemReportRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReportViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class ReportViewHolder(val binding: ItemReportRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ReportItem) {
            val purchasePrice = data.hargaBeli
            val sellingPrice = data.hargaJual

            with(binding) {
                tvProductNameRow.text = data.namaBarang
                tvPurchasePriceRowValue.text = "Rp.$purchasePrice"
                tvSellingPriceRowValue.text = "Rp. $sellingPrice"
                tvExitDateRow.text = "Tanggal keluar: ${data.tanggalKeluar.toStrDate()}"

                tvProfitProductRow.text = "Rp. ${data.total}"
            }
        }
    }
}

data class ReportItem(
    val id: Int,
    val namaBarang: String,
    val hargaJual: Int,
    val hargaBeli: Int,
    val total: Int,
    val jumlah: Int,
    val tanggalKeluar: Long,
)