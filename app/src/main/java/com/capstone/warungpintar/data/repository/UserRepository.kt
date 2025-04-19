package com.capstone.warungpintar.data.repository

import com.capstone.warungpintar.data.local.entities.UserDao
import com.capstone.warungpintar.data.local.entities.UserEntity
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDao: UserDao) {

    suspend fun register(obj: UserEntity) = userDao.insert(obj)

    suspend fun login(email: String, password: String): UserEntity? = userDao.login(email, password)
}