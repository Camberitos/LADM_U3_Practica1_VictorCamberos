package mx.edu.ittepic.ladm_u3_practica1_victorcamberosg.Data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException
import android.graphics.Bitmap
import mx.edu.ittepic.ladm_u3_practica1_victorcamberosg.Utils.DB
import mx.edu.ittepic.ladm_u3_practica1_victorcamberosg.Utils.Utils
import java.lang.Exception

class Evidence()
{
    var idActivity = -1
    var photo : ArrayList<Bitmap?> ?= null
    var idEvidence = -1

    constructor(idActivity : Int, photo : ArrayList<Bitmap?>) : this()
    {
        this.idActivity = idActivity
        this.photo = photo
    }

    lateinit var pointer : Context

    fun asignPointer(context : Context)
    {
        pointer = context
    }

    fun deleteEvidence(id : Int, deleteTo : Boolean) : Boolean
    {
        //true -> Evidence
        //false -> Activity
        try
        {
            val db =
                DB.getInstance(
                    pointer
                )
            val delete = db?.writableDatabase
            val idDelete = arrayOf(id.toString())

            val answer =
                if(deleteTo)
                    delete?.delete("EVIDENCES", "idEvidence = ?", idDelete)
                else
                    delete?.delete("EVIDENCES", "idActivity = ?", idDelete)

            if(answer == 0)
                return false

            delete?.close()
            db?.close()
        }
        catch(e : SQLiteException)
        {
            return false
        }
        return true
    }

    fun insertEvidence() : Boolean
    {
        try
        {
            val db = DB.getInstance(pointer)
            val insert = db?.writableDatabase
            photo?.forEach {
                it?.let {
                    val data = ContentValues()
                    data.put("idActivity", idActivity)
                    data.put("photo", Utils.getByteArray(it))

                    val answer = insert?.insert("EVIDENCES", "idEvidence", data)
                    if(answer?.toInt() == -1)
                        return false
                }
            }

            insert?.close()
            db?.close()
        }
        catch(e : Exception)
        {
            return false
        }
        return true
    }

    fun findEvidence(id : String) : ArrayList<Evidence>
    {
        val evidenceArray = ArrayList<Evidence>()

        try
        {
            val db = DB.getInstance(pointer)
            val select = db?.readableDatabase
            val columns = arrayOf("*")
            val idToSearch = arrayOf(id)

            val cursor = select?.query("EVIDENCES", columns, "idActivity = ?",idToSearch, null, null, null)


            if(cursor?.moveToFirst()!!)
            {
                do
                {
                    val arrayPhoto = getArrayList(cursor.getBlob(2))
                    val evidence = Evidence(cursor.getInt(1), arrayPhoto)
                    evidence.idEvidence = cursor.getInt(0)
                    evidenceArray.add(evidence)
                }while(cursor.moveToNext())
            }

            select.close()
            db.close()
        }
        catch(e:SQLiteException)
        {
            Utils.alertMessage("Algo salió mal :/", "Atencion", pointer)
        }

        return evidenceArray
    }

    private fun getArrayList(blob: ByteArray?): ArrayList<Bitmap?>
    {
        val array = ArrayList<Bitmap?>()
        array.add(Utils.getBitMap(blob))
        return array
    }
}