package com.arashjahani.sign_up_form.ui.sign_up

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.arashjahani.sig_up_form.R
import com.arashjahani.sig_up_form.databinding.FragmentSignupBinding
import com.arashjahani.sign_up_form.utils.fileFromContentUri
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.arashjahani.sign_up_form.dao.entity.RequestStatus
import com.arashjahani.sign_up_form.dao.entity.SignUpRequestModel
import com.arashjahani.sign_up_form.utils.isEmailValid
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

@AndroidEntryPoint
class SignupFragment : Fragment() {


    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private val mViewModel: SignupViewModel by viewModels()

    private var avatarUri:String=""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSignupBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()

        initObservers()
    }

    private fun initObservers(){


        lifecycleScope.launch {
            mViewModel.status.collect{
                when(it){
                    RequestStatus.LOADING->{
                        binding.signupLoading.visibility=View.VISIBLE
                        binding.btnSubmit.visibility=View.INVISIBLE
                    }
                    RequestStatus.SUCCESS ->{
                        binding.signupLoading.visibility=View.GONE
                    }
                    RequestStatus.FAILED ->{
                        binding.signupLoading.visibility=View.GONE
                        binding.btnSubmit.visibility=View.VISIBLE
                        Toast.makeText(requireContext(),"Signup error!",Toast.LENGTH_SHORT).show()
                    }
                    else ->{}
                }
            }
        }

        lifecycleScope.launch {
            mViewModel.avatarUploadingStatus.collect{
                when(it){
                    RequestStatus.LOADING->{
                        binding.avatarLoadingView.visibility=View.VISIBLE
                    }
                    RequestStatus.SUCCESS ->{
                        binding.avatarLoadingView.visibility=View.GONE
                        binding.imgAvatar.setImageURI(Uri.parse(avatarUri))
                    }
                    RequestStatus.FAILED ->{
                        binding.avatarLoadingView.visibility=View.GONE
                        Toast.makeText(requireContext(),"Signup error!",Toast.LENGTH_SHORT).show()
                    }
                    else ->{}
                }
            }
        }

        lifecycleScope.launch {
            mViewModel.profile.collect{
                it?.let {
                    var bundle=Bundle()
                    bundle.putParcelable("PROFILE",it)
                    findNavController().navigate(R.id.action_signupFragment_to_confirmationFragment,bundle)

                }
            }
        }

    }

    private fun initListeners() {

        binding.imgAvatar.setOnClickListener {
            chooseAvatar()
        }

        binding.btnSubmit.setOnClickListener {

            if (signUpFormIsValidToCallRequest())
                mViewModel.signUp(createSignUpRequest())
        }

        binding.txtEmail.addTextChangedListener(textChangeListener)
        binding.txtPassword.addTextChangedListener(textChangeListener)
    }

    private var textChangeListener=(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            if(binding.txtEmail.isFocused)
                binding.inputEmail.error=null

            if(binding.txtPassword.isFocused)
                binding.inputPassword.error=null
        }

        override fun afterTextChanged(p0: Editable?) {}

    })

    private fun signUpFormIsValidToCallRequest(): Boolean {

        var email=binding.txtEmail.text.toString()
        var passowrd=binding.txtPassword.text.toString()

        if(email.isNullOrEmpty()){
            binding.inputEmail.error="Email address is empty!"
            return false
        }

        if(!email.isEmailValid()){
            binding.inputEmail.error="Email address is no valid!"
            return false
        }

        if(passowrd.isNullOrEmpty()){
            binding.inputPassword.error="Password is not valid!"
            return false
        }

        if(passowrd.length<6){
            binding.inputPassword.error="Password is short!"
            return false
        }

        return true
    }

    private fun createSignUpRequest(): SignUpRequestModel {

        return SignUpRequestModel(
            avatarUri,
            binding.txtFirstName.text.toString(),
            binding.txtEmail.text.toString(),
            binding.txtPassword.text.toString(),
            binding.txtWebsite.text.toString(),
        )

    }

    private fun chooseAvatar() {

        ImagePicker.with(this).apply {
            compress(512)  //Final image size will be less than 512 KB(Optional)
            crop(9f, 16f)
            maxResultSize(
                640,
                360
            )  //Final image resolution will be less than 640*360(Optional)
            createIntent { intent ->
                avatarPickerResult.launch(intent)
            }
        }
    }

    private val avatarPickerResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri: Uri? = data?.data

                Log.v("fileUri", fileUri.toString())
                var file: File? = null
                if (fileUri?.scheme.equals("content")) {
                    file = fileFromContentUri(requireContext(), fileUri)
                } else {
                    file = File(fileUri?.path ?: "")

                }

                if (file != null) {


                    avatarUri=fileUri.toString()

                    val filePart = MultipartBody.Part.createFormData(
                        "file",
                        file.name,
                        RequestBody.create("image/*".toMediaTypeOrNull(), file)
                    )

                    mViewModel.uploadImage(filePart)

                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.image_picker_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }


            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT)
                    .show()
            } else {
            }
        }


}