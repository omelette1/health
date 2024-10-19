package com.quintonpyx.healthapp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.quintonpyx.healthapp.helper.GeneralHelper

class FoodAdapter(
    private val context: Context,
    private val foodList: ArrayList<Ingredient>
) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    private val database: DatabaseReference = Firebase.database.reference
    private val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.food_layout, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val currentIngredient = foodList[position]
        holder.txtName.text = currentIngredient.name

        // Load the image using Glide
        Glide.with(context)
            .load(currentIngredient.image) // Ensure this URL is valid
            .into(holder.imgFood)

        holder.btnAdd.setOnClickListener {
            Log.d("FoodAdapter", "Add button clicked for ${currentIngredient.name}")
            Toast.makeText(context, "Add button clicked for ${currentIngredient.name}", Toast.LENGTH_SHORT).show()

            if (user != null) {
                addFoodToUser(currentIngredient)
            } else {
                Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
            }
        }

        holder.itemView.setOnClickListener {
            // Implement item click handling if needed
        }

        holder.btnAdd.setOnClickListener {
            Toast.makeText(context, "Add button clicked!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun addFoodToUser(currentIngredient: Ingredient) {
        val key = database.child("userFood").push().key // Generate a random key
        if (key != null) {
            val userFood = UserFood(key, user!!.uid, currentIngredient.name, 0, GeneralHelper.getTodayDate())
            database.child("userFood").child(key).setValue(userFood)
                .addOnSuccessListener {
                    Toast.makeText(context, "Food has been added successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to add food: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "Error generating food key", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txt_name)
        val imgFood: ImageView = itemView.findViewById(R.id.img_food)
        val btnAdd: TextView = itemView.findViewById(R.id.btnAdd)
    }

}
