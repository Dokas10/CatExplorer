package com.catexplorer.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import coil.compose.AsyncImage
import com.catexplorer.R
import com.catexplorer.data.CatMainInfo
import com.catexplorer.databinding.FragmentBreedFavoritesBinding
import com.catexplorer.utils.CatBreedDatabase
import com.catexplorer.viewModel.CatBreedViewModel
import kotlinx.coroutines.launch

class BreedFavoritesFragment : Fragment() {

    private lateinit var binding: FragmentBreedFavoritesBinding
    private var favorites: ArrayList<CatMainInfo> = ArrayList()
    private var allBreeds: ArrayList<CatMainInfo> = ArrayList()
    private lateinit var db: CatBreedDatabase
    private val viewModel : CatBreedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(
            context!!,
            CatBreedDatabase::class.java, DATABASE_NAME
        ).build()
    }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Return from the viewModel all the saved breeds that were marked as favorites
        viewModel.getCatInformation().observe(this, Observer {
            allBreeds = it
            for(i in it) {
                if(i.favorite){
                    favorites.add(i)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        //Compose methods. It verifies if there are favorites
        //Populates list in positive case
        //Presents an empty state screen in negative case
        if(favorites.isNotEmpty()) {
            binding.composeViewFavs.apply {
                setContent {
                    FavoriteList()
                }
            }
        }else{
            binding.composeViewFavs.apply {
                setContent {
                    EmptyState()
                }
            }
        }
    }

    //Empty state composable function
    @Composable
    fun EmptyState(){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = getString(R.string.no_favorites_message), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }

    //Favorite list function
    @Composable
    fun FavoriteList(){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(3),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp)
            ){
                when(favorites) {
                    else -> itemsIndexed(favorites) { index, item ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Box(contentAlignment = Alignment.TopEnd) {

                                AsyncImage(
                                    model = item.url,
                                    contentDescription = null
                                )

                                FavoriteButton(modifier = Modifier.padding(12.dp), position = index)

                            }
                            Text(
                                text = item.catBreedInfo[0].name!!,
                            )
                            Text(text = getString(R.string.life_span_message, item.catBreedInfo[0].life_span!!))
                        }
                    }
                }
            }
        }
    }

    //Favorite button composable function(for each on of list elements)
    @Composable
    fun FavoriteButton(
        modifier: Modifier = Modifier,
        color: Color = Color.Gray,
        position: Int
    ) {

        var isFavorite by remember { mutableStateOf(favorites[position].favorite) }

        IconToggleButton(
            checked = isFavorite,
            onCheckedChange = {
                //
                isFavorite = !isFavorite
                favorites[position].favorite = isFavorite
                lifecycleScope.launch {
                    viewModel.insertDataIntoDatabase(db, favorites[position])
                }
                viewModel.setCatInformation(updateData())
            }
        ) {
            Icon(
                tint = color,
                imageVector = if (isFavorite) {
                    Icons.Filled.Favorite
                } else {
                    Icons.Default.FavoriteBorder
                },
                contentDescription = null
            )
        }
    }

    //Method to update all data based on the favorites that were removed
    private fun updateData(): ArrayList<CatMainInfo>{
        val mapFavorites= favorites.associateBy { it.id }
        return ArrayList(allBreeds.map { element1 ->
            mapFavorites[element1.id] ?: element1
        })
    }

    companion object{
        const val DATABASE_NAME = "CatInfoDatabase"
    }
}