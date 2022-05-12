package ru.maxmv.whack_a_mole.presentation.game

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager

import kotlinx.coroutines.*

import ru.maxmv.whack_a_mole.R
import ru.maxmv.whack_a_mole.data.model.Hole
import ru.maxmv.whack_a_mole.databinding.FragmentGameBinding
import ru.maxmv.whack_a_mole.presentation.game.adapter.HoleAdapter
import ru.maxmv.whack_a_mole.utils.Decorator

class GameFragment : Fragment() {
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameViewModel by viewModels()
    private val adapter = HoleAdapter()

    private var score = 0

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

        val preferences = this.requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)

        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = GridLayoutManager(context, 3)
            recyclerView.addItemDecoration(Decorator(50))
            textViewHighscore.text = getString(R.string.highscore, preferences.getInt("score", 0))
            textViewScore.text = getString(R.string.score, 0)
            buttonPlay.root.setOnClickListener {
                buttonPlay.root.visibility = View.GONE
                textViewTimer.visibility = View.VISIBLE
                viewModel.countTime()
                runGame()
            }
        }

        viewModel.apply {
            timeLiveData.observe(viewLifecycleOwner) {
                binding.textViewTimer.text = getString(R.string.time_left, it)
            }

            isGameOverLiveData.observe(viewLifecycleOwner) {
                val action = GameFragmentDirections.actionGameFragmentToGameOverFragment(score)
                findNavController().navigate(action)
            }
        }

        adapter.setItems(holes)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun runGame() {
        var position = -1

        object : Runnable {
            override fun run() {
                try {
                    if (position != -1) {
                        adapter.hideMole(position)
                    }
                    adapter.onItemClick = {
                        ++score
                        binding.textViewScore.text = getString(R.string.score, score)
                        adapter.hideMole(position)
                    }
                    position = adapter.showMole()
                } finally {
                    Handler(Looper.getMainLooper()).postDelayed(this, interval)
                }
            }
        }.run()
    }
}