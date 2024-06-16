package dev.ivanravasi.piggy

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var tokenRepository: TokenRepository
    private val fabActions = mapOf(
        R.id.navigation_accounts to R.id.navigation_add_account,
        R.id.navigation_beneficiaries to R.id.navigation_add_beneficiary,
        R.id.navigation_properties to R.id.navigation_add_property,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tokenRepository = TokenRepository(this@MainActivity)

        val navView: BottomNavigationView = binding.navView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.fabTransaction.setOnClickListener {
            navController.navigate(R.id.navigation_add_transaction)
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

    private fun setupFab(navController: NavController, destination: NavDestination) {
        val currentId = destination.id
        val destinationId = fabActions[currentId]
        if (destinationId == null) {
            binding.fab.hide()
            if (currentId == R.id.navigation_operations) {
                binding.fabTransaction.show()
                binding.fabTransfer.show()
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

    override fun onDestroy() {
        super.onDestroy()
        lifecycleScope.launch {
            if (!tokenRepository.shouldRemember())
                tokenRepository.deleteToken()
        }
    }
}