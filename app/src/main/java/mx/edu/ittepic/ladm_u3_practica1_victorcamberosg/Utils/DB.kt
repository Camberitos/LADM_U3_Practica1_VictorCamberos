package mx.edu.ittepic.ladm_u3_practica1_victorcamberosg.Utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DB(context : Context) : SQLiteOpenHelper(context, "ACTIVITIES", null, 1)
{
    companion object
    {
        private var instance : DB?= null

        fun getInstance(context : Context) : DB?
        {
            instance.let {
                instance =
                    DB(context)
            }

            return instance
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        var sql = "CREATE TABLE ACTIVITIES(" +
                      "idActivity INTEGER PRIMARY KEY AUTOINCREMENT," +
                      "description VARCHAR(2000)," +
                      "captureDate DATE," +
                      "deliveryDate DATE" +
                  ")"
        db?.execSQL(sql)

        sql = "CREATE TABLE EVIDENCES(" +
                    "idEvidence INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "idActivity INTEGER," +
                    "photo BLOB," +
                    "FOREIGN KEY(idActivity) REFERENCES ACTIVITIES(idActivity)" +
              ")"
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS EVIDENCES")
        db?.execSQL("DROP TABLE IF EXISTS ACTIVITIES")
        onCreate(db)
    }
}