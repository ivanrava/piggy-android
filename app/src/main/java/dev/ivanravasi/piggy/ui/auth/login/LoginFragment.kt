package dev.ivanravasi.piggy.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.FragmentLoginBinding
import dev.ivanravasi.piggy.ui.auth.ViewUtils
import kotlinx.coroutines.runBlocking


class LoginFragment : Fragment() {
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: FragmentLoginBinding
    private lateinit var navController: NavController
    private lateinit var tokenRepository: TokenRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        navController = findNavController()
        tokenRepository = TokenRepository(requireContext())

        runBlocking {
            val token = tokenRepository.getToken()
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
            // TODO: validate domain
            viewModel.requestToken(domain, email, password, {
                navController.navigate(R.id.action_loginFragment_to_mainActivity)
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
}