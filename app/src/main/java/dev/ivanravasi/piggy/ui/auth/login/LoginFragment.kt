package dev.ivanravasi.piggy.ui.auth.login

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.RetrofitClient
import dev.ivanravasi.piggy.api.bodies.TokenRequest
import dev.ivanravasi.piggy.databinding.FragmentLoginBinding
import dev.ivanravasi.piggy.ui.auth.ViewUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: FragmentLoginBinding
    private lateinit var navController: NavController
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "local-prefs")
    private val TOKEN_KEY = stringPreferencesKey("token")

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

        runBlocking {
            val token = getToken().firstOrNull()
            Log.i("token", token ?: "no token (initial)")
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
            val deviceName = Build.MANUFACTURER + " " + Build.MODEL;
            // TODO: validate domain
            val piggyApi = RetrofitClient.getInstance(domain)
            lifecycleScope.launch {
                try {
                    val response = piggyApi.token(TokenRequest(email, password, deviceName))
                    if (response.isSuccessful) {
                        val token = response.body()!!.token
                        Toast.makeText(context, token, Toast.LENGTH_LONG).show()
                        runBlocking {
                            saveToken(token)
                            Log.i("token", token)
                            val tokenRead = getToken().firstOrNull()
                            Log.i("token", tokenRead ?: "no token after save")
                        }
                        navController.navigate(R.id.action_loginFragment_to_mainActivity)
                    } else {
                        if (response.code() == 422) {
                            binding.inputEmail.error = "Bad credentials"
                            binding.inputPassword.error = "Bad credentials"
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

        return binding.root
    }

    private suspend fun saveToken(token: String) {
        context?.dataStore?.edit {
            it[TOKEN_KEY] = token
        }
    }

    private suspend fun deleteToken() {
        context?.dataStore?.edit {
            it.clear()
        }
    }

    private fun getToken(): Flow<String?> {
        return context?.dataStore?.data?.map {
            it[TOKEN_KEY]
        }!!
    }
}