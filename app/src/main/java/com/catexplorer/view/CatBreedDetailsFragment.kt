package com.catexplorer.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.room.Room
import coil.compose.AsyncImage
import com.catexplorer.R
import com.catexplorer.data.CatMainInfo
import com.catexplorer.databinding.FragmentCatBreedDetailsBinding
import com.catexplorer.utils.CatBreedDatabase
import com.catexplorer.viewModel.CatBreedViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [CatBreedDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CatBreedDetailsFragment : Fragment() {

    private lateinit var binding: FragmentCatBreedDetailsBinding
    private val viewModel: CatBreedViewModel by activityViewModels()
    private var catInformationList: ArrayList<CatMainInfo> = ArrayList()
    private var selectedPosition = -1
    private lateinit var db: CatBreedDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Get all the data used on the screen

        arguments?.let {
            selectedPosition = it.getInt(POSITION)
        } ?: kotlin.run {
            binding.composeViewDetails.apply {
                setContent {
                    ErrorLabel()
                }
            }
        }
        viewModel.getCatInformation().value?.let {
            catInformationList = it
        } ?: run {
            binding.composeViewDetails.apply {
                setContent {
                    ErrorLabel()
                }
            }
        }
        try {
            db = Room.databaseBuilder(
                context!!,
                CatBreedDatabase::class.java, CatBreedListFragment.DATABASE_NAME
            ).build()
        }catch (e : Exception){
            e.printStackTrace()
            Toast.makeText(requireContext(), getString(R.string.generic_database_access_error), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (::binding.isInitialized) {
            binding.root
        } else {
            Toast.makeText(requireContext(), getString(R.string.generic_binding_initialization_error), Toast.LENGTH_SHORT).show()
            super.onCreateView(inflater, container, savedInstanceState)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.composeViewDetails.apply{
            setContent{
                DetailsLayout()
            }
        }
    }

    //Details layout composable function
    @Composable
    fun DetailsLayout() {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(10.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(15.dp)) {
                    Text(text = catInformationList[selectedPosition].catBreedInfo[0].name!!, fontSize = 30.sp, fontWeight = Bold)
                    FavoriteButton(modifier = Modifier.padding(20.dp))
                }
            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp), contentAlignment = Alignment.Center) {
                AsyncImage(
                    model = catInformationList[selectedPosition].url,
                    contentDescription = null)
            }
            Text(text = getString(R.string.details_screen_origin), fontSize = 20.sp, fontWeight = Bold)
            Text(text = catInformationList[selectedPosition].catBreedInfo[0].origin!!)
            Text(text = getString(R.string.details_screen_temperament), fontSize = 20.sp, fontWeight = Bold)
            Text(text = catInformationList[selectedPosition].catBreedInfo[0].temperament!!)
            Text(text = getString(R.string.details_screen_description), fontSize = 20.sp, fontWeight = Bold)
            Text(text = catInformationList[selectedPosition].catBreedInfo[0].description!!)
        }
    }

    //Favorite button composable function
    @Composable
    fun FavoriteButton(
        modifier: Modifier = Modifier,
        color: Color = Color.Gray
    ) {

        var isFavorite by remember { mutableStateOf(catInformationList[selectedPosition].favorite) }

        IconToggleButton(
            checked = isFavorite,
            onCheckedChange = {
                //If button is clicked, changes the favorite state of the button and of the breed that is presented in the screen (Data is updated in the ViewModel)
                isFavorite = !isFavorite
                catInformationList[selectedPosition].favorite = isFavorite
                viewModel.insertDataIntoDatabase(db, catInformationList[selectedPosition])
                viewModel.setCatInformation(catInformationList)
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

    @Composable
    fun ErrorLabel(){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = getString(R.string.obtain_breed_details_data_error), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }

    companion object{
        const val POSITION : String = "Position"
    }
}