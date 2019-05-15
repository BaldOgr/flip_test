package com.github.baldogre.test.module.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.baldogre.test.R
import com.github.baldogre.test.common.OnItemClick
import com.github.baldogre.test.model.Robot

class RobotsAdapter(val robots: MutableList<Robot> = arrayListOf(), private val onItemClick: OnItemClick<Robot>) : RecyclerView.Adapter<RobotsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, type: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.robots_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return robots.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val robot = robots[position]
        viewHolder.id.text =
                viewHolder.id.context.getString(R.string.id, robot.id.toString())

        viewHolder.name.text =
                viewHolder.id.context.getString(R.string.name, robot.name)

        viewHolder.type.text =
                viewHolder.id.context.getString(R.string.type, robot.type)

        viewHolder.year.text =
                viewHolder.id.context.getString(R.string.year, robot.year)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id : TextView = itemView.findViewById(R.id.id)
        val name : TextView = itemView.findViewById(R.id.name)
        val type : TextView = itemView.findViewById(R.id.type)
        val year : TextView = itemView.findViewById(R.id.year)
        init {
            itemView.setOnClickListener { onItemClick.onClick(robots[adapterPosition], adapterPosition) }
        }
    }
}
