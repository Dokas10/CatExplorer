package com.catexplorer.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.catexplorer.data.CatMainInfo
import com.catexplorer.databinding.FragmentCatBreedDetailsBinding
import com.catexplorer.viewModel.CatBreedViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [CatBreedDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CatBreedDetailsFragment : Fragment() {

    private lateinit var binding: FragmentCatBreedDetailsBinding
    private lateinit var viewModel: CatBreedViewModel
    private var catInformationList: ArrayList<CatMainInfo> = ArrayList()
    private var selectedPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgs()
        viewModel = ViewModelProvider(requireActivity())[CatBreedViewModel::class.java]
        catInformationList = viewModel.getCatInformation().value!!
    }

    private fun getArgs(){
        selectedPosition = arguments!!.getInt("Position")
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
                DetailsLayout()
            }
        }
    }

    @Preview
    @Composable
    fun DetailsLayout() {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.verticalScroll(rememberScrollState()).padding(10.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(15.dp)) {
                    Text(text = catInformationList[selectedPosition].catBreedInfo!![0].name!!, fontSize = 30.sp, fontWeight = Bold)
                    FavoriteButton(modifier = Modifier.padding(20.dp))
                }
            }
            Box(modifier = Modifier.fillMaxWidth().padding(10.dp), contentAlignment = Alignment.Center) {
                AsyncImage(
                    model = catInformationList[selectedPosition].url,
                    contentDescription = null)
            }
            Text(text = "Origin", fontSize = 20.sp, fontWeight = Bold)
            Text(text = catInformationList[selectedPosition].catBreedInfo!![0].origin!!)
            Text(text = "Temperament", fontSize = 20.sp, fontWeight = Bold)
            Text(text = catInformationList[selectedPosition].catBreedInfo!![0].temperament!!)
            Text(text = "Description", fontSize = 20.sp, fontWeight = Bold)
            Text(text = catInformationList[selectedPosition].catBreedInfo!![0].description!!)
        }
    }

    @Composable
    fun FavoriteButton(
        modifier: Modifier = Modifier,
        color: Color = Color.Gray
    ) {

        var isFavorite by remember { mutableStateOf(catInformationList[selectedPosition].favorite) }

        IconToggleButton(
            checked = isFavorite,
            onCheckedChange = {
                isFavorite = !isFavorite
                this@CatBreedDetailsFragment.catInformationList[selectedPosition].favorite = isFavorite
                viewModel.setCatInformation(this@CatBreedDetailsFragment.catInformationList)
            }
        ) {
            Icon(
                tint = color,
                imageVector = if (isFavorite) {
                    Icons.Filled.Favorite
                } else {
                    Icons.Default.FavoriteBorder
                },
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
        }
    }
}