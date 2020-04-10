package mx.edu.ittepic.ladm_u3_practica1_victorcamberosg

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import mx.edu.ittepic.ladm_u3_practica1_victorcamberosg.Adapter.AdapterActivities
import mx.edu.ittepic.ladm_u3_practica1_victorcamberosg.Data.Activities
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val activity = Activities()

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activity.asignPointer(this)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        edtFind.addTextChangedListener {
            toFilter(edtFind.text.toString())
        }
    }

    private fun toFilter(text : String)
    {
        val filteredItems = ArrayList<Activities>()
        val array = activity.getActivities()

        array.forEach {
            if(it.description.toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))  ||
                it.captureDate.contains(text)                           ||
                it.deliveryDate.contains(text))
            {
                filteredItems.add(it)
            }
        }

        recyclerView.adapter = AdapterActivities(filteredItems)
    }

    override fun onResume() {
        super.onResume()
        fillRecycler()
    }

    private fun fillRecycler()
    {
        val evidence = activity.getActivities()
        recyclerView.adapter = AdapterActivities(evidence)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when(item.itemId)
        {
            R.id.addActivity -> startActivity(Intent(this, ActivitiesActivity :: class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}
