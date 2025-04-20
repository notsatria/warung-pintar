package com.capstone.warungpintar.ui.dashboard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.capstone.warungpintar.R
import com.capstone.warungpintar.data.local.entities.NotificationType
import com.capstone.warungpintar.data.remote.model.response.DashboardResponse
import com.capstone.warungpintar.databinding.ActivityDashboardProductBinding
import com.capstone.warungpintar.ui.addproduct.AddProductInActivity
import com.capstone.warungpintar.ui.category.CategoryProductActivity
import com.capstone.warungpintar.ui.deleteproduct.DeleteProductOutActivity
import com.capstone.warungpintar.ui.history.ProductHistoryActivity
import com.capstone.warungpintar.ui.liststockproduct.ListStockProductActivity
import com.capstone.warungpintar.ui.newsproduct.ListNewsActivity
import com.capstone.warungpintar.ui.notification.NotificationActivity
import com.capstone.warungpintar.ui.report.ReportActivity
import com.capstone.warungpintar.ui.welcoming.WelcomeActivity
import com.capstone.warungpintar.worker.InstanceWorker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class DashboardProduct : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardProductBinding

    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTopBarAction()
        binding.setupAction()

        handlePermissions()

        if (viewModel.userEmail.isNotEmpty()) {
            binding.topAppBar.subtitle = viewModel.userEmail
        } else {
            Toast.makeText(this, "Something failed", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "onCreate: email is null or empty, cannot get dashboard data")
            signOut()
        }

        /* viewModel.resultRequest.observe(this) { result ->
             if (result != null) {
                 when (result) {
                     is ResultState.Loading -> {
                         showLoading(true)
                     }

                     is ResultState.Success -> {
                         val data = result.data
                         setDataView(data)
                         showLoading(false)
                     }

                     is ResultState.Error -> {
                         showLoading(false)
                         Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                         Log.d(TAG, "onCreate: error fetch data from API: ${result.error}")
                     }
                 }
             }
         }*/
    }

    private fun setDataView(data: DashboardResponse) {
        with(binding) {
            // store data
            topAppBar.title = data.storeData.storeName

            // stock data
            tvBarangmasuk.text = data.stockData.entryProduct.toString()
            tvBarangkeluar.text = data.stockData.exitProduct.toString()
            tvProduk.text = data.stockData.product.toString()
            tvLowstock.text = data.stockData.lowStock.toString()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setTopBarAction() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {

                R.id.action_notification -> {
                    startActivity(Intent(this@DashboardProduct, NotificationActivity::class.java))
                    true
                }

                R.id.action_logout -> {
                    showLogoutDialog()
                    true
                }

                else -> false
            }
        }
    }

    private fun signOut() {
        viewModel.logout()
        startActivity(Intent(this@DashboardProduct, WelcomeActivity::class.java))
        finish()
    }

    private fun showLogoutDialog() {
        val alertDialog = MaterialAlertDialogBuilder(this)
        alertDialog.setTitle("Keluar?")
            .setMessage("Apakah anda yakin ingin keluar dari aplikasi")
            .setPositiveButton("YES") { _, _ ->
                signOut()
            }
            .setNegativeButton("NO") { dialog, _ ->
                dialog.dismiss()
            }
        alertDialog.create().show()
    }

    private fun ActivityDashboardProductBinding.setupAction() {
        lifecycleScope.launch {
            launch {
                viewModel.productLength.collectLatest {
                    tvProduk.text = it.toString()
                }
            }

            launch {
                viewModel.productInLength.collectLatest {
                    tvBarangmasuk.text = it.toString()
                }
            }

            launch {
                viewModel.productOutLength.collectLatest {
                    tvBarangkeluar.text = it.toString()
                }
            }

            launch {
                viewModel.productsWithLowStockLength.collectLatest {
                    tvLowstock.text = it.toString()
                }
            }
        }

        btnBarangmasuk.setOnClickListener {
            val intent = Intent(this@DashboardProduct, AddProductInActivity::class.java)
            startActivity(intent)
        }

        btnBarangkeluar.setOnClickListener {
            val intent = Intent(this@DashboardProduct, DeleteProductOutActivity::class.java)
            startActivity(intent)
        }

        btnStock.setOnClickListener {
            val intent = Intent(this@DashboardProduct, ListStockProductActivity::class.java)
            startActivity(intent)
        }

        btnKategori.setOnClickListener {
            val intent = Intent(this@DashboardProduct, CategoryProductActivity::class.java)
            startActivity(intent)
        }

        btnHistory.setOnClickListener {
            val intent = Intent(this@DashboardProduct, ProductHistoryActivity::class.java)
            startActivity(intent)
        }

        btnLaporan.setOnClickListener {
            val intent = Intent(this@DashboardProduct, ReportActivity::class.java)
            startActivity(intent)
        }

        btnBerita.setOnClickListener {
            val intent = Intent(this@DashboardProduct, ListNewsActivity::class.java)
            startActivity(intent)
        }
    }

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // empty
            } else {
                handlePermissions()
            }
        }

    private fun handlePermissions() {
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                Timber.d("Notification permission granted")
            }

            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    companion object {
        private const val TAG = "DashboardProduct"
    }
}