package com.andef.mycarandef.work.data.mapper

import com.andef.mycarandef.utils.toInt
import com.andef.mycarandef.utils.toLocalDate
import com.andef.mycarandef.work.data.dbo.WorkDbo
import com.andef.mycarandef.work.domain.entities.Work
import javax.inject.Inject

class WorkMapper @Inject constructor() {
    fun map(work: Work): WorkDbo = WorkDbo(
        id = work.id,
        title = work.title,
        note = work.note,
        mileage = work.mileage,
        date = work.date.toInt(),
        carId = work.carId
    )

    fun map(workDbo: WorkDbo): Work = Work(
        id = workDbo.id,
        title = workDbo.title,
        note = workDbo.note,
        mileage = workDbo.mileage,
        date = workDbo.date.toLocalDate(),
        carId = workDbo.carId
    )
}