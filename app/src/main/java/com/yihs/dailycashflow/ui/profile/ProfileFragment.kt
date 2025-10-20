package com.yihs.dailycashflow.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.yihs.dailycashflow.R
import com.yihs.dailycashflow.databinding.FragmentProfileBinding
import com.yihs.dailycashflow.ui.auth.LoginActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel : ProfileViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding  = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showNameUser()
        onClickLogOut()


    }


    private fun showNameUser(){
        viewModel.getUser().observe(viewLifecycleOwner){
            binding.tvName.text = getString(R.string.hello_user, it.name)
        }
    }

    private fun onClickLogOut(){
        binding.btnLogOut.setOnClickListener {
            lifecycleScope.launch {
                showLoading(true)
                delay(2000)
                showLoading(false)
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
                viewModel.removeSession()
            }
        }
    }


    private fun showLoading(isShow: Boolean = false){
        binding.apply {
            if(isShow){
                loadingIndicator.visibility = View.VISIBLE
                btnLogOut.visibility = View.INVISIBLE
            }else{
                loadingIndicator.visibility = View.INVISIBLE
                btnLogOut.visibility = View.VISIBLE
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}