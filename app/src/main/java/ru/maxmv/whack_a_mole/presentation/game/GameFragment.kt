package ru.maxmv.whack_a_mole.presentation.game

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.HandlerCompat.postDelayed
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.*
import ru.maxmv.whack_a_mole.data.model.Hole
import ru.maxmv.whack_a_mole.databinding.FragmentGameBinding
import ru.maxmv.whack_a_mole.presentation.game.adapter.HoleAdapter
import ru.maxmv.whack_a_mole.utils.Decorator

class GameFragment : Fragment() {
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!
    private val adapter = HoleAdapter()

    companion object {
        private const val interval = 500L
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)

        val holes = mutableListOf<Hole>()
        for (i in 1..9) {
            holes.add(Hole())
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(context, 3)
        binding.recyclerView.addItemDecoration(Decorator(50))
        var score = 0
        binding.textViewHighscore.setText("$score")
        adapter.setItems(holes)



        binding.buttonPlay.root.setOnClickListener {

            binding.buttonPlay.root.visibility = View.GONE
            binding.textViewTimer.visibility = View.VISIBLE

            object : CountDownTimer(30000, 1) {

                override fun onTick(millisUntilFinished: Long) {
                    binding.textViewTimer.text = "${millisUntilFinished / 1000}"
                }

                override fun onFinish() {
                    Toast.makeText(context, "YAS", Toast.LENGTH_SHORT).show()
                }
            }.start()

            var position = -1

            object : Runnable {
                override fun run() {
                    try {
                        if (position != -1) {
                            adapter.hideMole(position)
                        }
                        adapter.onItemClick = {
                            ++score
                            binding.textViewScore.text = "$score"
                            adapter.hideMole(position)
                        }
                        position = adapter.showMole()
                    } finally {
                        Handler().postDelayed(this, interval)
                    }
                }
            }.run()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}