package com.capstone.warungpintar.ui.addproduct

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.capstone.warungpintar.R
import com.capstone.warungpintar.data.ResultState
import com.capstone.warungpintar.data.remote.model.request.ProductRequest
import com.capstone.warungpintar.databinding.ActivityAddProductInBinding
import com.capstone.warungpintar.databinding.DialogResultScannerLayoutBinding
import com.capstone.warungpintar.ui.addproduct.AddScannerActivity.Companion.CAMERAX_RESULT
import com.capstone.warungpintar.utils.ImageUtils
import com.capstone.warungpintar.utils.Validation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class AddProductInActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductInBinding
    private lateinit var bindingDialog: DialogResultScannerLayoutBinding
    private val cameraRequest = 1888
    private var currentImageUriForOCR: Uri? = null
    private var currentImageUriForProduct: Uri? = null
    private var expiredDate = ""

    private val viewModel: AddProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                cameraRequest
            )
        }

        binding.setupAction()

        viewModel.resultUpload.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        showMessage("Berhasil menambahkan barang")
                        showLoading(false)
                        finish()
                    }

                    is ResultState.Error -> {
                        showMessage("Gagal menambahkan barang")
                        showLoading(false)
                        Log.d(TAG, "onCreate: error fetch data from API: ${result.error}")
                    }
                }
            }
        }

        viewModel.resultOCR.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        showLoadingInDialog(true)
                    }

                    is ResultState.Success -> {
                        val expiredDate = result.data
                        showLoadingInDialog(false)
                        binding.tvResultscan.text = expiredDate
                    }

                    is ResultState.Error -> {
                        showMessage("Gagal memindai tanggal kadaluarsa")
                        showLoadingInDialog(false)
                        Log.d(TAG, "onCreate: error fetch data from API: ${result.error}")
                    }
                }
            }
        }

    }

    private fun ActivityAddProductInBinding.setupAction() {
        lifecycleScope.launch {
            viewModel.getAllProducts().collect {
                val adapter = ArrayAdapter(
                    this@AddProductInActivity,
                    android.R.layout.simple_list_item_1,
                    it.map { it.nama })
                namabaranglEditText.setAdapter(adapter)
            }
        }

        btnAddproductClose.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnAddGambar.setOnClickListener {
            checkCameraPermissionForImage()
        }

        kodestockEditTextLayout.setOnClickListener {
            showDialog()
        }

        tglmasuklEditText.setOnClickListener {
            showDateDialog { view, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"

                tglmasuklEditText.setText(selectedDate)
            }
        }

        btnUpload.setOnClickListener {
            uploadProduct()
        }

        kategoribrgliEditText.setOnClickListener {
            showKategoriListDialog()
        }
    }

    private fun uploadProduct() {
        val isImageNull = currentImageUriForProduct == null

        val namaBarang = binding.namabaranglEditText.text.toString().trim()
        val tglMasuk = binding.tglmasuklEditText.text.toString().trim()
        val jmlMasuk = binding.jmlmasuklEditText.text.toString().trim()
        val stokRendah = binding.lowstockEditText.text.toString().trim()
        val expiredDate = binding.tvResultscan.text.toString().trim()
        val hargaBeli = binding.hargabeliEditText.text.toString().trim()
        val hargaJual = binding.hargajualEditText.text.toString().trim()
        val kategori = binding.kategoribrgliEditText.text.toString().trim()

        if (isImageNull ||
            namaBarang.isEmpty() ||
            tglMasuk.isEmpty() ||
            jmlMasuk.isEmpty() ||
            stokRendah.isEmpty() ||
            expiredDate.isEmpty() ||
            hargaBeli.isEmpty() ||
            hargaJual.isEmpty() ||
            kategori.isEmpty()
        ) {
            showMessage("Masukan data dengan lengkap!")
        } else {
            val imageFile = ImageUtils.uriToFile(currentImageUriForProduct!!, this)

            Log.d(TAG, "uploadProduct: image file: $imageFile")

            viewModel.insertProductIn(
                namaBarang,
                tglMasuk,
                jmlMasuk.toInt(),
                stokRendah.toInt(),
                expiredDate,
                hargaBeli.toInt(),
                hargaJual.toInt(),
                imageFile.path
            )
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this@AddProductInActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showLoadingInDialog(isLoading: Boolean) {
        if (isLoading) {
            bindingDialog.progressBar.visibility = View.VISIBLE
        } else {
            bindingDialog.progressBar.visibility = View.GONE
        }
    }

    private fun checkCameraPermissionForImage() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openCameraForImage()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                cameraRequest
            )
        }
    }

    private fun openCameraForImage() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        currentImageUriForProduct = ImageUtils.getImageUri(this)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentImageUriForProduct)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            launcherTakePicture.launch(takePictureIntent)
        }
    }

    private val launcherTakePicture =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                currentImageUriForProduct?.let { uri ->
                    Log.d(TAG, "uri: $uri")
                    binding.btnAddGambar.visibility = View.INVISIBLE
                    binding.ivProductImage.setImageURI(uri)
                } ?: Toast.makeText(
                    this@AddProductInActivity,
                    "Terjadi kegagalan",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun showDialog() {
        val alertDialog = MaterialAlertDialogBuilder(this)
        alertDialog.setTitle("Informasi!")
            .setMessage(getString(R.string.ocr_information_dialog))
            .setNeutralButton("Masukan Manual") { _, _ ->
                showDialogResult(null)
            }
            .setPositiveButton("Lanjutkan") { _, _ ->
                startCameraXForScanning()
            }
        alertDialog.create().show()
    }

    private fun startCameraXForScanning() {
        val intent = Intent(this, AddScannerActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUriForOCR =
                it.data?.getStringExtra(AddScannerActivity.EXTRA_CAMERAX_IMAGE)?.toUri()

            if (currentImageUriForOCR != null) {
                showDialogResult(currentImageUriForOCR!!)
                Log.d(TAG, "image uri: ${currentImageUriForOCR.toString()}")
            } else {
                Toast.makeText(
                    this,
                    "Gagal mengambil atau menampilkan gambar",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showDialogResult(uri: Uri?) {
        bindingDialog = DialogResultScannerLayoutBinding.inflate(layoutInflater)
        val buttonScan = bindingDialog.btnScanOcr
        val buttonResult = bindingDialog.btnSave
        val etExpiredDate = bindingDialog.etExpiredDate

        val alertDialog = MaterialAlertDialogBuilder(this)
            .setView(bindingDialog.root)
            .create()

        uri?.let {
            bindingDialog.ivResultScan.setImageURI(it)
        }

        etExpiredDate.setOnClickListener {
            showDateDialog { view, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"

                etExpiredDate.setText(selectedDate)
            }
        }

        buttonScan.setOnClickListener {
            currentImageUriForOCR?.let {
                val imageFile = ImageUtils.uriToFile(it, this)
                viewModel.performOCR(imageFile)
            } ?: Toast.makeText(
                this,
                "Gambar kosong, Masukan tanggal secara manual",
                Toast.LENGTH_SHORT
            ).show()
        }


        buttonResult.setOnClickListener {
            val isExpiredDateValid = Validation.validateIsNotEmpty(
                "Tanggal Kadaluarsa",
                bindingDialog.layoutExpiredDate,
                bindingDialog.etExpiredDate
            )

            if (isExpiredDateValid) {
                expiredDate = bindingDialog.etExpiredDate.text.toString().trim()
                Log.d(TAG, "showDialogResult: expired date $expiredDate")
                binding.tvResultscan.text = expiredDate
                alertDialog.dismiss()
            } else {
                Toast.makeText(this, "Masukan tanggal kadaluarsa", Toast.LENGTH_SHORT).show()
            }
        }

        alertDialog.show()
    }

    private fun showKategoriListDialog() {
        val dialog = MaterialAlertDialogBuilder(this@AddProductInActivity).apply {
            setTitle("Kategori")
            setItems(viewModel.categories.map { it.namaKategori }.toTypedArray()) { _, index ->
                binding.kategoribrgliEditText.setText(viewModel.categories[index].namaKategori)
                viewModel.selectedCategory = viewModel.categories[index]
            }
        }
        dialog.show()
    }

    private fun showDateDialog(onDateSetListener: DatePickerDialog.OnDateSetListener) {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            onDateSetListener,
            year,
            month,
            dayOfMonth
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis()

        datePickerDialog.show()
    }

    companion object {
        private const val TAG = "AddProductInActivity"
    }
}