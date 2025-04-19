package com.capstone.warungpintar.ui.deleteproduct

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.warungpintar.data.ResultState
import com.capstone.warungpintar.data.remote.model.request.DeleteProductRequest
import com.capstone.warungpintar.databinding.ActivityDeleteProductOutBinding
import com.capstone.warungpintar.utils.Validation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class DeleteProductOutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeleteProductOutBinding

    private val viewModel: DeleteProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteProductOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.setupActions()

        viewModel.productOutState.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        val data = result.data
                        showLoading(false)
                        showMessage(data)
                        finish()
                    }

                    is ResultState.Error -> {
                        showMessage("Terjadi kegagalan, coba lagi nanti")
                        showLoading(false)
                        Log.d(TAG, "onCreate: error fetch data from API: ${result.error}")
                    }
                }
            }
        }
    }

    private fun ActivityDeleteProductOutBinding.setupActions() {
        btnAddproductClose.setOnClickListener { _ ->
            onBackPressedDispatcher.onBackPressed()
        }

        tglkeluarEditText.setOnClickListener {
            val year = Calendar.getInstance().get(Calendar.YEAR)
            val month = Calendar.getInstance().get(Calendar.MONTH)
            val dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this@DeleteProductOutActivity,
                { view, year, month, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${month + 1}/$year"

                    tglkeluarEditText.setText(selectedDate)
                },
                year,
                month,
                dayOfMonth
            )

            datePickerDialog.datePicker.minDate = System.currentTimeMillis()

            datePickerDialog.show()
        }

        btnDeleteProduct.setOnClickListener { _ ->
            if (validate()) {
                deleteProduct()
            } else {
                showMessage("Data tidak boleh ada yang kosong")
            }
        }

        namabaranglEditText.setOnClickListener {
            showProductListDialog()
        }
    }

    private fun showProductListDialog() {
        val productItems = viewModel.productList.map { it.nama }.toTypedArray()
        val dialog = MaterialAlertDialogBuilder(this).apply {
            setTitle("Daftar Barang")
            setItems(productItems) { _, index ->
                val selectedItem = productItems[index]
                binding.namabaranglEditText.setText(selectedItem)
                viewModel.selectedProduct = viewModel.productList[index]
            }
        }

        dialog.show()
    }

    private fun deleteProduct() {
        with(binding) {
            val exitDate = tglkeluarEditText.text.toString().trim()
            val quantity = jmlmasuklEditText.text.toString().trim().toInt()
            val sellingPrice = hargajualEditText.text.toString().trim().toInt()

            viewModel.insertProductOut(
                hargaJual = sellingPrice,
                jumlah = quantity,
                tglKeluar = exitDate
            )
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun validate(): Boolean {
        with(binding) {
            val isValidProductName = Validation.validateIsNotEmpty(
                "Barang",
                namabarangEditTextLayout,
                namabaranglEditText
            )

            val isValidExitDate = Validation.validateIsNotEmpty(
                "Tanggal Keluar",
                tglkeluarEditTextLayout,
                tglkeluarEditText
            )

            val isValidQuantity = Validation.validateIsNotEmpty(
                "Jumlah Barang",
                jmlmasukEditTextLayout,
                jmlmasuklEditText
            )

            val isValidPrice = Validation.validateIsNotEmpty(
                "Harga jual",
                hargajualEditTextLayout,
                hargajualEditText
            )

            return isValidProductName && isValidExitDate && isValidQuantity && isValidPrice
        }
    }

    companion object {
        private const val TAG = "DeleteProductOutActivit"
    }
}