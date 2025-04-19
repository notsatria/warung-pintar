package com.capstone.warungpintar.data.repository

import com.capstone.warungpintar.data.local.entities.CategoryDao
import com.capstone.warungpintar.data.local.entities.ProductDao
import com.capstone.warungpintar.data.local.entities.ProductEntity
import com.capstone.warungpintar.data.local.entities.ProductInDao
import com.capstone.warungpintar.data.local.entities.ProductInEntity
import com.capstone.warungpintar.data.local.entities.ProductOutDao
import com.capstone.warungpintar.data.local.entities.ProductOutEntity
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productDao: ProductDao,
    private val productInDao: ProductInDao,
    private val productOutDao: ProductOutDao,
    private val categoryDao: CategoryDao
) {

    suspend fun insertProduct(product: ProductEntity) = productDao.insert(product)

    suspend fun insertProductIn(productIn: ProductInEntity) =
        productInDao.insert(productIn)

    suspend fun insertProductOut(productOut: ProductOutEntity) = productOutDao.insert(productOut)

    fun getCategories() = categoryDao.getAllKategori()
}