package kr.valor.bal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kr.valor.bal.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var binding: ActivityMainBinding

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = host.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.home_dest, R.id.schedule_dest, R.id.overview_dest)
        )

        setupBottomNavMenu(navController)

        setupActionBarWithNavController(navController, appBarConfiguration)

        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
    }

    override fun onOptionsItemSelected(menuItem : MenuItem) : Boolean {
        if (menuItem.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true // must return true to consume it here
        }
        return super.onOptionsItemSelected(menuItem)
    }

    private fun setupBottomNavMenu(navController: NavController) {
        bottomNavigationView = binding.bottomNavView
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.schedule_detail_dest -> {
                    bottomNavigationView.visibility = View.GONE
                    supportActionBar!!.hide()
                }

                R.id.home_dest -> {
                    supportActionBar!!.hide()
                }

                R.id.edit_dest -> {
                    bottomNavigationView.visibility = View.GONE
                    supportActionBar!!.show()
                }

                else -> {
                    bottomNavigationView.visibility = View.VISIBLE
                    supportActionBar!!.show()
                }
            }
        }
        bottomNavigationView.setupWithNavController(navController)
    }
}