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

class WorkoutsAdapter(private val onItemClick: (Workout) -> Unit) :
    ListAdapter<Workout, WorkoutsAdapter.WorkoutViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout, parent, false)
        return WorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workout = getItem(position)
        holder.bind(workout)
        holder.itemView.setOnClickListener { onItemClick(workout) }
    }

    class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvType: TextView = itemView.findViewById(R.id.tvWorkoutType)
        private val tvDuration: TextView = itemView.findViewById(R.id.tvDuration)
        private val tvCalories: TextView = itemView.findViewById(R.id.tvCalories)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)

        fun bind(workout: Workout) {
            tvType.text = workout.workoutType
            tvDuration.text = "${workout.duration} min"
            tvCalories.text = "${workout.caloriesBurned} cal"
            tvDate.text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(workout.date)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Workout>() {
        override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem.workoutId == newItem.workoutId
        }

        override fun areContentsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem == newItem
        }
    }
}