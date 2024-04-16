package com.assessment.test

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.assessment.test.adapter.PostAdapter
import com.assessment.test.databinding.FragmentFirstBinding
import com.assessment.test.dataclass.PostData
import com.assessment.test.vm.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(), onDetailsClickListener {

    private var _binding: FragmentFirstBinding? = null
    private lateinit var viewModel: MainViewModel
    private lateinit var postAdapter: PostAdapter
    private var currentPage = 0
    private val pageSize = 10
    private var initialData = ArrayList<PostData>()
    private var dataList = ArrayList<PostData>()

    private val delayScope = CoroutineScope(Dispatchers.Main)
    private var isLoading = false
    private var isLastPage = false
    private val visibleThreshold = 6
    private lateinit var layoutManager: LinearLayoutManager

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        postAdapter = PostAdapter(initialData, this)
        layoutManager = LinearLayoutManager(requireContext())
        binding.userlist.layoutManager = layoutManager
        binding.userlist.adapter = postAdapter
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                showLoader()
            } else {
                hideLoader()
            }
        }

        viewModel.fetchPosts()
        viewModel.postList.observe(viewLifecycleOwner) { newData ->
            newData?.let { it ->
                dataList.addAll(it)
                if (currentPage == 0) {
                    initialData.addAll(
                        dataList.subList(
                            0,
                            minOf(10, dataList.size)
                        )
                    )
                    currentPage++
                    postAdapter.notifyDataSetChanged()
                }
            }
        }


        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }

        binding.userlist.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (!isLoading && !isLastPage) {
                    if (visibleItemCount + firstVisibleItemPosition + visibleThreshold >= totalItemCount
                        && firstVisibleItemPosition >= 0
                    ) {
                        loadMoreData()
                    }
                }
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
            }
        }

    }





    private fun showLoader() {

        binding.swipeRefreshLayout.isRefreshing = true

    }

    private fun hideLoader() {
        binding.swipeRefreshLayout.isRefreshing = false

    }

    private fun loadMoreData() {
        isLoading = true
        currentPage++
        val totalpage=dataList.size/pageSize
        if(currentPage>totalpage){
            isLastPage = true
        }
        fetchData(currentPage, pageSize) { newData ->
            isLoading = false
            if (newData.isNullOrEmpty()) {
                isLastPage = true
                Snackbar.make(binding.root, "No more data available", Snackbar.LENGTH_SHORT).show()
            } else {
                initialData.addAll(newData)
                postAdapter.notifyItemRangeInserted(initialData.size - newData.size, newData.size)
            }
        }
    }

    private fun fetchData(page: Int, pageSize: Int, callback: (List<PostData>?) -> Unit) {
        delayScope.launch {
            delay(300)
            val newData = generateSampleData(page, pageSize)
            callback(newData)
        }
    }

    private fun generateSampleData(page: Int, pageSize: Int): List<PostData> {
        val startIndex = (page - 1) * pageSize
        val endIndex = startIndex + pageSize
        if (startIndex >= dataList.size) {
            return emptyList() // No more data available
        }
        return dataList.subList(startIndex, minOf(endIndex, dataList.size))
    }


    private fun refreshData() {
        binding.swipeRefreshLayout.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(postData: PostData) {
        viewModel.sendMessage(postData)
        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
    }
}