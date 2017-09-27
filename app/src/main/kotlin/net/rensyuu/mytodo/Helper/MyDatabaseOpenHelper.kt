package net.rensyuu.mytodo.Helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*


class appDbHelpler(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "MyDatabase", null, 1) {

    companion object {
        val DB_NAME = "person.db"
        val DB_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable("Person", true,
                "id" to INTEGER + PRIMARY_KEY + UNIQUE,
                "name" to TEXT,
                "status" to INTEGER,
                "folderId" to INTEGER)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//        db!!.dropTable(PersonTable.Name, true)
//        onCreate(db)
    }
}

// Access property for Context
val Context.database: appDbHelpler
    get() = appDbHelpler(applicationContext)
