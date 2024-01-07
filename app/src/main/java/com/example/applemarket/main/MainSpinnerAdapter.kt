package com.example.applemarket.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.applemarket.databinding.ItemMainSpinnerBinding

class MainSpinnerAdapter (context: Context, private val resId: Int, private val localList: List<String>) : ArrayAdapter<String> (context,resId,localList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var binding = ItemMainSpinnerBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        binding.tvSpinnerItem.text = localList[position]
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var binding = ItemMainSpinnerBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        binding.tvSpinnerItem.text = localList[position]
        return binding.root
    }

    override fun getCount() = localList.size
}