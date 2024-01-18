package com.catexplorer.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import coil.compose.AsyncImage
import com.catexplorer.R
import com.catexplorer.data.CatBreedInfo
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

    private lateinit var binding: FragmentCatBreedListBinding
    private val viewModel : CatBreedViewModel by activityViewModels()
    private var breedNumberReturned = 20
    private var hasBreeds = 1
    private var page = 1
    private var listBreedInfo: ArrayList<CatBreedInfo> = ArrayList()
    private var listBreed : ArrayList<CatMainInfo>? = ArrayList()
    private var favoritesList : ArrayList<CatMainInfo>? = ArrayList()
    private lateinit var db: CatBreedDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            db = Room.databaseBuilder(
                context!!,
                CatBreedDatabase::class.java, DATABASE_NAME
            ).build()
        }catch (e: Exception){
            e.printStackTrace()
            Toast.makeText(requireContext(), getString(R.string.generic_database_access_error), Toast.LENGTH_SHORT).show()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {
            binding = FragmentCatBreedListBinding.inflate(
                inflater,
                container,
                false
            )
            return binding.root
        }catch (e : Exception){
            Toast.makeText(requireContext(), getString(R.string.generic_binding_initialization_error), Toast.LENGTH_SHORT).show()
            return onCreateView(inflater, container, savedInstanceState)
        }
    }

    override fun onResume() {
        super.onResume()
        populateFavoritesList()
        if((activity as CatBreedMainActivity).checkInternetConnection()) {
            if(!listBreed.isNullOrEmpty()){
                initializeUIComponents()
            }else {
                makeAllApiCalls(true)
                populateListsFromApi(addToDB = true, clearList = true)
            }
        }else{
            accessListInDatabase()
        }
    }

    private fun populateFavoritesList(){
        if(viewModel.getFavoritesInformation().value.isNullOrEmpty()){
            favoritesList!!.clear()
            viewModel.getAllFavoritesFromDatabase(db)
            viewModel.getFavoritesInformation().observe(this, Observer{
                favoritesList = it
            })
        }else{
            favoritesList = viewModel.getFavoritesInformation().value
        }
    }

    private fun accessListInDatabase(){
        try {
            viewModel.getAllDataFromDatabase(db)
            viewModel.getCatInformation().observe(this, Observer{
                if (!it.isNullOrEmpty()) {
                    listBreed = it
                    binding.composeView.apply {
                        setContent {
                            BreedsScreen()
                        }
                    }
                } else {
                    binding.composeView.apply {
                        setContent {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = getString(R.string.no_internet_connection_error),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = getString(R.string.please_connect_message),
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }
            })
        }catch (e : Exception){
            e.printStackTrace()
            Toast.makeText(requireContext(), getString(R.string.generic_no_access_data_error), Toast.LENGTH_SHORT).show()
        }
    }
    private fun makeAllApiCalls(callBreeds: Boolean){
        viewModel.getCatList(breedNumberReturned, hasBreeds)
        if(callBreeds) {
            viewModel.getAllBreeds()
        }
        viewModel.observeCatListLiveData().observe(viewLifecycleOwner, Observer { list ->
            if(!list.isNullOrEmpty()) {
                for (i in list.indices) {
                    viewModel.getCatBreedInfo(list[i].id)
                }
            }else{
                binding.composeView.apply {
                    setContent {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = getString(R.string.service_error), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        })
    }

    private fun populateListsFromApi(addToDB: Boolean, clearList: Boolean){
        if(clearList) {
            listBreed!!.clear()
        }
        viewModel.observeCatBreedInfoLiveData().observe(viewLifecycleOwner, Observer {
            if(!listBreed!!.contains(it)){
                listBreed?.add(it)
            }
            if(addToDB && it.catBreedInfo.isNotEmpty()) {
                viewModel.insertDataIntoDatabase(db, it)
            }
            if (listBreed?.size == breedNumberReturned * page) {
                viewModel.setCatInformation(listBreed!!)
                initializeUIComponents()
            }
        })
        viewModel.observeCatBreedListLiveData().observe(this, Observer {
            listBreedInfo = it
        })
    }

    private fun initializeUIComponents(){
        binding.composeView.apply {
            setContent {
                BreedsScreen()
            }
        }
    }

    @Composable
    fun BreedsScreen() {
        var list by remember { mutableStateOf<ArrayList<CatMainInfo>>(ArrayList()) }
        var hasResults by remember { mutableStateOf<Boolean>(true) }
        list = listBreed!!
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            SearchBarField(onListChanged = { searchList, searchNoResults ->
                if(!searchList.isNullOrEmpty()) {
                    listBreed = searchList
                    viewModel.setCatInformation(searchList)
                    list = searchList
                }
                hasResults = searchNoResults
            })
            if(hasResults) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    when (list) {
                        null -> {}
                        else -> itemsIndexed(list) { index, item ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.clickable {
                                    navigateToDetailsScreen(index)
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
                                    text = item.catBreedInfo[0].name!!,
                                )
                            }
                        }
                    }
                    item {
                        LaunchedEffect(true) {
                            if((activity as CatBreedMainActivity).checkInternetConnection()) {
                                page++
                                makeAllApiCalls(false)
                                populateListsFromApi(addToDB = true, clearList = false)
                            }
                        }
                    }
                }
            }else{
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = getString(R.string.no_results_error), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    private fun navigateToDetailsScreen(position: Int){
        val bundle = Bundle()
        bundle.putInt(CatBreedDetailsFragment.POSITION, position)
        findNavController().navigate(
            R.id.action_catBreedListFragment_to_catBreedDetailsFragment,
            bundle
        )
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
                if(isFavorite){
                    favoritesList!!.add(listBreed!![position])
                }else{
                    favoritesList!!.remove(listBreed!![position])
                }
                listBreed!![position].favorite = isFavorite
                viewModel.setFavoritesInformation(favoritesList!!)
                viewModel.insertDataIntoDatabase(db, listBreed!![position])
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
                if(filteredList == null){
                    filteredList = ArrayList()
                }
                if((activity as CatBreedMainActivity).checkInternetConnection()) {
                    var breedId = ""
                    for (i in listBreedInfo) {
                        if (i.name!!.contains(text)) {
                            breedId = if (breedId.isEmpty()) {
                                i.id!!
                            } else {
                                breedId + "," + i.id!!
                            }
                        }
                    }
                    if (!breedId.isEmpty()) {
                        viewModel.getImagesByBreed(breedNumberReturned, breedId)
                        viewModel.observeCatImagesByBreedLiveData().observe(this, Observer {
                            filteredList = it
                            onListChanged(filteredList, true)
                            active = false
                        })
                    } else {
                        filteredList = viewModel.getCatInformation().value
                        onListChanged(filteredList, false)
                        active = false
                    }
                }else{
                    if(text.isNotEmpty()){
                        viewModel.getBreedsByNameFromDatabase(db, text)
                        viewModel.getCatInformation().observe(this, Observer {
                            filteredList = it
                            active = false
                        })
                    }else{
                        viewModel.getAllDataFromDatabase(db)
                    }
                }
            },
            active = active,
            onActiveChange ={
                active = it
            },
            placeholder = {
                Text(text = getString(R.string.search_label))
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = getString(R.string.search_content_description))
            },
            trailingIcon = {
                if(active) {
                    Icon(
                        modifier = Modifier.clickable {
                            if(text.isNotEmpty()) {
                                text = ""
                                if((activity as CatBreedMainActivity).checkInternetConnection()){
                                    populateListsFromApi(addToDB = false, clearList = false)
                                }else{
                                    accessListInDatabase()
                                }
                            }else{
                                active = false
                            }
                        },
                        imageVector = Icons.Default.Close,
                        contentDescription = getString(R.string.close_content_description)
                    )
                }
            },
            content = {}
        )
    }

    companion object{
        const val DATABASE_NAME = "CatInfoDatabase"
    }
}
