package com.quintonpyx.healthapp

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import java.util.*




class FoodAdapter(val context: Context, val foodList: ArrayList<Food>): RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
//        Log.d("TAG","RAN")

        val view:View = LayoutInflater.from(context).inflate(R.layout.food_layout,parent,false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val currentFood = foodList[position]
        holder.txtName.text = currentFood.name
        holder.txtCalorie.text = currentFood.calorie.toString() +"kcal"

        holder.itemView.setOnClickListener{
//           // add food to user
        }
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    class FoodViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val txtName = itemView.findViewById<TextView>(R.id.txt_name)
        val txtCalorie = itemView.findViewById<TextView>(R.id.txt_calorie)

    }


}