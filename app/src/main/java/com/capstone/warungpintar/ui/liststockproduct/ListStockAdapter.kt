package com.capstone.warungpintar.ui.liststockproduct

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.warungpintar.R
import com.capstone.warungpintar.data.model.Product
import com.capstone.warungpintar.databinding.ItemStockProductRowBinding
import com.capstone.warungpintar.ui.detailproduct.DetailProductActivity
import java.io.File

class ListStockAdapter :
    ListAdapter<Product, ListStockAdapter.ListStockViewHolder>(DIFF_CALLBACK), Filterable {

    companion object {

        private const val TAG = "ListStockAdapter"

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.productId == newItem.productId
            }

            override fun areContentsTheSame(
                oldItem: Product,
                newItem: Product
            ): Boolean {
                return oldItem == newItem
            }
        }

    }

    private var productListFull = listOf<Product>()

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults? {
                val filteredList = if (constraint.isNullOrEmpty()) {
                    productListFull
                } else {
                    val filterPattern = constraint.toString().trim().lowercase()
                    productListFull.filter {
                        it.productName.lowercase()
                            .contains(filterPattern) || it.productCategory.lowercase()
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
                submitList(results?.values as? List<Product>)
            }
        }
    }

    fun setData(products: List<Product>) {
        productListFull = products
        submitList(products)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListStockViewHolder {
        val binding = ItemStockProductRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ListStockViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ListStockViewHolder, position: Int) {
        val product = getItem(position)
        if (product != null) {
            holder.bind(product)
        }
    }

    inner class ListStockViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgProduct = itemView.findViewById<ImageView>(R.id.img_stock_product_row)
        private val tvProductName = itemView.findViewById<TextView>(R.id.tv_product_name_row)
        private val tvSalePrice = itemView.findViewById<TextView>(R.id.tv_sale_price_row_value)
        private val tvBuyPrice = itemView.findViewById<TextView>(R.id.tv_buy_price_row_value)
        private val tvTotalStock = itemView.findViewById<TextView>(R.id.tv_total_product_row_value)
        private val tvEntryDate = itemView.findViewById<TextView>(R.id.tv_entry_date_row_value)

        fun bind(product: Product) {
            val image = File(product.imageUrl)
            Glide.with(itemView)
                .load(image)
                .into(imgProduct)
            tvProductName.text = product.productName
            tvSalePrice.text = "Rp.${product.sellingPrice}"
            tvBuyPrice.text = "Rp.${product.purchasePrice}"
            tvEntryDate.text = product.entryDate
            tvTotalStock.text = product.productQuantity.toString()

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailProductActivity::class.java)
                intent.putExtra(DetailProductActivity.EXTRA_PRODUCT_DETAIL, product)
                itemView.context.startActivity(intent)
            }
        }
    }
}