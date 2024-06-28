package dev.ivanravasi.piggy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.RetrofitClient
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.FragmentHomeBinding
import dev.ivanravasi.piggy.ui.accounts.AccountAdapter
import dev.ivanravasi.piggy.ui.accounts.OnAccountClickListener
import dev.ivanravasi.piggy.ui.charts.ChartAdapter
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var navController: NavController
    private lateinit var tokenRepository: TokenRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        navController = findNavController()
        tokenRepository = TokenRepository(requireContext())

        val viewModel = HomeViewModel(tokenRepository)
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.btnLogout.setOnClickListener {
            revokeToken()
        }

        val adapter = AccountAdapter(object : OnAccountClickListener {
            override fun onAccountClick(account: Account) {
                val bundle = Bundle()
                bundle.putLong("id", account.id)
                navController.navigate(R.id.navigation_operations, bundle)
            }
        })
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

    private fun revokeToken() {
        lifecycleScope.launch {
            val domain = tokenRepository.getDomain()!!
            val token = tokenRepository.getToken()!!
            val piggyApi = RetrofitClient.getPiggyInstance(domain)
            try {
                val response = piggyApi.revoke("Bearer $token")
                if (response.isSuccessful) {
                    tokenRepository.deleteToken()
                    navController.navigate(R.id.authActivity)
                } else {
                    Toast.makeText(
                        context,
                        "Error ${response.code()}. Please contact the app developer.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, e.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
}