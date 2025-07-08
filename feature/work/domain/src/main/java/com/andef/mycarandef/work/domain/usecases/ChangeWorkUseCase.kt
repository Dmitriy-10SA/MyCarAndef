package com.andef.mycarandef.work.domain.usecases

import com.andef.mycarandef.work.domain.repository.WorkRepository
import java.time.LocalDate
import javax.inject.Inject

class ChangeWorkUseCase @Inject constructor(private val repository: WorkRepository) {
    suspend operator fun invoke(
        id: Long,
        title: String,
        note: String?,
        mileage: Int,
        date: LocalDate
    ) = repository.changeWork(id, title, note, mileage, date)
}