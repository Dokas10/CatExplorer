package com.catexplorer.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import coil.compose.AsyncImage
import com.catexplorer.data.CatInfoTable
import com.catexplorer.databinding.FragmentBreedFavoritesBinding
import com.catexplorer.utils.CatBreedDatabase
import kotlinx.coroutines.launch

class BreedFavoritesFragment : Fragment() {

    private lateinit var binding: FragmentBreedFavoritesBinding
    private var favorites: List<CatInfoTable> = emptyList()
    private lateinit var db: CatBreedDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(
            context!!,
            CatBreedDatabase::class.java, "CatInfoDatabase"
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
        lifecycleScope.launch {
            favorites = db.dao.getFavoriteBreeds()
            binding.composeViewFavs.apply {
                setContent {
                    FavoriteList()
                }
            }
        }
    }

    @Composable
    fun FavoriteList(){
        var text = ""
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(3),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp)
            ){
                when(favorites) {
                    else -> items(favorites) { item ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Box(contentAlignment = Alignment.TopEnd) {

                                AsyncImage(
                                    model = item.url,
                                    contentDescription = null
                                )

                                FavoriteButton(modifier = Modifier.padding(12.dp))

                            }
                            Text(
                                text = item.name,
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun FavoriteButton(
        modifier: Modifier = Modifier,
        color: Color = Color.Red,
    ) {

        var isFavorite by remember { mutableStateOf(true) }

        IconToggleButton(
            checked = isFavorite,
            onCheckedChange = {
                isFavorite = !isFavorite
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
}