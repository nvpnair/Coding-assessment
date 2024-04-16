package com.assessment.test.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.assessment.test.databinding.ItemcardBinding
import com.assessment.test.dataclass.PostData
import com.assessment.test.onDetailsClickListener

class PostAdapter(private var dataList: MutableList<PostData>,private val listener: onDetailsClickListener) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemcardBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.bind(data)

        holder.binding.card.setOnClickListener(View.OnClickListener {

            listener.onItemClicked(data)
        })
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolder(val binding: ItemcardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: PostData) {
            val startTime = System.currentTimeMillis()
            binding.id.text=data.id.toString()
           binding.txtHead.text= data.title
            binding.txtBody.text=data.body


            val endTime = System.currentTimeMillis()
            val bindingTime = endTime - startTime
            Log.d("ItemBindingTime", "Item at position  took $bindingTime ms to bind")
            binding.log.text= "took $bindingTime ms to bind"

        }

    }
}