package com.catexplorer.view

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.catexplorer.R
import com.catexplorer.data.CatMainInfo
import com.catexplorer.viewModel.CatBreedViewModel
import androidx.compose.foundation.layout.Column as Column1

class CatBreedMainActivity : AppCompatActivity() {

    private lateinit var viewModel: CatBreedViewModel
    private var breedNumberReturned = 10
    private var hasBreeds = 1
    private var listBreed : ArrayList<CatMainInfo>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observeViewModel()
    }

    private fun observeViewModel(){
        viewModel = ViewModelProvider(this)[CatBreedViewModel::class.java]
        viewModel.getCatList(breedNumberReturned, hasBreeds)
        viewModel.observeCatListLiveData().observe(this, Observer { list ->
            for(i in list.indices){
                viewModel.getCatBreedInfo(list[i].id)
            }
            viewModel.observeCatBreedInfoLiveData().observe(this, Observer {
                this@CatBreedMainActivity.listBreed?.add(it)
                if (listBreed?.size == list.size) {
                    setContent {
                        TabScreen()
                    }
                }
            })
        })
    }

    @Composable
    fun TabScreen() {
        var tabIndex by remember { mutableStateOf(0) }

        val tabs = listOf("Breeds", "Favorites")

        Column1(modifier = Modifier.fillMaxWidth()) {
            TabRow(selectedTabIndex = tabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(text = { Text(title) },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index }
                    )
                }
            }
            when (tabIndex) {
                0 -> BreedsScreen()
            }
        }
    }

    @Composable
    fun BreedsScreen(){
        var text = ""
        Column1(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            TextField(
                value = "",
                onValueChange = {text = it},
                label = { Text(text = "Search")},
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp)
            )
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(3),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp)
            ){
                when(listBreed) {
                    null -> {}
                    else -> items(listBreed!!) { item ->
                        Column1(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            AsyncImage(
                                model = item.url,
                                contentDescription = null
                            )
                            Text(
                                text = item.catBreedInfo!![0].name!!,
                            )
                        }
                    }
                }
            }
        }
    }
}
