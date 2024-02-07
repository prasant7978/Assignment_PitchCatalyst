package com.kumar.assignmentpitchcatalyst

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kumar.assignmentpitchcatalyst.databinding.ActivityAddItemBinding
import com.kumar.assignmentpitchcatalyst.model.Item

class AddItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddItemBinding
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance("https://assignment-pitch-catalyst-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private val ref = db.getReference("items")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)

        binding.cancelButton.setOnClickListener {
            finish()
        }

        binding.addlButton.setOnClickListener {
            if(binding.titleInput.length() > 0 && binding.bodyInput.length() > 0){
                val item = Item(
                    "",
                    binding.titleInput.text.toString(),
                    binding.bodyInput.text.toString()
                )

                addItemToDB(item)
            }
            else{
                Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
            }
        }

        setContentView(binding.root)
    }

    private fun addItemToDB(item: Item) {
        val id = ref.push().key.toString()
        item.id = id

        ref.child(id).setValue(item).addOnSuccessListener {
            Toast.makeText(this, "Item added", Toast.LENGTH_SHORT).show()
            binding.titleInput.text.clear()
            binding.bodyInput.text.clear()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show()
        }
    }
}