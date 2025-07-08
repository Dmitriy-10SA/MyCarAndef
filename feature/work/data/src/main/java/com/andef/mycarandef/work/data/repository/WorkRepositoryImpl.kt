package com.andef.mycarandef.work.data.repository

import com.andef.mycarandef.work.data.dao.WorkDao
import com.andef.mycarandef.work.data.mapper.WorkMapper
import com.andef.mycarandef.work.domain.entities.Work
import com.andef.mycarandef.work.domain.repository.WorkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class WorkRepositoryImpl @Inject constructor(
    private val workMapper: WorkMapper,
    private val workDao: WorkDao
) : WorkRepository {
    override suspend fun addWork(work: Work) {
        workDao.addWork(workMapper.map(work))
    }

    override suspend fun changeWork(
        id: Long,
        title: String,
        note: String?,
        mileage: Int,
        date: LocalDate,
    ) {
        workDao.changeWork(id, title, note, mileage, date)
    }

    override suspend fun removeWork(id: Long) {
        workDao.removeWork(id)
    }

    override suspend fun getWorkById(id: Long): Work {
        return workMapper.map(workDao.getWorkById(id))
    }

    override fun getWorks(): Flow<List<Work>> {
        return workDao.getWorks().map { worksDbo ->
            worksDbo.map { workDbo ->
                workMapper.map(workDbo)
            }
        }
    }
}