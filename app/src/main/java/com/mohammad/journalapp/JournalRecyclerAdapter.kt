package com.mohammad.journalapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mohammad.journalapp.databinding.JournalRowBinding

class JournalRecyclerAdapter(val context:Context,var journalList:List<Journal>)
    :RecyclerView.Adapter<JournalRecyclerAdapter.MyHolder>() {
     lateinit var binding:JournalRowBinding

    public class MyHolder(val binding: JournalRowBinding)
        :RecyclerView.ViewHolder(binding.root){
      fun bind(journal:Journal){
          binding.journal=journal
      }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
//       val view: View =LayoutInflater.from(context)
//           .inflate(R.layout.journal_row,parent,false)
//        return MyHolder(view,context)
       binding = JournalRowBinding.inflate(
           LayoutInflater.from(parent.context),
           parent,
           false
       )
      return MyHolder(binding)
    }

    override fun getItemCount(): Int {
       return journalList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val journal:Journal = journalList.get(position)
        holder.bind(journal)
    }
}