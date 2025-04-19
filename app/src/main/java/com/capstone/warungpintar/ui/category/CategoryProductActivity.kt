package com.capstone.warungpintar.ui.category

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.warungpintar.data.ResultState
import com.capstone.warungpintar.databinding.ActivityCategoryProductBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryProductBinding
    private lateinit var adapter: CategoryProductAdapter

    private val viewModel: ListCategoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()

        viewModel.categoryNameList.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        val data = result.data
                        adapter.setData(data)
                        showLoading(false)
                    }

                    is ResultState.Error -> {
                        showMessage("Terjadi kegagalan, coba lagi nanti")
                        showLoading(false)
                    }
                }
            }
        }

        binding.topAppBar.setNavigationOnClickListener { _ ->
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setupViews() {
        adapter = CategoryProductAdapter()
        with(binding) {
            rvCategory.adapter = adapter
            rvCategory.layoutManager = LinearLayoutManager(this@CategoryProductActivity)

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    adapter.filter.filter(query)
                    return true
                }
            })
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this@CategoryProductActivity, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "CategoryProductActivity"
    }
}