package ru.maxmv.whack_a_mole.presentation.game.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay

import ru.maxmv.whack_a_mole.data.model.Hole
import ru.maxmv.whack_a_mole.databinding.ItemHoleBinding

class HoleAdapter : RecyclerView.Adapter<HoleAdapter.HoleViewHolder>() {
    private val items = mutableListOf<Hole>()

    var onItemClick: ((Hole) -> Unit)? = null

    fun setItems(items: List<Hole>) {
        this.items.apply {
            clear()
            addAll(items)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoleViewHolder {
        val binding =
            ItemHoleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HoleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HoleViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun onViewRecycled(holder: HoleViewHolder) {
        holder.unbind()
    }

    fun showMole(): Int {
        val position = (0 until items.size).shuffled().last()
        items[position].state = true
        notifyDataSetChanged()
        return position
    }

    fun hideMole(position: Int) {
        items[position].state = false
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    inner class HoleViewHolder(
        private var binding: ItemHoleBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(hole: Hole) {
            binding.imageButtonMole.setOnClickListener {
                onItemClick?.invoke(hole)
            }
            if (hole.state) {
                binding.imageButtonMole.visibility = View.VISIBLE
            } else {
                binding.imageButtonMole.visibility = View.INVISIBLE
            }
        }

        fun unbind() {
            itemView.setOnClickListener(null)
        }
    }
}