package net.rensyuu.mytodo.Helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class MyDatabaseOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "MyTodo", null, 1) {
    companion object {
        private var instance: MyDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): MyDatabaseOpenHelper {
            if (instance == null) {
                instance = MyDatabaseOpenHelper(ctx.getApplicationContext())
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Here you create tables
        db?.createTable(TaskTableParam.TABLE_NAME, true,
                TaskTableParam.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                TaskTableParam.NAME to TEXT,
                TaskTableParam.STATUS to INTEGER,
                TaskTableParam.FOLDER_ID to INTEGER

        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable("User", true)
    }

    object TaskTableParam {
        val TABLE_NAME = "Task"
        val ID = "id"
        val NAME = "name"
        val STATUS = "status"
        val FOLDER_ID = "folderId"
    }
}

// Access property for Context
val Context.database: MyDatabaseOpenHelper
    get() = MyDatabaseOpenHelper.getInstance(getApplicationContext())
