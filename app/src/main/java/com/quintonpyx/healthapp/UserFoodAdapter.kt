package com.quintonpyx.healthapp

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.quintonpyx.healthapp.helper.GeneralHelper
import java.util.*




class UserFoodAdapter(val context: Context, val userFoodList: ArrayList<UserFood>): RecyclerView.Adapter<UserFoodAdapter.UserFoodViewHolder>() {

    private lateinit var database: DatabaseReference
    private lateinit var user: FirebaseUser

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserFoodViewHolder {
//        Log.d("TAG","RAN")

        val view:View = LayoutInflater.from(context).inflate(R.layout.user_food_layout,parent,false)
        return UserFoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserFoodViewHolder, position: Int) {
        val currentUserFood = userFoodList[position]
        holder.txtName.text = currentUserFood.food
        holder.txtCalorie.text = currentUserFood.calorie.toString() +" kcal"

        holder.btnDelete.setOnClickListener {
            // add food
            user = FirebaseAuth.getInstance().currentUser!!
            database = Firebase.database.reference

                database.child("userFood").orderByChild("uid").equalTo(currentUserFood.uid).ref.removeValue()
                notifyDataSetChanged()
                Toast.makeText(this.context,"Food has been removed successfully", Toast.LENGTH_SHORT)


        }
        holder.itemView.setOnClickListener{
//           // add user
        }
    }

    override fun getItemCount(): Int {
        return userFoodList.size
    }

    class UserFoodViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val txtName = itemView.findViewById<TextView>(R.id.txt_name)
        val txtCalorie = itemView.findViewById<TextView>(R.id.txt_calorie)
        val btnDelete = itemView.findViewById<TextView>(R.id.btnDelete)

    }


}