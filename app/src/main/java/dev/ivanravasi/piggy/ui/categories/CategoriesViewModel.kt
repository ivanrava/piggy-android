package dev.ivanravasi.piggy.ui.categories

import android.util.Log
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
            _isLoading.value = true
            getCategoryTrees()
            getBudgets()
            _isLoading.value = false
        }
    }

    private suspend fun getCategoryTrees() {
        try {
            val response = piggyApi.categoryTrees("Bearer ${tokenRepository.getToken()}")
            _objList.value = response.body()!!.data.sortedBy { it.name }
        } catch (e: Exception) {
            Log.e("categories", e.toString())
        }
    }

    private suspend fun getBudgets() {
        try {
            // TODO: parametrize with current year
            val response = piggyApi.budget("Bearer ${tokenRepository.getToken()}", 2024)
            if (response.isSuccessful) {
                _objList.value = _objList.value!!.map { parent ->
                    parent.children = parent.children.map { child ->
                        child.budget = response.body()!!.data.find { it.id == child.id }!!.budget
                        child
                    }
                    parent
                }
            }
        } catch (e: Exception) {
            Log.e("budget", e.toString())
        }
    }
}