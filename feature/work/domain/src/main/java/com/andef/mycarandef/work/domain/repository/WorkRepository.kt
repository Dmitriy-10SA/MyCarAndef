package com.andef.mycarandef.work.domain.repository

import com.andef.mycarandef.work.domain.entities.Work
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface WorkRepository {
    suspend fun getAllWorksAsList(): List<Work>
    suspend fun addWork(work: Work)
    suspend fun changeWork(
        id: Long,
        title: String,
        note: String?,
        mileage: Int,
        date: LocalDate
    )

    suspend fun removeWork(id: Long)
    suspend fun getWorkById(id: Long): Work
    fun getWorksByCarId(carId: Long): Flow<List<Work>>
}