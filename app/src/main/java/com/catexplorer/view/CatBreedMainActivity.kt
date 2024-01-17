package com.catexplorer.view

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.catexplorer.R
import com.catexplorer.databinding.ActivityMainBinding
import com.catexplorer.utils.NetworkUtils
import com.catexplorer.viewModel.CatBreedViewModel


class CatBreedMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration : AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private val networkUtils = NetworkUtils(this)
    private val viewModel: CatBreedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        navController = findNavController(R.id.fragment)
        drawerLayout = findViewById(R.id.my_drawer_layout)
        binding.navigationView.setupWithNavController(navController)

        val topLevel = HashSet<Int>()
        topLevel.add(R.id.breedFavoritesFragment)
        topLevel.add(R.id.catBreedListFragment)

        appBarConfiguration = AppBarConfiguration.Builder(topLevel).setOpenableLayout(drawerLayout).build()
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun checkInternetConnection() : Boolean{
        return networkUtils.isNetworkAvailable()
    }
}
