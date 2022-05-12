package ru.maxmv.whack_a_mole.presentation.game_over

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import ru.maxmv.whack_a_mole.R
import ru.maxmv.whack_a_mole.databinding.FragmentGameOverBinding


class GameOverFragment : Fragment() {
    private var _binding: FragmentGameOverBinding? = null
    private val binding get() = _binding!!

    private val args: GameOverFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameOverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val preferences = this.requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val preferencesEditor = preferences.edit()

        if (preferences.getInt("score", 0) < args.score) {
            preferencesEditor.putInt("score", args.score)
            preferencesEditor.apply()
        }

        binding.apply {
            textViewScore.text = getString(R.string.result, args.score)
            imageButtonHome.setOnClickListener {
                val action = GameOverFragmentDirections.actionGameOverFragmentToMainMenuFragment()
                findNavController().navigate(action)
            }
            imageButtonReplay.setOnClickListener {
                val action = GameOverFragmentDirections.actionGameOverFragmentToGameFragment()
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}