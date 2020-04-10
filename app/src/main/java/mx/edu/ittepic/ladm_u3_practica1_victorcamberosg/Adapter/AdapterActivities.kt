package mx.edu.ittepic.ladm_u3_practica1_victorcamberosg.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_activities.view.*
import mx.edu.ittepic.ladm_u3_practica1_victorcamberosg.Data.Activities
import mx.edu.ittepic.ladm_u3_practica1_victorcamberosg.Data.Evidence
import mx.edu.ittepic.ladm_u3_practica1_victorcamberosg.DetailActivity
import mx.edu.ittepic.ladm_u3_practica1_victorcamberosg.R
import androidx.core.util.Pair

class AdapterActivities(private val data: List<Activities>) : RecyclerView.Adapter<AdapterActivities.Holder>()
{
    class Holder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bindView(item: Activities?) {
            item?.let {
                with(it)
                {
                    itemView.viewDescription.text = "Descripción: $description"
                    itemView.viewCaptureDate.text = "Fecha captura: $captureDate"
                    itemView.viewDeliveryDate.text = "Fecha entrega: $deliveryDate"

                    val evidence = Evidence()
                    evidence.asignPointer(itemView.context)

                    val photos = evidence.findEvidence(idActivity.toString())

                    if(photos.size == 0)
                    {
                        Glide.with(itemView.context)
                            .load(R.drawable.without_image)
                            .into(itemView.imgItem)
                    }
                    else
                    {
                        Glide.with(itemView.context)
                            .load(photos[0].photo?.get(0))
                            .into(itemView.imgItem)
                    }

                    itemView.setOnClickListener {
                        val intent = Intent(itemView.context, DetailActivity::class.java)

                        intent.putExtra("description", description)
                        intent.putExtra("captureDate", captureDate)
                        intent.putExtra("deliveryDate", deliveryDate)
                        intent.putExtra("id", idActivity)

                        val p1 : Pair<View, String> = Pair.create(itemView.viewDescription, "description")
                        val p2 : Pair<View, String> = Pair.create(itemView.viewCaptureDate, "captureDate")
                        val p3 : Pair<View, String> = Pair.create(itemView.viewDeliveryDate, "deliveryDate")

                        val options : ActivityOptionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(itemView.context as Activity, p1, p2 ,p3)
                        itemView.context.startActivity(intent, options.toBundle())
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return Holder(
            layoutInflater.inflate(
                R.layout.list_activities,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindView(data[position])
    }
}
