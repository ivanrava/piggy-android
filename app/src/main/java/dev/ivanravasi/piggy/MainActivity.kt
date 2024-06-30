package dev.ivanravasi.piggy

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.ActivityMainBinding
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var tokenRepository: TokenRepository
    private val fabActions = mapOf(
        R.id.navigation_accounts to R.id.navigation_add_account,
        R.id.navigation_beneficiaries to R.id.navigation_add_beneficiary,
        R.id.navigation_categories to R.id.navigation_add_category,
        R.id.navigation_properties to R.id.navigation_add_property,
        R.id.navigation_home to R.id.navigation_add_chart
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        tokenRepository = TokenRepository(this@MainActivity)
        runBlocking {
            if (tokenRepository.getToken() == null) {
                val intent = Intent(this@MainActivity, AuthActivity::class.java)
                startActivity(intent)
            }
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.fabTransaction.setOnClickListener {
            navController.navigate(R.id.navigation_add_transaction, getCurrentBundle())
            binding.fabTransaction.hide()
            binding.fabTransfer.hide()
        }
        binding.fabTransfer.setOnClickListener {
            navController.navigate(R.id.navigation_add_transfer, getCurrentBundle())
            binding.fabTransaction.hide()
            binding.fabTransfer.hide()
        }

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            setupFab(
                controller,
                destination
            )
        }

        navView.setupWithNavController(navController)
    }

    private fun getCurrentBundle(): Bundle? {
        val fragment = supportFragmentManager.primaryNavigationFragment
        if (fragment is NavHostFragment) {
            val childFragment = fragment.childFragmentManager.fragments[0]
            return childFragment.requireArguments()
        }
        return null
    }

    private fun setupFab(navController: NavController, destination: NavDestination) {
        val currentId = destination.id
        val destinationId = fabActions[currentId]
        if (destinationId == null) {
            binding.fab.hide()
            if (currentId == R.id.navigation_operations) {
                binding.fabTransaction.show()
                binding.fabTransfer.show()
            } else {
                binding.fabTransaction.hide()
                binding.fabTransfer.hide()
            }
        } else {
            binding.fabTransaction.hide()
            binding.fabTransfer.hide()

            binding.fab.show()
            binding.fab.setOnClickListener {
                navController.navigate(destinationId)
                binding.fab.hide()
            }
        }
    }
}