package com.tonycode.app

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private lateinit var input: EditText
    private lateinit var password: EditText
    private lateinit var result: EditText
    private lateinit var actionButton: MaterialButton

    private var decryptMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildUi()
    }

    private fun dp(value: Int): Int =
        (value * resources.displayMetrics.density).toInt()

    private fun buildUi() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.rgb(8, 16, 17))
            setPadding(dp(18), dp(14), dp(18), dp(18))
        }

        val scroll = ScrollView(this)
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        content.addView(TextView(this).apply {
            text = "TONYCODE"
            textSize = 27f
            setTextColor(Color.WHITE)
            setTypeface(null, android.graphics.Typeface.BOLD)
        })

        content.addView(TextView(this).apply {
            text = "Ubah teks biasa menjadi teks rahasia dan kembalikan lagi."
            textSize = 14f
            setTextColor(Color.LTGRAY)
            setPadding(0, dp(4), 0, dp(14))
        })

        val modeRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
        }

        val encryptTab = MaterialButton(this).apply {
            text = "CONVERT"
        }

        val decryptTab = MaterialButton(this).apply {
            text = "CONVERT BACK"
        }

        modeRow.addView(
            encryptTab,
            LinearLayout.LayoutParams(0, dp(52), 1f)
        )

        modeRow.addView(
            decryptTab,
            LinearLayout.LayoutParams(0, dp(52), 1f)
        )

        content.addView(modeRow)

        content.addView(label("TEKS"))

        input = field("Tulis atau paste teks di sini...")
        content.addView(
            input,
            LinearLayout.LayoutParams(-1, dp(175))
        )

        content.addView(label("KUNCI / PASSWORD"))

        password = field("Masukkan password").apply {
            inputType =
                InputType.TYPE_CLASS_TEXT or
                InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        content.addView(
            password,
            LinearLayout.LayoutParams(-1, dp(60))
        )

        actionButton = MaterialButton(this).apply {
            text = "CONVERT (ENCRYPT)"
        }

        content.addView(
            actionButton,
            LinearLayout.LayoutParams(-1, dp(58)).apply {
                topMargin = dp(12)
            }
        )

        content.addView(label("HASIL"))

        result = field("Hasil akan muncul di sini...").apply {
            isFocusable = false
            setTextIsSelectable(true)
        }

        content.addView(
            result,
            LinearLayout.LayoutParams(-1, dp(185))
        )

        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
        }

        val copy = MaterialButton(this).apply {
            text = "COPY"
        }

        val paste = MaterialButton(this).apply {
            text = "PASTE"
        }

        val share = MaterialButton(this).apply {
            text = "SHARE"
        }

        val clear = MaterialButton(this).apply {
            text = "CLEAR"
        }

        listOf(copy, paste, share, clear).forEach {
            row.addView(
                it,
                LinearLayout.LayoutParams(0, dp(52), 1f)
            )
        }

        content.addView(row)

        content.addView(TextView(this).apply {
            text =
                "Pengirim dan penerima harus menggunakan password yang sama."
            textSize = 12f
            setTextColor(Color.GRAY)
            setPadding(0, dp(16), 0, dp(12))
        })

        encryptTab.setOnClickListener {
            decryptMode = false
            actionButton.text = "CONVERT (ENCRYPT)"
            result.text.clear()
        }

        decryptTab.setOnClickListener {
            decryptMode = true
            actionButton.text = "CONVERT BACK (DECRYPT)"
            result.text.clear()
        }

        actionButton.setOnClickListener {
            try {
                val text = input.text.toString().trim()
                val pwd = password.text.toString()

                val output =
                    if (decryptMode) {
                        Crypto.decrypt(text, pwd)
                    } else {
                        Crypto.encrypt(text, pwd)
                    }

                result.setText(output)

            } catch (e: Exception) {
                toast(
                    if (decryptMode)
                        "Gagal membuka teks. Pastikan teks dan password benar."
                    else
                        e.message ?: "Gagal."
                )
            }
        }

        copy.setOnClickListener {
            val text = result.text.toString()

            if (text.isBlank()) {
                toast("Belum ada hasil.")
                return@setOnClickListener
            }

            val cm =
                getSystemService(Context.CLIPBOARD_SERVICE)
                    as ClipboardManager

            cm.setPrimaryClip(
                ClipData.newPlainText(
                    "TONYCODE",
                    text
                )
            )

            toast("Hasil disalin.")
        }

        paste.setOnClickListener {
            val cm =
                getSystemService(Context.CLIPBOARD_SERVICE)
                    as ClipboardManager

            val clip = cm.primaryClip

            if (clip != null && clip.itemCount > 0) {
                input.setText(
                    clip.getItemAt(0)
                        .coerceToText(this)
                )
            }
        }

        share.setOnClickListener {
            val text = result.text.toString()

            if (text.isBlank()) {
                toast("Belum ada hasil.")
                return@setOnClickListener
            }

            val intent =
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(
                        Intent.EXTRA_TEXT,
                        text
                    )
                }

            startActivity(
                Intent.createChooser(
                    intent,
                    "Bagikan TONYCODE"
                )
            )
        }

        clear.setOnClickListener {
            input.text.clear()
            password.text.clear()
            result.text.clear()
        }

        scroll.addView(content)

        root.addView(
            scroll,
            LinearLayout.LayoutParams(
                -1,
                0,
                1f
            )
        )

        setContentView(root)
