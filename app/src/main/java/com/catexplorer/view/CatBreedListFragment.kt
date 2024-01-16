package com.catexplorer.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import coil.compose.AsyncImage
import com.catexplorer.R
import com.catexplorer.data.CatBreedInfo
import com.catexplorer.data.CatInfoTable
import com.catexplorer.data.CatMainInfo
import com.catexplorer.databinding.FragmentCatBreedListBinding
import com.catexplorer.utils.CatBreedDatabase
import com.catexplorer.viewModel.CatBreedViewModel
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [CatBreedListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CatBreedListFragment : Fragment() {

    private var _binding: FragmentCatBreedListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CatBreedViewModel
    private var breedNumberReturned = 20
    private var hasBreeds = 1
    private var listBreedInfo: ArrayList<CatBreedInfo> = ArrayList()
    private var listBreed : ArrayList<CatMainInfo>? = ArrayList()
    private lateinit var db: CatBreedDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(
            context!!,
            CatBreedDatabase::class.java, "CatInfoDatabase"
        ).build()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatBreedListBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        observeViewModel()
    }

    private fun observeViewModel(){
        if(!listBreed.isNullOrEmpty()){
            binding.composeView.apply {
                setContent {
                    BreedsScreen()
                }
            }
        }else {
            viewModel = ViewModelProvider(requireActivity())[CatBreedViewModel::class.java]
            viewModel.getCatList(breedNumberReturned, hasBreeds)
            viewModel.getAllBreeds()
            viewModel.observeCatListLiveData().observe(viewLifecycleOwner, Observer { list ->
                for (i in list.indices) {
                    viewModel.getCatBreedInfo(list[i].id)
                }
                viewModel.observeCatBreedInfoLiveData().observe(viewLifecycleOwner, Observer {
                    listBreed?.add(it)
                    lifecycleScope.launch {
                        val breed = CatInfoTable(
                            it.id,
                            it.url,
                            it.width,
                            it.height,
                            it.catBreedInfo!![0].life_span!!,
                            it.catBreedInfo!![0].name!!,
                            it.catBreedInfo!![0].origin!!,
                            it.catBreedInfo!![0].temperament!!,
                            it.catBreedInfo!![0].description!!,
                            false
                        )
                        db.dao.insertBreed(breed)
                    }
                    if (listBreed?.size == list.size) {
                        binding.composeView.apply {
                            setContent {
                                BreedsScreen()
                            }
                        }
                    }
                })
            })
            viewModel.observeCatBreedListLiveData().observe(this, Observer {
                listBreedInfo = it
            })
        }
    }

    @Composable
    fun BreedsScreen() {
        var list by remember { mutableStateOf<ArrayList<CatMainInfo>?>(null) }
        var hasResults by remember { mutableStateOf<Boolean>(true) }
        list = listBreed
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            SearchBarField(onListChanged = { searchList, searchNoResults ->
                if(list.isNullOrEmpty()) {
                    listBreed = searchList
                    list = searchList
                }
                hasResults = searchNoResults
            })
            if(hasResults) {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(3),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    when (list) {
                        null -> {}
                        else -> itemsIndexed(list!!) { index, item ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.clickable {
                                    viewModel.setCatInformation(listBreed!!)
                                    var bundle = Bundle()
                                    bundle.putInt("Position", index)
                                    findNavController().navigate(
                                        R.id.action_catBreedListFragment_to_catBreedDetailsFragment,
                                        bundle
                                    )
                                }
                            ) {
                                Box(contentAlignment = Alignment.TopEnd) {

                                    AsyncImage(
                                        model = item.url,
                                        contentDescription = null
                                    )

                                    FavoriteButton(
                                        modifier = Modifier.padding(12.dp),
                                        position = index
                                    )

                                }
                                Text(
                                    text = item.catBreedInfo!![0].name!!,
                                )
                            }
                        }
                    }
                }
            }else{
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No results found!", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    @Composable
    fun FavoriteButton(
        modifier: Modifier = Modifier,
        color: Color = Color.Gray,
        position: Int
    ) {

        var isFavorite by remember { mutableStateOf(listBreed!![position].favorite) }

        IconToggleButton(
            checked = isFavorite,
            onCheckedChange = {
                isFavorite = !isFavorite
                listBreed!![position].favorite = isFavorite
                lifecycleScope.launch {
                    val breed = CatInfoTable(
                        listBreed!![position].id,
                        listBreed!![position].url,
                        listBreed!![position].width,
                        listBreed!![position].height,
                        listBreed!![position].catBreedInfo!![0].life_span!!,
                        listBreed!![position].catBreedInfo!![0].name!!,
                        listBreed!![position].catBreedInfo!![0].origin!!,
                        listBreed!![position].catBreedInfo!![0].temperament!!,
                        listBreed!![position].catBreedInfo!![0].description!!,
                        listBreed!![position].favorite
                    )
                    db.dao.insertBreed(breed)
                }
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SearchBarField(onListChanged: (ArrayList<CatMainInfo>?, Boolean) -> Unit){
        var text by remember { mutableStateOf("") }
        var active by remember { mutableStateOf(false) }
        var filteredList by remember { mutableStateOf<ArrayList<CatMainInfo>?>(null) }
        SearchBar(
            modifier = Modifier.fillMaxWidth(),
            query = text,
            onQueryChange= {
                text = it
            },
            onSearch ={
                var breedId : String = ""
                for(i in listBreedInfo){
                    if(i.name!!.contains(text)){
                        breedId = if(breedId.isEmpty()){
                            i.id!!
                        }else{
                            breedId + "," + i.id!!
                        }
                    }
                }
                if(!breedId.isEmpty()) {
                    viewModel.getImagesByBreed(breedNumberReturned, breedId)
                    viewModel.observeCatImagesByBreedLiveData().observe(this, Observer {
                        filteredList = it
                        onListChanged(it, true)
                        active = false
                    })
                }else{
                    onListChanged(null, false)
                    active = false
                }
            },
            active = active,
            onActiveChange ={
                active = it
            },
            placeholder = {
                Text(text = "Search")
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
            },
            trailingIcon = {
                if(active) {
                    Icon(
                        modifier = Modifier.clickable {
                            if(text.isNotEmpty()) {
                                text = ""
                            }else{
                                active = false
                                filteredList = listBreed
                            }
                        },
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Icon"
                    )
                }
            },
            content = {}
        )
    }
}