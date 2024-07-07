package dev.ivanravasi.piggy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.color.DynamicColors
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.GsonProvider
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.data.DataStoreRepository
import dev.ivanravasi.piggy.databinding.FragmentHomeBinding
import dev.ivanravasi.piggy.ui.accounts.AccountAdapter
import dev.ivanravasi.piggy.ui.charts.ChartAdapter
import kotlinx.coroutines.runBlocking


class HomeFragment : Fragment() {
    private lateinit var navController: NavController
    private lateinit var dataStoreRepository: DataStoreRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        navController = findNavController()
        dataStoreRepository = DataStoreRepository(requireContext())

        val viewModel = HomeViewModel(dataStoreRepository, navController)
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        if (DynamicColors.isDynamicColorAvailable()) {
            runBlocking {
                binding.switchTheme.isChecked = dataStoreRepository.isMaterialYouEnabled() ?: false
            }
            binding.switchTheme.setOnCheckedChangeListener { _, _ ->
                runBlocking {
                    dataStoreRepository.toggleMaterialYou()
                    requireActivity().recreate()
                }
            }
        } else {
            binding.switchTheme.visibility = View.GONE
        }

        binding.btnLogout.setOnClickListener {
            viewModel.revokeToken {
                navController.navigate(R.id.authActivity)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            if (it.isNotEmpty())
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loadingProgress.visibility = View.VISIBLE

                binding.subtitleRecentAccounts.visibility = View.GONE
                binding.listRecentAccounts.visibility = View.GONE
                binding.listCharts.visibility = View.GONE
            } else {
                binding.loadingProgress.visibility = View.GONE

                binding.subtitleRecentAccounts.visibility = View.VISIBLE
                binding.listRecentAccounts.visibility = View.VISIBLE
                binding.listCharts.visibility = View.VISIBLE
            }
        }

        val adapter = AccountAdapter {
            val bundle = Bundle()
            bundle.putString("account", GsonProvider.getSerializer().toJson(it))
            navController.navigate(R.id.navigation_operations, bundle)
        }
        binding.listRecentAccounts.adapter = adapter
        viewModel.accounts.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        val chartAdapter = ChartAdapter()
        binding.listCharts.adapter = chartAdapter
        viewModel.favoriteCharts.observe(viewLifecycleOwner) {
            chartAdapter.submitList(it)
        }

        return binding.root
    }
}