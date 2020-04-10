package mx.edu.ittepic.ladm_u3_practica1_victorcamberosg.Adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.image_layout.view.*
import mx.edu.ittepic.ladm_u3_practica1_victorcamberosg.R

class AdapterImage(private val data: List<Bitmap?>?) : RecyclerView.Adapter<AdapterImage.Holder>()
{
    class Holder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: Bitmap?) {
            item?.let {
                with(it)
                {
                    Glide.with(itemView.context).load(item).into(itemView.imageActivity)
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
            holder.bindView(data[position])
        }
    }
}