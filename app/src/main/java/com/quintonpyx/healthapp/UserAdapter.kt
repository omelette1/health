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




class UserAdapter(val context: Context, val userList: ArrayList<User>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
//        Log.d("TAG","RAN")

        val view:View = LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.txtName.text = currentUser.name
        holder.txtSteps.text = currentUser.steps.toString() +" steps"
        holder.txtPosition.text = (position+2).toString()

        holder.txtName.setOnClickListener{
            val intent = Intent(this.context,OtherProfile::class.java)
            intent.putExtra("uid",currentUser.uid)
            this.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val txtName = itemView.findViewById<TextView>(R.id.txt_name)
        val txtSteps = itemView.findViewById<TextView>(R.id.txt_steps)
        val txtPosition = itemView.findViewById<TextView>(R.id.txt_position)


    }


}