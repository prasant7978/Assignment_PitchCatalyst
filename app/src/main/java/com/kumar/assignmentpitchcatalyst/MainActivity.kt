package com.kumar.assignmentpitchcatalyst

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kumar.assignmentpitchcatalyst.adapter.ItemAdapter
import com.kumar.assignmentpitchcatalyst.databinding.ActivityMainBinding
import com.kumar.assignmentpitchcatalyst.model.Item

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance("https://assignment-pitch-catalyst-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private val ref = db.reference.child("items")
    val items = ArrayList<Item>()
    val deleteSet = hashSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        getAllItems()

        binding.addItem.setOnClickListener {
            val intent = Intent(this@MainActivity, AddItemActivity::class.java)
            startActivity(intent)
        }

        binding.deleteItem.setOnClickListener {
            if(deleteSet.size > 0) {
                AlertDialog.Builder(this)
                    .setTitle("Delete items")
                    .setMessage("Are you sure you want to delete these items?")
                    .setCancelable(false)
                    .setNegativeButton(
                        "No",
                        DialogInterface.OnClickListener { dialogInterface, which ->
                            dialogInterface.cancel()
                        })
                    .setPositiveButton(
                        "Yes",
                        DialogInterface.OnClickListener { dialogInterface, which ->
                            deleteItems()
                        }).create().show()
            }
            else
                Toast.makeText(this, "Select any item to delete", Toast.LENGTH_SHORT).show()
        }

        setContentView(binding.root)
    }

    private fun deleteItems(){
        for(key in deleteSet){
            ref.child(key).removeValue()
        }

        getAllItems()
    }

    private fun getAllItems(){
        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                items.clear()
                for(data in snapshot.children){
                    val item = data.getValue(Item::class.java)
                    if(item != null){
                        item.id = data.key.toString()
                        items.add(item)
                    }
                }

                if(items.isNotEmpty()){
                    binding.recyclerview.layoutManager = LinearLayoutManager(this@MainActivity)
                    binding.recyclerview.adapter = ItemAdapter(items,
                        {checkedId->
                            deleteSet.add(checkedId)
                        },
                        {uncheckedId->
                            deleteSet.remove(uncheckedId)
                        }
                    )

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}