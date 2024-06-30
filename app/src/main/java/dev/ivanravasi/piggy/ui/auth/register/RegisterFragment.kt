package dev.ivanravasi.piggy.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dev.ivanravasi.piggy.MainActivity
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.FragmentRegisterBinding
import dev.ivanravasi.piggy.ui.auth.ViewUtils

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val viewModel = RegisterViewModel(TokenRepository(requireContext()))

        navController = findNavController()
        binding.linkLogin.apply {
            text = ViewUtils.underlineText(text)
            setOnClickListener {
                navController.navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }

        binding.inputName.editText!!.doAfterTextChanged { conditionallyDisableButton() }
        binding.inputEmail.editText!!.doAfterTextChanged { conditionallyDisableButton() }
        binding.inputPassword.editText!!.doAfterTextChanged { conditionallyDisableButton() }
        binding.inputPasswordConfirmation.editText!!.doAfterTextChanged { conditionallyDisableButton() }
        binding.inputInstanceDomain.editText!!.doAfterTextChanged { conditionallyDisableButton() }

        binding.buttonSignUp.setOnClickListener {
            val name = binding.inputName.editText!!.text.toString()
            val email = binding.inputEmail.editText!!.text.toString()
            val password = binding.inputPassword.editText!!.text.toString()
            val passwordConfirmation = binding.inputPasswordConfirmation.editText!!.text.toString()
            val domain = binding.inputInstanceDomain.editText!!.text.toString()

            if (password != passwordConfirmation) {
                binding.inputPasswordConfirmation.error = "The passwords do not match"
            }

            // TODO: validate domain
            viewModel.register(domain, name, email, password, passwordConfirmation, {
                startApp()
            }, {
                Toast.makeText(
                    context,
                    "Error ${it}. Please contact the app developer.",
                    Toast.LENGTH_LONG
                ).show()
            })
        }

        return binding.root
    }

    private fun conditionallyDisableButton() {
        binding.buttonSignUp.isEnabled = (
                binding.inputName.editText!!.text.toString().isNotEmpty() &&
                binding.inputEmail.editText!!.text.toString().isNotEmpty() &&
                binding.inputPassword.editText!!.text.toString().isNotEmpty() &&
                binding.inputPasswordConfirmation.editText!!.text.toString().isNotEmpty() &&
                binding.inputInstanceDomain.editText!!.text.toString().isNotEmpty()
                )
    }

    private fun startApp() {
        val intent = Intent(requireView().context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}