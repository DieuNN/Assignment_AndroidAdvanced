package com.example.assignmentandroidadvanced.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.assignmentandroidadvanced.databinding.ActivitySocialBinding
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.squareup.picasso.Picasso
import com.example.assignmentandroidadvanced.R

class SocialActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySocialBinding
    private lateinit var callBackManager: CallbackManager
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesEdit: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySocialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("fb_info", MODE_PRIVATE)
        callBackManager = CallbackManager.Factory.create()

        if (sharedPreferences.getString("name", "") != "") {
            binding.fbName.text = sharedPreferences.getString("name", "")
            Picasso.get().load(sharedPreferences.getString("image", "")).error(R.drawable.no_image_available).into(binding.fbImage)
        }

        binding.loginButton.setPermissions(listOf("email", "user_birthday", "user_gender", "user_friends"))

        binding.loginButton.registerCallback(callBackManager,object: FacebookCallback<LoginResult> {
            override fun onCancel() {
                Toast.makeText(baseContext, "Login canceled", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException) {
                Toast.makeText(baseContext, "Login Error", Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess(result: LoginResult) {
                Toast.makeText(baseContext, "Login Success", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callBackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        val graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken()
        ) { obj, _ ->
            val name = obj!!.getString("name")
            val imageURL = obj.getJSONObject("picture").getJSONObject("data").getString("url")

            Picasso.get().load(imageURL).into(binding.fbImage)
            binding.fbName.text = name

            sharedPreferencesEdit = sharedPreferences.edit()

            sharedPreferencesEdit.putString("name", name)
            sharedPreferencesEdit.putString("image", imageURL)
            sharedPreferencesEdit.commit()


        }

        val bundle = Bundle()
        bundle.putString("fields", "gender, name, id, first_name, last_name, picture.width(200).height(200)")

        graphRequest.parameters = bundle
        graphRequest.executeAsync()


    }

    private val accessTokenTracker = object : AccessTokenTracker() {
        override fun onCurrentAccessTokenChanged(
            oldAccessToken: AccessToken?,
            currentAccessToken: AccessToken?
        ) {
            if (currentAccessToken == null) {
                LoginManager.getInstance().logOut()
                binding.fbName.text = ""
                binding.fbImage.setImageResource(0)
                sharedPreferencesEdit = sharedPreferences.edit()
                sharedPreferencesEdit.clear().commit()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        accessTokenTracker.stopTracking()
    }


}