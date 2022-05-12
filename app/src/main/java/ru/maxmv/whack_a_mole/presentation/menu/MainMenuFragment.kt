package ru.maxmv.whack_a_mole.presentation.menu

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import ru.maxmv.whack_a_mole.R
import ru.maxmv.whack_a_mole.databinding.FragmentMainMenuBinding

class MainMenuFragment : Fragment() {

    private var _binding: FragmentMainMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferences = this.requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)

        binding.textViewHighscore.text =
            getString(R.string.highscore, preferences.getInt("score", 0))

        binding.imageButtonPlay.root.setOnClickListener {
            val action = MainMenuFragmentDirections.actionMainMenuFragmentToGameFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

