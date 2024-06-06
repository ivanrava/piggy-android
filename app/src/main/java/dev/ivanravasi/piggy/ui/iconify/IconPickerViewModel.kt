package dev.ivanravasi.piggy.ui.iconify

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

    fun queryIcons(query: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val res = iconifyApi.search(query)
                val fetchedIcons = res.body()!!.icons
                _icons.value = fetchedIcons
            } catch (e: Exception) {
//                Toast.makeText(context, e.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
        _isLoading.value = false
    }
}