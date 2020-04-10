package mx.edu.ittepic.ladm_u3_practica1_victorcamberosg.Adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.image_layout.view.*
import mx.edu.ittepic.ladm_u3_practica1_victorcamberosg.Data.Evidence
import mx.edu.ittepic.ladm_u3_practica1_victorcamberosg.DetailActivity
import mx.edu.ittepic.ladm_u3_practica1_victorcamberosg.R
import mx.edu.ittepic.ladm_u3_practica1_victorcamberosg.Utils.Utils

class AdapterEvidence(private val data: List<Evidence?>?, private val detailActivity : DetailActivity) : RecyclerView.Adapter<AdapterEvidence.Holder>()
{
    class Holder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: Evidence?, detailActivity : DetailActivity) {
            item?.let {
                with(it)
                {
                    Glide.with(itemView.context).load(photo?.get(0)).into(itemView.imageActivity)

                    itemView.setOnClickListener {
                        AlertDialog.Builder(itemView.context)
                            .setTitle("¿Seguro que desea eliminar esta evidencia?")
                            .setPositiveButton("SI"){_,_->
                                val evidence = Evidence()
                                evidence.asignPointer(itemView.context)

                                if(evidence.deleteEvidence(idEvidence, true))
                                {
                                    Utils.toastMessageLong("Se eliminó correctamente", itemView.context)
                                    detailActivity.fillRecycler()
                                }
                                else
                                {
                                    Utils.toastMessageLong("Algo salió mal :/", itemView.context)
                                }
                            }
                            .setNegativeButton("NO"){_,_->}
                            .show()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return Holder(
            layoutInflater.inflate(
                R.layout.image_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = data?.size ?: 0

    override fun onBindViewHolder(holder: Holder, position: Int) {
        data?.let {
            holder.bindView(data[position], detailActivity)
        }
    }
}