package com.catexplorer.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import com.catexplorer.R
import com.catexplorer.data.CatMainInfo
import com.catexplorer.databinding.FragmentCatBreedDetailsBinding
import com.catexplorer.databinding.FragmentCatBreedListBinding

/**
 * A simple [Fragment] subclass.
 * Use the [CatBreedDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CatBreedDetailsFragment : Fragment() {

    private lateinit var binding: FragmentCatBreedDetailsBinding
    private var catInfo: CatMainInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgs()
    }

    private fun getArgs(){
        catInfo = arguments?.getSerializable("BreedInfo") as CatMainInfo?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatBreedDetailsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.composeViewDetails.apply{
            setContent{
                listDetails()
            }
        }
    }

    @Composable
    fun listDetails(){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = catInfo?.url,
                contentDescription = null
            )
            Text(text = catInfo?.catBreedInfo!![0].name!!)
            Text(text = catInfo?.catBreedInfo!![0].origin!!)
            Text(text = catInfo?.catBreedInfo!![0].temperament!!)
            Text(text = catInfo?.catBreedInfo!![0].description!!)
        }
    }
}