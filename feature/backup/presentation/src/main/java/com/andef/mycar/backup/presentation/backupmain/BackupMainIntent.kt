package com.andef.mycar.backup.presentation.backupmain

import com.andef.mycar.backup.domain.BackupData

sealed class BackupMainIntent {
    data class SaveData(
        val onSuccess: (BackupData) -> Unit,
        val onError: (String) -> Unit
    ) : BackupMainIntent()

    data class RestoreDate(
        val data: BackupData,
        val onSuccess: (String) -> Unit,
        val onError: (String) -> Unit
    ) : BackupMainIntent()
}