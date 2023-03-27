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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*




class FoodAdapter(val context: Context, val foodList: ArrayList<Food>): RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {
    private lateinit var database: DatabaseReference
    private lateinit var user: FirebaseUser
  
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
//        Log.d("TAG","RAN")

      
        val view:View = LayoutInflater.from(context).inflate(R.layout.food_layout,parent,false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val currentFood = foodList[position]
        holder.txtName.text = currentFood.name
        holder.txtCalorie.text = currentFood.calorie.toString() +"kcal"

        holder.btnAdd.setOnClickListener {
            // add food
            user = FirebaseAuth.getInstance().currentUser!!
            database = Firebase.database.reference
            // generate random key
            val key = database.push().key
            if (key != null) {
                database.child("userFood").child(key).setValue(UserFood(user.uid,currentFood.name,currentFood.calorie))
            }

        }
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
        val btnAdd = itemView.findViewById<TextView>(R.id.btnAdd)

    }


}