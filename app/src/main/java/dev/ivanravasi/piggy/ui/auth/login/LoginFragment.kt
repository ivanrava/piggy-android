package dev.ivanravasi.piggy.ui.auth.login

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.RetrofitClient
import dev.ivanravasi.piggy.api.bodies.TokenCreateRequest
import dev.ivanravasi.piggy.data.DataStoreManager
import dev.ivanravasi.piggy.databinding.FragmentLoginBinding
import dev.ivanravasi.piggy.ui.auth.ViewUtils
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: FragmentLoginBinding
    private lateinit var navController: NavController
    private lateinit var dataStore: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        navController = findNavController()
        dataStore = DataStoreManager(requireContext())

        runBlocking {
            val token = dataStore.getToken()
            if (token != null)
                navController.navigate(R.id.action_loginFragment_to_mainActivity)
        }

        binding.linkRegister.apply {
            text = ViewUtils.underlineText(text)
            setOnClickListener {
                navController.navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }

        binding.buttonSignIn.setOnClickListener {
            val email = binding.inputEmail.editText!!.text.toString()
            val password = binding.inputPassword.editText!!.text.toString()
            val domain = binding.inputInstanceDomain.editText!!.text.toString()
            val deviceName = Build.MANUFACTURER + " " + Build.MODEL
            val remember = binding.switchRemember.isChecked
            // TODO: validate domain
            requestToken(domain, email, password, deviceName, remember)
        }

        return binding.root
    }

    private fun requestToken(
        domain: String,
        email: String,
        password: String,
        deviceName: String,
        remember: Boolean
    ) {
        val piggyApi = RetrofitClient.getInstance(domain)
        lifecycleScope.launch {
            try {
                val response = piggyApi.token(TokenCreateRequest(email, password, deviceName))
                if (response.isSuccessful) {
                    val token = response.body()!!.token
                    dataStore.saveAuthData(token, domain, remember)
                    navController.navigate(R.id.action_loginFragment_to_mainActivity)
                } else {
                    if (response.code() == 422) {
                        setErrorBadCredentials()
                    } else {
                        Toast.makeText(
                            context,
                            "Error ${response.code()}. Please contact the app developer.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, e.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setErrorBadCredentials() {
        binding.inputEmail.error = "Bad credentials"
        binding.inputPassword.error = "Bad credentials"
    }
}