package com.aeropay_merchant.Utilities

import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import javax.crypto.*
import javax.crypto.spec.GCMParameterSpec


class KeystoreDecryptor_APSDK @Throws(CertificateException::class, NoSuchAlgorithmException::class, KeyStoreException::class, IOException::class)
constructor() {

    private var keyStore: KeyStore? = null

    init {
        initKeyStore()
    }

    @Throws(KeyStoreException::class, CertificateException::class, NoSuchAlgorithmException::class, IOException::class)
    private fun initKeyStore() {
        keyStore = KeyStore.getInstance(KeystoreManager_APSDK.ANDROID_KEY_STORE)
        keyStore!!.load(null)
    }

    @Throws(UnrecoverableEntryException::class, NoSuchAlgorithmException::class, KeyStoreException::class, NoSuchProviderException::class, NoSuchPaddingException::class, InvalidKeyException::class, IOException::class, BadPaddingException::class, IllegalBlockSizeException::class, InvalidAlgorithmParameterException::class)
    fun decryptData(alias: String, encryptedData: ByteArray, encryptionIv: ByteArray): String {

        val cipher = Cipher.getInstance(KeystoreManager_APSDK.TRANSFORMATION)
        val spec = GCMParameterSpec(128, encryptionIv)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(alias), spec)

        return String(cipher.doFinal(encryptedData))
    }

    @Throws(NoSuchAlgorithmException::class, UnrecoverableEntryException::class, KeyStoreException::class)
    private fun getSecretKey(alias: String): SecretKey {
        return (keyStore!!.getEntry(alias, null) as KeyStore.SecretKeyEntry).secretKey
    }
}