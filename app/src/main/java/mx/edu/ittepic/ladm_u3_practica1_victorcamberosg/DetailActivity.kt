package mx.edu.ittepic.ladm_u3_practica1_victorcamberosg

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_detail.*
import mx.edu.ittepic.ladm_u3_practica1_victorcamberosg.Adapter.AdapterEvidence
import mx.edu.ittepic.ladm_u3_practica1_victorcamberosg.Data.Activities
import mx.edu.ittepic.ladm_u3_practica1_victorcamberosg.Data.Evidence
import mx.edu.ittepic.ladm_u3_practica1_victorcamberosg.Utils.Utils

class DetailActivity : AppCompatActivity() {

    var id = -1

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        recyclerViewDetail.layoutManager = GridLayoutManager(this, 2)

        intent?.extras?.let {
            txtCaptureDateDetail.text = "Fecha captura: ${it.getString("captureDate")}"
            txtDeliveryDateDetail.text = "Fecha entrega: ${it.getString("deliveryDate")}"
            txtDescriptionDetail.text = "Descripción: ${it.getString("description")}"
            id = it.getInt("id")
            fillRecycler()

            btnDeleteActivity.setOnClickListener {
                deleteQActivity()
            }
        }
    }

    private fun deleteQActivity()
    {
        AlertDialog.Builder(this)
            .setTitle("¿Seguro que desea eliminarlo?")
            .setPositiveButton("SI"){d,i->
                deleteActivity()
            }
            .setNegativeButton("NO"){d,i->}
            .show()
    }

    private fun deleteActivity()
    {
        val activity = Activities()
        activity.asignPointer(this)

        if(activity.deleteActivity(id))
        {
            Utils.toastMessageLong("Se eliminó satisfactoriamente", this)
            finish()
        }
        else
        {
            Utils.toastMessageLong("Algo salió mal :/", this)
        }
    }

    fun fillRecycler()
    {
        val evidence = Evidence()
        evidence.asignPointer(this)

        val array = evidence.findEvidence(id.toString())

        recyclerViewDetail.adapter = AdapterEvidence(array, this)
    }
}
