package dev.ivanravasi.piggy.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dev.ivanravasi.piggy.MainActivity
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.data.DataStoreRepository
import dev.ivanravasi.piggy.databinding.FragmentLoginBinding
import dev.ivanravasi.piggy.ui.auth.ViewUtils
import kotlinx.coroutines.runBlocking


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var navController: NavController
    private lateinit var dataStoreRepository: DataStoreRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        navController = findNavController()
        dataStoreRepository = DataStoreRepository(requireContext())
        val viewModel = LoginViewModel(dataStoreRepository)

        runBlocking {
            val token = dataStoreRepository.getToken()
            if (token != null) {
                startApp()
            }
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
            if (!domain.startsWith("http://") && !domain.startsWith("https://")) {
                binding.inputInstanceDomain.error = "Not a valid domain"
                return@setOnClickListener
            } else {
                binding.inputInstanceDomain.error = null
            }
            viewModel.requestToken(domain, email, password, {
                startApp()
            }, {
                if (it == 422) {
                    setErrorBadCredentials()
                } else {
                    Toast.makeText(
                        context,
                        "Error ${it}. Please contact the app developer.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        }

        return binding.root
    }

    private fun setErrorBadCredentials() {
        binding.inputEmail.error = "Bad credentials"
        binding.inputPassword.error = "Bad credentials"
    }

    private fun startApp() {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}