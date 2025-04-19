package com.capstone.warungpintar.ui.liststockproduct

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.warungpintar.data.ResultState
import com.capstone.warungpintar.databinding.ActivityListStockProductBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListStockProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListStockProductBinding
    private lateinit var adapter: ListStockAdapter

    private val viewModel: ListStockViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStockProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.setupView()
        viewModel.getListProduct()

        viewModel.resultList.observe(this) { result ->
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
                        Toast.makeText(
                            this@ListStockProductActivity,
                            "Terjadi kegagalan, coba lagi",
                            Toast.LENGTH_SHORT
                        ).show()
                        showLoading(false)
                        Log.d(TAG, "onCreate: error fetch data from API: ${result.error}")
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

    private fun ActivityListStockProductBinding.setupView() {
        adapter = ListStockAdapter()
        val recyclerView = rvListStockProduct
        recyclerView.layoutManager = LinearLayoutManager(this@ListStockProductActivity)
        recyclerView.adapter = adapter

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

    companion object {
        private const val TAG = "ListStockProductActivit"
    }
}