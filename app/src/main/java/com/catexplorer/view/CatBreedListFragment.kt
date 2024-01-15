package com.catexplorer.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import androidx.room.RoomDatabase
import coil.compose.AsyncImage
import com.catexplorer.R
import com.catexplorer.data.CatInfoTable
import com.catexplorer.data.CatMainInfo
import com.catexplorer.databinding.FragmentCatBreedListBinding
import com.catexplorer.utils.CatBreedDao
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
    private var listBreed : ArrayList<CatMainInfo>? = ArrayList()
    private lateinit var db: CatBreedDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(
            context!!,
            CatBreedDatabase::class.java, "database-name"
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
            viewModel = ViewModelProvider(this)[CatBreedViewModel::class.java]
            viewModel.getCatList(breedNumberReturned, hasBreeds)
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
        }
    }

    @Composable
    fun BreedsScreen(){
        var text = ""
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TextField(
                value = "",
                onValueChange = {text = it},
                label = { Text(text = "Search") },
                modifier = Modifier
                    .fillMaxWidth()
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
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.clickable {
                                val bundle = Bundle()
                                bundle.putSerializable("BreedInfo", item)
                                findNavController().navigate(R.id.action_catBreedListFragment_to_catBreedDetailsFragment, bundle)

                            }
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