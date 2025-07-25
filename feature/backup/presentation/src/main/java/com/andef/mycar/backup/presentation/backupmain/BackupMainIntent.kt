package com.andef.mycar.backup.presentation.backupmain

sealed class BackupMainIntent {
    data class SaveData(
        val onSuccess: (String) -> Unit,
        val onError: (String) -> Unit
    ) : BackupMainIntent()

    data class RestoreDate(
        val onSuccess: () -> Unit,
        val onError: (String) -> Unit
    ) : BackupMainIntent()
}