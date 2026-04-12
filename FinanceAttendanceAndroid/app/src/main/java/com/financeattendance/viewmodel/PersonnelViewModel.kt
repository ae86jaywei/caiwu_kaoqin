package com.financeattendance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financeattendance.data.entity.Personnel
import com.financeattendance.data.repository.PersonnelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PersonnelViewModel @Inject constructor(
    private val repository: PersonnelRepository
) : ViewModel() {
    private val _persons = MutableStateFlow<List<Personnel>>(emptyList())
    val persons: StateFlow<List<Personnel>> = _persons.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadPersons() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.queryAllPersons().collect { personsList ->
                _persons.value = personsList
            }
            _isLoading.value = false
        }
    }

    fun addPerson(person: Personnel) {
        viewModelScope.launch {
            val newPerson = person.copy(
                id = UUID.randomUUID().toString(),
                createTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            )
            repository.addPerson(newPerson)
            loadPersons()
        }
    }

    fun updatePerson(person: Personnel) {
        viewModelScope.launch {
            repository.updatePerson(person)
            loadPersons()
        }
    }

    fun deletePerson(person: Personnel) {
        viewModelScope.launch {
            repository.deletePerson(person)
            loadPersons()
        }
    }
}
