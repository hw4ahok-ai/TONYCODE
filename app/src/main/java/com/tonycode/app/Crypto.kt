package com.tonycode.app

import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object Crypto {

    private const val PREFIX = "TC1:"
    private const val ITERATIONS = 150000
    private const val KEY_BITS = 256
    private const val SALT_SIZE = 16
    private const val IV_SIZE = 12
    private const val TAG_BITS = 128

    private fun makeKey(password: String, salt: ByteArray): SecretKeySpec {
        val spec = PBEKeySpec(
            password.toCharArray(),
            salt,
            ITERATIONS,
            KEY_BITS
        )

        val factory =
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")

        return SecretKeySpec(
            factory.generateSecret(spec).encoded,
            "AES"
        )
    }

    fun encrypt(text: String, password: String): String {

        require(text.isNotEmpty()) {
            "Teks tidak boleh kosong."
        }

        require(password.length >= 4) {
            "Password minimal 4 karakter."
        }

        val random = SecureRandom()

        val salt = ByteArray(SALT_SIZE)
        random.nextBytes(salt)

        val iv = ByteArray(IV_SIZE)
        random.nextBytes(iv)

        val cipher =
            Cipher.getInstance("AES/GCM/NoPadding")

        cipher.init(
            Cipher.ENCRYPT_MODE,
            makeKey(password, salt),
            GCMParameterSpec(TAG_BITS, iv)
        )

        val encrypted =
            cipher.doFinal(
                text.toByteArray(StandardCharsets.UTF_8)
            )

        val data = salt + iv + encrypted

        return PREFIX +
            Base64.encodeToString(
                data,
                Base64.NO_WRAP or Base64.URL_SAFE
            )
    }

    fun decrypt(text: String, password: String): String {

        require(text.startsWith(PREFIX)) {
            "Bukan format TONYCODE."
        }

        require(password.length >= 4) {
            "Password minimal 4 karakter."
        }

        val data =
            Base64.decode(
                text.removePrefix(PREFIX),
                Base64.NO_WRAP or Base64.URL_SAFE
            )

        val salt =
            data.copyOfRange(0, SALT_SIZE)

        val iv =
            data.copyOfRange(
                SALT_SIZE,
                SALT_SIZE + IV_SIZE
            )

        val encrypted =
            data.copyOfRange(
                SALT_SIZE + IV_SIZE,
                data.size
            )

        val cipher =
            Cipher.getInstance("AES/GCM/NoPadding")

        cipher.init(
            Cipher.DECRYPT_MODE,
            makeKey(password, salt),
            GCMParameterSpec(TAG_BITS, iv)
        )

        return String(
            cipher.doFinal(encrypted),
            StandardCharsets.UTF_8
        )
    }
}
