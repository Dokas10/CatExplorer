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

    private var _binding: FragmentCatBreedListBinding? = null
    private val binding get() = _binding!!
    private val viewModel : CatBreedViewModel by activityViewModels()
    private var breedNumberReturned = 20
    private var hasBreeds = 1
    private var listBreedInfo: ArrayList<CatBreedInfo> = ArrayList()
    private var listBreed : ArrayList<CatMainInfo>? = ArrayList()
    private lateinit var db: CatBreedDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(
            context!!,
            CatBreedDatabase::class.java, DATABASE_NAME
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
        if((activity as CatBreedMainActivity).checkInternetConnection()) {
            if(!listBreed.isNullOrEmpty()){
                initializeUIComponents()
            }else {
                makeAllApiCalls()
                populateListsFromApi(true)
            }
        }else{
            accessListInDatabase()
        }
    }

    private fun accessListInDatabase(){
        lifecycleScope.launch {
            val dbList = db.dao.getAllBreedsInDatabase()
            for(i in dbList){
                listBreed?.add(CatMainInfo(
                    i.id,
                    i.url,
                    i.width,
                    i.height,
                    listOf(CatBreedInfo(id = null, life_span = i.lifespan, name = i.name, origin = i.origin, temperament = i.temperament, description = i.description)),
                    i.isFavorite
                ))
            }
            viewModel.setCatInformation(listBreed!!)
            if(!listBreed.isNullOrEmpty()) {
                binding.composeView.apply {
                    setContent {
                        BreedsScreen()
                    }
                }
            }else{
                binding.composeView.apply {
                    setContent {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = getString(R.string.no_internet_connection_error), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Text(text = getString(R.string.please_connect_message), fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    }
    private fun makeAllApiCalls(){
        viewModel.getCatList(breedNumberReturned, hasBreeds)
        viewModel.getAllBreeds()
        viewModel.observeCatListLiveData().observe(viewLifecycleOwner, Observer { list ->
            if(!list.isNullOrEmpty()) {
                for (i in list.indices) {
                    viewModel.getCatBreedInfo(list[i].id)
                }
            }else{
                binding.composeView.apply {
                    setContent {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = getString(R.string.no_favorites_message), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        })
    }

    private fun populateListsFromApi(addToDB: Boolean){
        listBreed!!.clear()
        viewModel.observeCatBreedInfoLiveData().observe(viewLifecycleOwner, Observer {
            listBreed?.add(it)
            if(addToDB) {
                viewModel.insertDataIntoDatabase(db, it)
            }
            if (listBreed?.size == breedNumberReturned) {
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
        var list by remember { mutableStateOf<ArrayList<CatMainInfo>?>(null) }
        var hasResults by remember { mutableStateOf<Boolean>(true) }
        list = listBreed
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
                listBreed!![position].favorite = isFavorite
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
                        filteredList = ArrayList()
                        onListChanged(filteredList, false)
                        active = false
                    }
                }else{
                    lifecycleScope.launch {
                        val tempList = db.dao.getBreedsInDatabaseByName(text)
                        for (i in tempList) {
                            filteredList?.add(
                                CatMainInfo(
                                    i.id,
                                    i.url,
                                    i.width,
                                    i.height,
                                    listOf(
                                        CatBreedInfo(
                                            i.breedId,
                                            i.name,
                                            i.temperament,
                                            i.origin,
                                            i.description,
                                            i.lifespan
                                        )
                                    ),
                                    i.isFavorite
                                )
                            )
                        }
                        onListChanged(filteredList, true)
                        active = false
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
                                    populateListsFromApi(false)
                                }else{
                                    accessListInDatabase()
                                }
                                filteredList = listBreed
                                onListChanged(filteredList, true)
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
