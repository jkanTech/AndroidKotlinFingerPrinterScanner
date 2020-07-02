package com.jkantech.androidkotlinfingerprinter

import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricPrompt
import android.hardware.biometrics.BiometricPrompt.AuthenticationCallback
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.os.Message
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_intro.*
import java.security.Key

class Intro : AppCompatActivity() {
    private var cancellationSignal:CancellationSignal?=null
    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
    get()=
        @RequiresApi(Build.VERSION_CODES.P)
        object : BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                super.onAuthenticationError(errorCode, errString)
                toast("Erreur d'authentification $errorCode")

            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)
                toast("Authenfier avec succes")
                startActivity(Intent(this@Intro,MainActivity::class.java))
                finish()
            }



        }
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        checkBiometricSupport()



        authentification_btn.setOnClickListener {
            val biometricPrompt=BiometricPrompt.Builder(this)
            .setTitle("Besoin de d'authenfication")
                .setSubtitle("L'authentification est requise")
                .setDescription("Cette application naiccessite ")
                .setNegativeButton("Annuler",this.mainExecutor,DialogInterface.OnClickListener {
                        dialog, which ->
                    toast("Autentification annuler")

                }).build()
            biometricPrompt.authenticate(getCancellationSignal(),mainExecutor,authenticationCallback)
        }



    }
private fun getCancellationSignal():CancellationSignal {
    cancellationSignal = CancellationSignal()
    cancellationSignal?.setOnCancelListener {
        toast("Action annuler par l'utilisateur")
    }
    return cancellationSignal as CancellationSignal

}
    private fun toast(message: String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkBiometricSupport():Boolean {
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (!keyguardManager.isDeviceSecure){
            toast("L'empreinte digitat n'est pas actif")
            return false
        }
if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.USE_BIOMETRIC) !=PackageManager.PERMISSION_GRANTED){
    toast("La permission d'utiliser l'empreinte n'est pas actif")
    return false
}
        return  if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)){
            true
        }else true

    }
}