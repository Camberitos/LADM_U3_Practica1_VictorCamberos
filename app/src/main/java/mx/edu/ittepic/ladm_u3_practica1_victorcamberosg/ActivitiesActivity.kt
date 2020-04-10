package mx.edu.ittepic.ladm_u3_practica1_victorcamberosg

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_new_evidence.*
import mx.edu.ittepic.ladm_u3_practica1_victorcamberosg.Adapter.AdapterImage
import mx.edu.ittepic.ladm_u3_practica1_victorcamberosg.Data.Activities
import mx.edu.ittepic.ladm_u3_practica1_victorcamberosg.Data.Evidence
import mx.edu.ittepic.ladm_u3_practica1_victorcamberosg.Utils.Utils
import java.time.LocalDateTime

class ActivitiesActivity : AppCompatActivity() {

    private var image = ArrayList<Bitmap?>()
    private val requestTakePhoto = 0
    private val requestSelectImageGallery = 1

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_evidence)

        recyclerViewEvidence.layoutManager = GridLayoutManager(this, 2)

        btnAdd.setOnClickListener {
            addEvidence()
        }

        btnCancel.setOnClickListener {
            finish()
        }

        btnAddPhoto.setOnClickListener {
            addImage()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == requestSelectImageGallery)
        {
            val imageUri = data?.data
            fillRecyclerList(Utils.getBitMap(imageUri, contentResolver))
        }
        else if (resultCode == RESULT_OK && requestCode == requestTakePhoto)
        {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            fillRecyclerList(imageBitmap)
        }
    }

    private fun fillRecyclerList(bitMap : Bitmap)
    {
        image.add(bitMap)
        recyclerViewEvidence.adapter = AdapterImage(image)
    }

    private fun addImage()
    {
        val popup = PopupMenu(this, btnAddPhoto)
        popup.inflate(R.menu.photo_popup)

        popup.setOnMenuItemClickListener {
            when(it.itemId)
            {
                R.id.gallery -> selectImageInAlbum()
                R.id.camera -> takePhoto()
            }
            true
        }

        popup.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addEvidence()
    {
        val description = txtDescription.text.toString()
        val deliveryDate = txtDeliveryDate.text.toString()
        val captureDate = LocalDateTime.now().toString().substring(0, 10)

        if(description.trim().isEmpty() || deliveryDate.trim().isEmpty())
        {
            Utils.alertMessage("Asegurese de llenar todos los campos", "Atención", this)
            return
        }

        if(!deliveryDate.contains(Regex("[0-9]{4}-[0-9]{2}-[0-9]{2}")))
        {
            Utils.alertMessage("Asegurese de que el formato de la fecha sea YYYY-MM-DD", "Atención", this)
            return
        }

        val activity = Activities(description, captureDate, deliveryDate)
        activity.asignPointer(this)

        if(activity.insertActivity())
        {
            if(image.size != 0)
            {
                val activity = Activities()
                activity.asignPointer(this)

                val array = activity.getActivities()
                val evidence = Evidence(array[array.size - 1].idActivity, image)
                evidence.asignPointer(this)

                if(evidence.insertEvidence())
                    Utils.toastMessageLong("Se insertó correctamente", this)
                else
                    Utils.alertMessage("Algo salió mal :/", "Error", this)
            }
            else
            {
                Utils.toastMessageLong("Se insertó correctamente", this)
            }
        }
        else
            Utils.alertMessage("Algo salió mal :/", "Error", this)
        finish()
    }

    /**********************************************************************************************/
    private fun selectImageInAlbum()
    {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        if (intent.resolveActivity(packageManager) != null)
        {
            startActivityForResult(intent, requestSelectImageGallery)
        }
    }

    private fun takePhoto()
    {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, requestTakePhoto)
            }
        }
    }
}
