package com.capstone.warungpintar.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.capstone.warungpintar.data.model.Product
import com.capstone.warungpintar.utils.toStrDate

data class ProductWithCategory(
    @Embedded val product: ProductEntity,

    @Relation(
        parentColumn = "kategoriId",
        entityColumn = "id"
    )
    val kategori: CategoryEntity
) {
    fun toProduct(): Product {
        return Product(
            productId = product.id,
            productName = product.nama,
            entryDate = product.tanggalMasuk.toStrDate(),
            productCategory = kategori.namaKategori,
            productQuantity = product.jumlah,
            lowStock = product.lowStock,
            codeStock = "",
            purchasePrice = product.hargaBeli,
            sellingPrice = product.hargaJual,
            imageUrl = product.imagePath,
            expiredDate = product.expired.toStrDate()
        )
    }

    fun toProductEntity(): ProductEntity {
        return ProductEntity(
            id = product.id,
            nama = product.nama,
            jumlah = product.jumlah,
            lowStock = product.lowStock,
            expired = product.expired,
            hargaBeli = product.hargaBeli,
            hargaJual = product.hargaJual,
            imagePath = product.imagePath,
            kategoriId = product.kategoriId,
            tanggalMasuk = product.tanggalMasuk
        )
    }
}
