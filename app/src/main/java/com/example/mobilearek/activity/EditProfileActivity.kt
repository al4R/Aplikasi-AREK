package com.example.mobilearek.activity


import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.mobilearek.MainActivity
import com.example.mobilearek.R
import com.example.mobilearek.app.ApiConfig
import com.example.mobilearek.helper.SharedPref
import com.example.mobilearek.model.ResponModel
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.et_email
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_profil_image.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EditProfileActivity : AppCompatActivity() {
    private lateinit var s: SharedPref


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        s = SharedPref(this)

        getData()
        mainButton()

    }

    @SuppressLint("SetTextI18n")
    fun getData() {
        val user = s.getUser()
        if (user != null) {
            et_email.setText(user.email)
            et_nama.setText(user.name)
            et_telepon.setText(user.telepon)
        }

    }
    fun mainButton(){
        btn_simpan.setOnClickListener {
            if(et_email.text.toString().isEmpty())
            {
                et_email.error = "Email tidak boleh kosong"
                et_email.requestFocus()
            }else if(et_nama.text.toString().isEmpty()){
                et_nama.error = "Nama tidak boleh kosong"
                et_nama.requestFocus()
            }else if(et_telepon.text.toString().isEmpty()){
                et_telepon.error = "Nomor telepon tidak boleh kosong"
                et_telepon.requestFocus()
            }
            else if(Patterns.EMAIL_ADDRESS.matcher(et_email.text.toString()).matches() == false)
            {
                et_email.error = "Email tidak valid"
                et_email.requestFocus()
            }
            else if(et_telepon.text.toString().length < 9)
            {
                et_telepon.error="Nomor telepon tidak valid"
                et_telepon.requestFocus()
            }else {
                editUser()
            }
        }
        btn_batal.setOnClickListener {
            onBackPressed()
        }
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Ubah profile"
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    fun editUser(){
        progres_bar.visibility  = View.VISIBLE
        val user = s.getUser()
        val id = user!!.id
        ApiConfig.instanceRetrofit.updateUser(id,et_email.text.toString(),et_nama.text.toString(),et_telepon.text.toString()).
        enqueue(object : Callback<ResponModel> {
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val respon = response.body()
                if (respon != null) {
                    if (respon.success == 1){
                        progres_bar.visibility  = View.GONE
                        s.setUser(respon.user)
                        s.setString(s.nama,respon.user.name)
                        s.setString(s.email,respon.user.email)
                        s.setString(s.telepon,respon.user.telepon)
                        onBackPressed()
                        Toast.makeText(this@EditProfileActivity, "Berhasil mengubah profile " + respon.message, Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@EditProfileActivity, "Error:" + respon.message, Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this@EditProfileActivity, "Tidak ada respon", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                progres_bar.visibility  = View.GONE
                Toast.makeText(this@EditProfileActivity, "Error:" + t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

}