package com.capstone.warungpintar.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.warungpintar.databinding.ItemCategoryProductRowBinding

class CategoryProductAdapter : ListAdapter<String, CategoryProductAdapter.CategoryViewHolder>(
    DIFF_CALLBACK
), Filterable {

    companion object {

        private const val TAG = "CategoryProductAdapter"

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }

    }

    private var fullCategoryNameList = listOf<String>()

    fun setData(list: List<String>) {
        fullCategoryNameList = list
        submitList(list)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults? {
                val filteredList = if (constraint.isNullOrEmpty()) {
                    fullCategoryNameList
                } else {
                    val filterPattern = constraint.toString().trim().lowercase()
                    fullCategoryNameList.filter {
                        it.lowercase()
                            .contains(filterPattern)
                    }
                }

                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(
                p0: CharSequence?,
                results: FilterResults?
            ) {
                submitList(results?.values as? List<String>)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryProductRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val productName = getItem(position)
        if (productName != null) {
            holder.bind(productName)
        }
    }

    class CategoryViewHolder(val binding: ItemCategoryProductRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(productName: String) {
            binding.tvTitleCategoryItem.text = productName
        }
    }
}