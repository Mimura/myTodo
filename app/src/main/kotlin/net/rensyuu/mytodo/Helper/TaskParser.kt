package net.rensyuu.mytodo.Helper

import net.rensyuu.mytodo.model.Task
import org.jetbrains.anko.db.MapRowParser

class TaskParser : MapRowParser<Task> {
    override fun parseRow(columns: Map<String, Any?>): Task {
        return Task(columns["id"] as Int, columns["name"] as String, columns["status"] as Int, columns["folderId"] as Int)
    }
}