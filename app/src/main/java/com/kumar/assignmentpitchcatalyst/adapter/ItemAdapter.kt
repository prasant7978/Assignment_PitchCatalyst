package com.kumar.assignmentpitchcatalyst.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kumar.assignmentpitchcatalyst.databinding.ItemBinding
import com.kumar.assignmentpitchcatalyst.model.Item

class ItemAdapter(
    val items: ArrayList<Item>,
    val checkedId:(String) -> Unit,
    val uncheckedId:(String) -> Unit,
): RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    class ItemViewHolder(val binding: ItemBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.binding.title.text = items[position].title
        holder.binding.body.text = items[position].body
        holder.binding.checkbox.isChecked = false

        holder.binding.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                checkedId(items[position].id)
            }else{
                uncheckedId(items[position].id)
            }
        }
    }

}