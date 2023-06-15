package com.arashjahani.sign_up_form.ui.confirmation

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arashjahani.sig_up_form.R
import com.arashjahani.sig_up_form.databinding.FragmentConfirmationBinding
import com.arashjahani.sig_up_form.databinding.FragmentSignupBinding
import com.arashjahani.sign_up_form.dao.entity.Profile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmationFragment : Fragment() {

    private var _binding: FragmentConfirmationBinding? = null
    private val binding get() = _binding!!

    var profile:Profile?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentConfirmationBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profile= arguments?.getParcelable("PROFILE")

        prepareViews()

        initListeners()
    }


    private fun prepareViews(){
        binding.lblTitle.text="Hello, ${profile?.firstName ?: ""}!"

        binding.imgAvatar.setImageURI(Uri.parse(profile?.avatarUri))

        binding.lblWebsite.paintFlags = binding.lblWebsite.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.lblWebsite.text=profile?.website ?: ""

        binding.lblName.text=profile?.firstName ?: ""

        binding.lblEmail.text=profile?.email ?: ""
    }

    private fun initListeners(){
        binding.lblWebsite.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(profile?.website ?: ""))
            startActivity(intent)
        }
    }
}
