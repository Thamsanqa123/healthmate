package com.example.healthmate


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.healthmate.R
import java.text.SimpleDateFormat
import java.util.*

class MealsAdapter(private val onItemClick: (Meal) -> Unit) :
    ListAdapter<Meal, MealsAdapter.MealViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_meal, parent, false)
        return MealViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = getItem(position)
        holder.bind(meal)
        holder.itemView.setOnClickListener { onItemClick(meal) }
    }

    class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvType: TextView = itemView.findViewById(R.id.tvMealType)
        private val tvFoodItems: TextView = itemView.findViewById(R.id.tvFoodItems)
        private val tvCalories: TextView = itemView.findViewById(R.id.tvCalories)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)

        fun bind(meal: Meal) {
            tvType.text = meal.mealType
            tvFoodItems.text = meal.foodItems
            tvCalories.text = "${meal.calories} cal"
            tvDate.text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(meal.date)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.mealId == newItem.mealId
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }
}