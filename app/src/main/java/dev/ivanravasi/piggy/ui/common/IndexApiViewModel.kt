package dev.ivanravasi.piggy.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.ivanravasi.piggy.data.TokenRepository

open class IndexApiViewModel<T>(
    val tokenRepository: TokenRepository
): ApiViewModel(tokenRepository) {
    protected val _objList = MutableLiveData<List<T>>().apply {
        value = emptyList()
    }
    val objList: LiveData<List<T>> = _objList
    protected val _isLoading = MutableLiveData<Boolean>().apply { value = true }
    val isLoading: LiveData<Boolean> = _isLoading
}