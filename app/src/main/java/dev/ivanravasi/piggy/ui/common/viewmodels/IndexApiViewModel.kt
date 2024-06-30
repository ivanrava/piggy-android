package dev.ivanravasi.piggy.ui.common.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dev.ivanravasi.piggy.data.TokenRepository
import kotlinx.coroutines.launch

abstract class IndexApiViewModel<T>(
    val tokenRepository: TokenRepository,
    navController: NavController
): ApiViewModel(tokenRepository, navController) {
    fun delete(id: Long, resources: String) {
        viewModelScope.launch {
            tryApiRequest("delete_$resources") {
                piggyApi.delete("Bearer ${tokenRepository.getToken()}", id, resources)
                refreshContents()
            }
        }
    }

    abstract suspend fun refreshContents()

    protected val _objList = MutableLiveData<List<T>>().apply {
        value = emptyList()
    }
    val objList: LiveData<List<T>> = _objList
    protected val _isLoading = MutableLiveData<Boolean>().apply { value = true }
    val isLoading: LiveData<Boolean> = _isLoading
}