package com.assessment.test

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.assessment.test.databinding.FragmentSecondBinding
import com.assessment.test.vm.MainViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private lateinit var viewModel: MainViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

      /*  binding.backImg.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            viewModel.observePostList=true
           // requireActivity().onBackPressed()
        }*/
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]


        viewModel.message.observe(viewLifecycleOwner, Observer {
            // updating data in displayMsg


            binding.id.text=it.id.toString()
            binding.txtHead.text=it.title
            binding.txtBody.text=it.body

        })


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}