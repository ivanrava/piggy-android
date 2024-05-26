package dev.ivanravasi.piggy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.RetrofitClient
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var tokenRepository: TokenRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        navController = findNavController()
        tokenRepository = TokenRepository(requireContext())

        binding.buttonLogout.setOnClickListener {
            revokeToken()
        }

        return root
    }

    private fun revokeToken() {
        lifecycleScope.launch {
            val domain = tokenRepository.getDomain()!!
            val token = tokenRepository.getToken()!!
            val piggyApi = RetrofitClient.getInstance(domain)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}