package dev.ivanravasi.piggy.ui.categories

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Category
import dev.ivanravasi.piggy.data.DataStoreRepository
import dev.ivanravasi.piggy.ui.common.viewmodels.IndexApiViewModel
import kotlinx.coroutines.launch

class CategoriesViewModel(
    dataStoreRepository: DataStoreRepository,
    navController: NavController
) : IndexApiViewModel<Category>(dataStoreRepository, navController) {
    init {
        viewModelScope.launch {
            hydrateApiClient()
            refreshContents()
        }
    }

    private suspend fun getCategoryTrees() {
        tryApiRequest("categories") {
            val response = piggyApi.categoryTrees("Bearer ${dataStoreRepository.getToken()}")
            _objList.value = response.body()!!.data.sortedBy { it.name }
        }
    }

    private suspend fun getBudgets() {
        tryApiRequest("budget") {
            // TODO: parametrize with current year
            val response = piggyApi.budget("Bearer ${dataStoreRepository.getToken()}", 2024)
            if (response.isSuccessful) {
                _objList.value = _objList.value!!.map { parent ->
                    parent.children = parent.children.map { child ->
                        val objWithBudgetData = response.body()!!.data.find { it.id == child.id }!!
                        child.budget = objWithBudgetData.budget
                        child.expenditures = objWithBudgetData.expenditures
                        child
                    }
                    parent
                }
            }
        }
    }

    override suspend fun refreshContents() {
        _isLoading.value = true
        getCategoryTrees()
        getBudgets()
        _isLoading.value = false
    }
}