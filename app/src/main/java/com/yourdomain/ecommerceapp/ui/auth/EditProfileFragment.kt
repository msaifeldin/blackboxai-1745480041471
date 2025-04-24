package com.yourdomain.ecommerceapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yourdomain.ecommerceapp.databinding.FragmentEditProfileBinding
import com.yourdomain.ecommerceapp.ui.common.BaseFragment

class EditProfileFragment : BaseFragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupViews() {
        binding.saveButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            viewModel.updateProfile(name, email)
        }
    }

    override fun setupObservers() {
        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.nameEditText.setText(user.name)
            binding.emailEditText.setText(user.email)
        }

        viewModel.isProfileUpdated.observe(viewLifecycleOwner) { updated ->
            if (updated) {
                findNavController().popBackStack()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) showLoading() else hideLoading()
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                showError(it) {
                    viewModel.clearError()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
