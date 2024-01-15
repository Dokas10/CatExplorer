package com.catexplorer.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.catexplorer.R
import com.catexplorer.databinding.FragmentBreedFavoritesBinding
import com.catexplorer.databinding.FragmentCatBreedDetailsBinding

class BreedFavoritesFragment : Fragment() {

    private lateinit var binding: FragmentBreedFavoritesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBreedFavoritesBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }
}