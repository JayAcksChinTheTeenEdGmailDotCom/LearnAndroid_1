package com.example.parkingsg

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast

class RecyclerAdapter(context: Context) : RecyclerView.Adapter<RecyclerAdapter.VehicleHolder>(){

    private var vehicles = emptyList<Vehicle>()
    var onItemClick: ((Vehicle) -> Unit)? = null
    var onVehicleClick: ((Vehicle) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.VehicleHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.select_vehicle_row, parent, false)
        return VehicleHolder(inflater)
    }

    override fun getItemCount() = vehicles.size

    override fun onBindViewHolder(holder: RecyclerAdapter.VehicleHolder, position: Int) {
        holder.btn?.text = vehicles[position].vehiclePlate
        holder.imgBtn?.tag = vehicles[position]
    }

    fun setVehicles(vehicles: List<Vehicle>) {
        this.vehicles = vehicles
        notifyDataSetChanged()
    }

    inner class VehicleHolder(v: View) : RecyclerView.ViewHolder(v) {

//        var row: ConstraintLayout? = null
        var btn: Button? = null
        var imgBtn: ImageButton? = null

        init{
            //row
            //btn
            btn = v.findViewById(R.id.selectVehicle)
            //imgBtn
            imgBtn = v.findViewById(R.id.delVehicle)

            imgBtn?.setOnClickListener {
                onItemClick?.invoke(vehicles[adapterPosition])
            }

            btn?.setOnClickListener {
                onVehicleClick?.invoke(vehicles[adapterPosition])
                //Log.d("selectBtn", "Clicked")
            }
//            imgBtn?.setOnClickListener{
//                Log.d("imgBtn", imgBtn?.tag.toString())
//
//            }
        }
    }

}

