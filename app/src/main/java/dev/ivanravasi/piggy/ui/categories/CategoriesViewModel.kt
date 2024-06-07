package dev.ivanravasi.piggy.ui.categories

import androidx.lifecycle.viewModelScope
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Category
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.ui.common.IndexApiViewModel
import kotlinx.coroutines.launch

class CategoriesViewModel(
    tokenRepository: TokenRepository
) : IndexApiViewModel<Category>(tokenRepository) {
    init {
        viewModelScope.launch {
            hydrateApiClient()
            getProperties()
        }
    }

    private fun getProperties() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = piggyApi.categoryTrees("Bearer ${tokenRepository.getToken()}")
                _objList.value = response.body()!!.data.sortedBy { it.name }
            } catch (e: Exception) {
//                Toast.makeText(context, e.localizedMessage, Toast.LENGTH_LONG).show()
            }
            _isLoading.value = false
        }
    }
}