package net.rensyuu.mytodo.Helper

import net.rensyuu.mytodo.Helper.MyDatabaseOpenHelper.TaskTableParam
import net.rensyuu.mytodo.model.Task
import org.jetbrains.anko.db.MapRowParser

class TaskParser : MapRowParser<Task> {
    override fun parseRow(columns: Map<String, Any?>): Task {
        return Task(columns[TaskTableParam.ID].toString().toInt(), columns[TaskTableParam.NAME].toString(), columns[TaskTableParam.STATUS].toString().toInt(), columns[TaskTableParam.FOLDER_ID].toString().toInt())
    }
}

