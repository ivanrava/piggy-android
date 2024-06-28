package dev.ivanravasi.piggy.ui.iconify

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.ivanravasi.piggy.api.RetrofitClient
import kotlinx.coroutines.launch

class IconPickerViewModel: ViewModel() {
    private val iconifyApi = RetrofitClient.getIconifyInstance()

    private val _isLoading = MutableLiveData<Boolean>().apply { value = false }
    val isLoading: LiveData<Boolean> = _isLoading
    private val _icons = MutableLiveData<List<String>>().apply {
        value = emptyList()
    }
    val icons: LiveData<List<String>> = _icons

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun queryIcons(query: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val res = iconifyApi.search(query)
                val fetchedIcons = res.body()!!.icons
                _icons.value = fetchedIcons
            } catch (e: Exception) {
                _error.value = "An error has been encountered while searching for icons. Please contact the app developer."
                Log.e("iconify", e.toString())
            }
        }
        _isLoading.value = false
    }
}