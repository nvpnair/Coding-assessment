package com.assessment.test.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assessment.test.repo.RetrofitClient
import com.assessment.test.dataclass.PostData
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val apiService = RetrofitClient.create()
    private val _postList = MutableLiveData<List<PostData>>()
    val postList: LiveData<List<PostData>> get() = _postList
    val message = MutableLiveData<PostData>()
    // LiveData for loading state
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading
    var observePostList = true
    var postDataList: MutableList<PostData> = mutableListOf()

    // LiveData for error message
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchPosts() {
        // Check if _postList already contains data
        if (_postList.value != null && _postList.value!!.isNotEmpty()) {
            // Data already loaded, no need to fetch again
            return
        }

        viewModelScope.launch {
            try {
                _loading.value = true
                val response = apiService.getPosts()
                if (response.isSuccessful) {
                    _postList.value = response.body()
                } else {
                    _errorMessage.value =  "No data"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error" // Set error message
            } finally {
                _loading.value = false
            }
        }
    }


    fun sendMessage(postData: PostData) {
        message.value = postData
    }
}