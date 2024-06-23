package dev.ivanravasi.piggy.ui.charts

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Chart
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Stat
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.ui.common.ApiViewModel
import kotlinx.coroutines.launch


class ChartLoaderViewModel(
    private val tokenRepository: TokenRepository,
): ApiViewModel(tokenRepository) {
    private val _stats = MutableLiveData<List<Stat>>().apply { value = emptyList() }
    val stats: LiveData<List<Stat>> = _stats
    private val statDescriptions = mapOf(
        "count" to "count",
        "avg" to "average",
        "sum" to "sum",
        "max" to "maximum"
    )
    private val intervalDescriptions = mapOf(
        "month" to "Monthly",
        "year" to "Yearly"
    )

    private val _chartName = MutableLiveData<String>().apply { value = "Loading..." }
    val chartName: LiveData<String> = _chartName

    init {
        viewModelScope.launch {
            hydrateApiClient()
        }
    }

    fun requestChart(chart: Chart) {
        viewModelScope.launch {
            requestStats(chart)
            getChartName(chart)
        }
    }

    private suspend fun requestStats(chart: Chart) {
        try {
            val response = if (chart.filter == "all") {
                piggyApi.stats(
                    "Bearer ${tokenRepository.getToken()}",
                    chart.interval,
                    chart.stat
                )
            } else {
                piggyApi.statsFiltered(
                    "Bearer ${tokenRepository.getToken()}",
                    chart.interval,
                    chart.filter,
                    chart.filterId,
                    chart.stat
                )
            }
            _stats.value = response.body()!!
        } catch (e: Exception) {
            Log.e("stats", e.toString())
        }
    }

    private suspend fun getChartName(chart: Chart) {
        when (chart.filter) {
            "accounts" -> getAccount(chart.filterId)
            "categories" -> getCategory(chart.filterId)
            "beneficiaries" -> getBeneficiary(chart.filterId)
            "all" -> _chartName.value = "Global In/Out History"
        }
        _chartName.value = "${_chartName.value} - ${humanStatDescription(chart)}"
    }

    private suspend fun getAccount(accountId: Long) {
        try {
            val response = piggyApi.account(
                "Bearer ${tokenRepository.getToken()}",
                accountId
            )
            _chartName.value = response.body()!!.data.name
        } catch (e: Exception) {
            Log.e("stats", e.toString())
        }
    }

    private suspend fun getCategory(categoryId: Long) {
        try {
            val response = piggyApi.category(
                "Bearer ${tokenRepository.getToken()}",
                categoryId
            )
            _chartName.value = response.body()!!.data.name
        } catch (e: Exception) {
            Log.e("stats", e.toString())
        }
    }

    private suspend fun getBeneficiary(beneficiaryId: Long) {
        try {
            val response = piggyApi.beneficiary(
                "Bearer ${tokenRepository.getToken()}",
                beneficiaryId
            )
            _chartName.value = response.body()!!.data.name
        } catch (e: Exception) {
            Log.e("stats", e.toString())
        }
    }

    private fun humanStatDescription(chart: Chart): String {
        return "${intervalDescriptions[chart.interval]} ${statDescriptions[chart.stat]}"
    }
}