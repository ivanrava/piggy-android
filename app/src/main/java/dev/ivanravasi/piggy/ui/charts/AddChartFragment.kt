package dev.ivanravasi.piggy.ui.charts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButtonToggleGroup
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.dicebear.loadBeneficiary
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Beneficiary
import dev.ivanravasi.piggy.api.piggy.bodies.requests.ChartRequest
import dev.ivanravasi.piggy.data.DataStoreRepository
import dev.ivanravasi.piggy.databinding.FragmentAddChartBinding
import dev.ivanravasi.piggy.ui.accounts.OnAccountClickListener
import dev.ivanravasi.piggy.ui.backWithSnackbar
import dev.ivanravasi.piggy.ui.beneficiaries.OnBeneficiaryClickListener
import dev.ivanravasi.piggy.ui.operations.index.dialogs.AccountBottomSheet
import dev.ivanravasi.piggy.ui.operations.add.dialogs.SearchBeneficiaryBottomSheet
import dev.ivanravasi.piggy.ui.setAccount


class AddChartFragment : Fragment() {
    private lateinit var binding: FragmentAddChartBinding
    private lateinit var viewModel: AddChartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddChartBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, AddChartViewModel.Factory(
            DataStoreRepository(requireContext()),
            findNavController()
        ))[AddChartViewModel::class.java]

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.buttonAdd.isEnabled = !it

            if (it) binding.loadingProgress.show()
            else {
                if (viewModel.beneficiary.value == null) {
                    binding.filterBeneficiary.isEnabled = false
                }
                if (viewModel.account.value == null) {
                    binding.filterAccount.isEnabled = false
                }
                if (viewModel.category.value == null) {
                    binding.filterCategory.isEnabled = false
                }
                binding.loadingProgress.hide()
            }
        }

        binding.toggleChartType.addOnButtonCheckedListener { toggleButton, _, _ ->
            toggleLayoutVisibility(toggleButton)
        }
        setInitialValues()
        binding.toggleFilter.addOnButtonCheckedListener { toggleButton, _, _ ->
            toggleSelectorVisibility(toggleButton)
        }

        viewModel.beneficiary.observe(viewLifecycleOwner) {
            if (it != null) {
                setBeneficiary(it)
            } else {
                binding.beneficiaryName.text = "No beneficiary selected"
            }
        }
        viewModel.category.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.pickCategory.setCategory(it)
            }
        }
        viewModel.account.observe(viewLifecycleOwner) {
            if (it != null) {
                setAccount(it)
            }
        }

        binding.pickCategory.disableDeselection()

        binding.cardBeneficiary.beneficiaryImg.setOnClickListener {
            SearchBeneficiaryBottomSheet(viewModel.beneficiaries.value!!, object : OnBeneficiaryClickListener {
                override fun onBeneficiaryClick(beneficiary: Beneficiary) {
                    viewModel.beneficiary.value = beneficiary
                }
            }).show(parentFragmentManager, "BeneficiaryBottomSheet")
        }
        viewModel.categories.observe(viewLifecycleOwner) {
            binding.pickCategory.updateCategories(it)
        }
        binding.pickCategory.setOnSelectedCategoryListener {
            viewModel.category.value = it
        }
        binding.cardAccount.cardAccount.setOnClickListener {
            AccountBottomSheet(viewModel.accounts.value!!, object : OnAccountClickListener {
                override fun onAccountClick(account: Account) {
                    viewModel.account.value = account
                }
            }).show(parentFragmentManager, "AccountBottomSheet")
        }

        binding.buttonAdd.setOnClickListener {
            val request = ChartRequest(
                chartKind(),
                chartInterval(),
                chartStat(),
                chartFilter(),
                chartFilterId(),
            )
            viewModel.submit(request) {
                viewModel.favorite(it.body()!!.data.id)
                backWithSnackbar(binding.buttonAdd, "Chart added successfully")
            }
        }

        return binding.root
    }

    private fun chartFilterId(): Long? {
        return when (binding.toggleChartType.checkedButtonId) {
            R.id.button_history -> {
                when (binding.toggleFilter.checkedButtonId) {
                    R.id.filter_account -> viewModel.account.value!!.id
                    R.id.filter_category -> viewModel.category.value!!.id
                    R.id.filter_beneficiary -> viewModel.beneficiary.value!!.id
                    else -> null
                }
            }
            else -> null
        }
    }

    private fun chartFilter(): String {
        return when (binding.toggleChartType.checkedButtonId) {
            R.id.button_top_list -> {
                when (binding.toggleDomain.checkedButtonId) {
                    R.id.domain_accounts -> "accounts"
                    R.id.domain_categories -> "categories"
                    else -> "beneficiaries"
                }
            }
            else -> when (binding.toggleFilter.checkedButtonId) {
                R.id.filter_account -> "accounts"
                R.id.filter_category -> "categories"
                R.id.filter_beneficiary -> "beneficiaries"
                else -> "all"
            }
        }
    }

    private fun chartStat(): String {
        return when (binding.toggleStatistic.checkedButtonId) {
            R.id.stat_avg -> "avg"
            R.id.stat_max -> "max"
            R.id.stat_count -> "count"
            else -> "sum"
        }
    }

    private fun chartInterval(): String {
        return when (binding.toggleChartType.checkedButtonId) {
            R.id.button_top_list -> {
                when (binding.toggleTimeframe.checkedButtonId) {
                    R.id.timeframe_year -> "year"
                    R.id.timeframe_month -> "month"
                    else -> "all"
                }
            }
            else -> when (binding.toggleInterval.checkedButtonId) {
                R.id.interval_yearly -> "year"
                else -> "month"
            }
        }
    }

    private fun chartKind(): String {
        return when (binding.toggleChartType.checkedButtonId) {
            R.id.button_top_list -> "list"
            else -> when (binding.toggleKind.checkedButtonId) {
                R.id.kind_bar -> "bar"
                else -> "line"
            }
        }
    }

    private fun setBeneficiary(beneficiary: Beneficiary) {
        binding.beneficiaryName.text = beneficiary.name
        binding.cardBeneficiary.beneficiaryImg.loadBeneficiary(beneficiary.img, beneficiary.name)
    }

    private fun setAccount(account: Account) {
        binding.cardAccount.setAccount(account)
    }

    private fun setInitialValues() {
        binding.toggleChartType.check(R.id.button_history)
        binding.toggleDomain.check(R.id.domain_categories)
        binding.toggleKind.check(R.id.kind_line)
        binding.toggleInterval.check(R.id.interval_yearly)
        binding.toggleTimeframe.check(R.id.timeframe_all)
        binding.toggleStatistic.check(R.id.stat_sum)
        toggleSelectorVisibility(binding.toggleFilter)
    }

    private fun toggleLayoutVisibility(toggleButtonGroup: MaterialButtonToggleGroup) {
        when (toggleButtonGroup.checkedButtonId) {
            R.id.button_history -> {
                binding.layoutHistory.visibility = View.VISIBLE
                binding.layoutTopList.visibility = View.GONE
            }
            R.id.button_top_list -> {
                binding.layoutHistory.visibility = View.GONE
                binding.layoutTopList.visibility = View.VISIBLE
            }
        }
    }

    private fun toggleSelectorVisibility(toggleButtonGroup: MaterialButtonToggleGroup) {
        binding.beneficiarySelector.visibility = View.GONE
        binding.pickCategory.visibility = View.GONE
        binding.cardAccount.cardAccount.visibility = View.GONE
        when (toggleButtonGroup.checkedButtonId) {
            R.id.filter_beneficiary -> binding.beneficiarySelector.visibility = View.VISIBLE
            R.id.filter_category -> binding.pickCategory.visibility = View.VISIBLE
            R.id.filter_account -> binding.cardAccount.cardAccount.visibility = View.VISIBLE
        }
    }
}