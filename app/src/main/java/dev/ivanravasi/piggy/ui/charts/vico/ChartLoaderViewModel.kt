package dev.ivanravasi.piggy.ui.charts.vico

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Chart
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Stat
import dev.ivanravasi.piggy.data.DataStoreRepository
import dev.ivanravasi.piggy.ui.common.viewmodels.ApiViewModel
import kotlinx.coroutines.launch


class ChartLoaderViewModel(
    private val dataStoreRepository: DataStoreRepository,
    navController: NavController
): ApiViewModel(dataStoreRepository, navController) {
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
        "year" to "Yearly",
        "all" to "Global"
    )

    private val _chartName = MutableLiveData<String>().apply { value = "Loading..." }
    val chartName: LiveData<String> = _chartName

    fun requestChart(chart: Chart) {
        viewModelScope.launch {
            hydrateApiClient()
            requestStats(chart)
            getChartName(chart)
        }
    }

    private suspend fun requestStats(chart: Chart) {
        tryApiRequest("chart_stat") {
            val response = if (chart.filter == "all") {
                piggyApi.stats(
                    "Bearer ${dataStoreRepository.getToken()}",
                    chart.interval,
                    chart.stat
                )
            } else if (chart.filterId != null) {
                piggyApi.statsFiltered(
                    "Bearer ${dataStoreRepository.getToken()}",
                    chart.interval,
                    chart.filter,
                    chart.filterId,
                    chart.stat
                )
            } else {
                piggyApi.statsList(
                    "Bearer ${dataStoreRepository.getToken()}",
                    chart.filter,
                    chart.interval,
                    chart.stat
                )
            }
            _stats.value = response.body()!!
        }
    }

    private suspend fun getChartName(chart: Chart) {
        when (chart.filter) {
            "accounts" -> if (chart.filterId != null) getAccount(chart.filterId) else _chartName.value = "Accounts"
            "categories" -> if (chart.filterId != null) getCategory(chart.filterId) else _chartName.value = "Categories"
            "beneficiaries" -> if (chart.filterId != null) getBeneficiary(chart.filterId) else _chartName.value = "Beneficiaries"
            "all" -> _chartName.value = "Global In/Out History"
        }
        _chartName.value = "${_chartName.value} - ${humanStatDescription(chart)}"
    }

    private suspend fun getAccount(accountId: Long) {
        tryApiRequest("chart_stats_account") {
            val response = piggyApi.account(
                "Bearer ${dataStoreRepository.getToken()}",
                accountId
            )
            _chartName.value = response.body()!!.data.name
        }
    }

    private suspend fun getCategory(categoryId: Long) {
        tryApiRequest("chart_stats_category") {
            val response = piggyApi.category(
                "Bearer ${dataStoreRepository.getToken()}",
                categoryId
            )
            _chartName.value = response.body()!!.data.name
        }
    }

    private suspend fun getBeneficiary(beneficiaryId: Long) {
        tryApiRequest("chart_stats_beneficiary") {
            val response = piggyApi.beneficiary(
                "Bearer ${dataStoreRepository.getToken()}",
                beneficiaryId
            )
            _chartName.value = response.body()!!.data.name
        }
    }

    private fun humanStatDescription(chart: Chart): String {
        return "${intervalDescriptions[chart.interval]} ${statDescriptions[chart.stat]}"
    }
}