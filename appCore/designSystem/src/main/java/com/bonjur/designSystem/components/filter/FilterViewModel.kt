//
//  FilterViewModel.kt
//  AppFeature
//
//  Created by Huseyn Hasanov on 12.01.26
//

package com.yourapp.discover.viewmodel

import androidx.lifecycle.ViewModel
import com.bonjur.designSystem.components.filter.FilterView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.collections.filter
import kotlin.collections.map
import kotlin.collections.sortedBy

class FilterViewModel(
    initialModel: List<FilterView.Model>,
    private val onItemsSelected: (List<FilterView.Items>) -> Unit
) : ViewModel() {

    // MARK: - Properties

    private val _model = MutableStateFlow(
        initialModel.map { section ->
            section.copy(
                items = section.items.sortedBy { it.title.lowercase() }
            )
        }
    )
    val model: StateFlow<List<FilterView.Model>> = _model.asStateFlow()

    private val _filterModel = MutableStateFlow<List<FilterView.Model>>(emptyList())
    val filterModel: StateFlow<List<FilterView.Model>> = _filterModel.asStateFlow()

    private val _selectedItem = MutableStateFlow<FilterView.Model?>(null)
    val selectedItem: StateFlow<FilterView.Model?> = _selectedItem.asStateFlow()

    // MARK: - Public Methods

    fun updateModel(updatedModel: List<FilterView.Model>) {
        _model.value = updatedModel
        sortFilters()
    }

    fun fetchFilterScreenData() {
        _filterModel.value = _model.value
    }

    fun selectItem(item: FilterView.Model?) {
        _selectedItem.value = if (_selectedItem.value?.id == item?.id) null else item
    }

    fun isSelected(item: FilterView.Model): Boolean {
        return _selectedItem.value?.id == item.id
    }

    fun hasSelectedSubItems(item: FilterView.Model): Boolean {
        return item.items.any { it.selected }
    }

    fun getSelectedCount(): Int {
        return _model.value.count { section ->
            section.items.any { it.selected }
        }
    }

    // MARK: - Sub-item Selection

    fun toggleSubItem(item: FilterView.Items) {
        _selectedItem.update { selected ->
            selected?.copy(
                items = selected.items.map { subItem ->
                    if (subItem.id == item.id) {
                        subItem.copy(selected = !subItem.selected)
                    } else {
                        subItem
                    }
                }
            )
        }
    }

    fun toggleSubItemInFilterScreen(item: FilterView.Items) {
        _filterModel.update { filterModel ->
            filterModel.map { section ->
                updateSection(section, togglingItem = item)
            }
        }
    }

    // MARK: - Confirmation Actions

    fun confirmSelection() {
        val selected = _selectedItem.value ?: return

        _model.update { model ->
            model.map { section ->
                if (section.id == selected.id) {
                    section.copy(items = reorderSelectedFirst(selected.items))
                } else {
                    section
                }
            }
        }
        sortFilters()
        notifySelectedItems()
        _selectedItem.value = null
    }

    fun confirmFilterScreen() {
        _model.value = _filterModel.value.map { section ->
            section.copy(items = reorderSelectedFirst(section.items))
        }
        sortFilters()
        notifySelectedItems()
    }

    // MARK: - Removal Actions

    fun removeSelection() {
        val selected = _selectedItem.value ?: return

        _model.update { model ->
            model.map { section ->
                if (section.id == selected.id) {
                    section.copy(
                        items = sortAlphabetically(
                            section.items.map { it.copy(selected = false) }
                        )
                    )
                } else {
                    section
                }
            }
        }
        sortFilters()
        notifySelectedItems()
        _selectedItem.value = null
    }

    fun removeAllFilters() {
        _filterModel.update { filterModel ->
            filterModel.map { section ->
                section.copy(
                    items = sortAlphabetically(
                        section.items.map { it.copy(selected = false) }
                    )
                )
            }
        }

        _model.value = _filterModel.value
        sortFilters()
        notifySelectedItems()
    }

    fun sortFilters() {
        val alphabeticallySorted = sortModelsAlphabetically(_model.value)
        _model.value = reorderSelectedModelsFirst(alphabeticallySorted)
    }

    // MARK: - Private Helpers

    private fun hasSelectedItems(model: FilterView.Model): Boolean {
        return model.items.any { it.selected }
    }

    private fun sortModelsAlphabetically(models: List<FilterView.Model>): List<FilterView.Model> {
        return models.sortedBy { it.title.lowercase() }
    }

    private fun reorderSelectedModelsFirst(models: List<FilterView.Model>): List<FilterView.Model> {
        val selected = models.filter { hasSelectedItems(it) }
        val unselected = models.filter { !hasSelectedItems(it) }
        return selected + unselected
    }

    private fun updateSection(
        section: FilterView.Model,
        togglingItem: FilterView.Items
    ): FilterView.Model {
        return section.copy(
            items = section.items.map { subItem ->
                if (subItem.id == togglingItem.id) {
                    subItem.copy(selected = !subItem.selected)
                } else {
                    subItem
                }
            }
        )
    }

    private fun notifySelectedItems() {
        val allSelectedItems = _model.value.flatMap { section ->
            section.items.filter { it.selected }
        }
        onItemsSelected(allSelectedItems)
    }

    private fun reorderSelectedFirst(items: List<FilterView.Items>): List<FilterView.Items> {
        val selected = items.filter { it.selected }
        val unselected = items.filter { !it.selected }
        return selected + unselected
    }

    private fun sortAlphabetically(items: List<FilterView.Items>): List<FilterView.Items> {
        return items.sortedBy { it.title.lowercase() }
    }
}

class FilterViewModelFactory(
    private val initialModel: List<FilterView.Model>,
    private val onItemsSelected: (List<FilterView.Items>) -> Unit
) : androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilterViewModel::class.java)) {
            return FilterViewModel(initialModel, onItemsSelected) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}