package com.tonycode.app

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
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
    private var decryptMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.rgb(8, 16, 17))
            setPadding(24, 24, 24, 24)
        }

        val scroll = ScrollView(this)

        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val title = TextView(this).apply {
            text = "TONYCODE"
            textSize = 28f
            setTextColor(Color.WHITE)
        }

        content.addView(title)

        val subtitle = TextView(this).apply {
            text = "Ubah teks menjadi teks rahasia dan kembalikan lagi."
            textSize = 14f
            setTextColor(Color.LTGRAY)
        }

        content.addView(subtitle)

        val encryptButton = MaterialButton(this).apply {
            text = "CONVERT"
        }

        val decryptButton = MaterialButton(this).apply {
            text = "CONVERT BACK"
        }

        val modeRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
        }

        modeRow.addView(
            encryptButton,
            LinearLayout.LayoutParams(0, 140, 1f)
        )

        modeRow.addView(
            decryptButton,
            LinearLayout.LayoutParams(0, 140, 1f)
        )

        content.addView(modeRow)

        input = EditText(this).apply {
            hint = "Tulis atau paste teks di sini..."
            setTextColor(Color.WHITE)
            setHintTextColor(Color.GRAY)
            minHeight = 300
        }

        content.addView(input)

        password = EditText(this).apply {
            hint = "Password"
            setTextColor(Color.WHITE)
            setHintTextColor(Color.GRAY)

            inputType =
                InputType.TYPE_CLASS_TEXT or
                InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        content.addView(password)

        val actionButton = MaterialButton(this).apply {
            text = "CONVERT (ENCRYPT)"
        }

        content.addView(actionButton)

        result = EditText(this).apply {
            hint = "Hasil akan muncul di sini..."
            setTextColor(Color.WHITE)
            setHintTextColor(Color.GRAY)
            minHeight = 300
            isFocusable = false
            setTextIsSelectable(true)
        }

        content.addView(result)

        val copyButton = MaterialButton(this).apply {
            text = "COPY"
        }

        val pasteButton = MaterialButton(this).apply {
            text = "PASTE"
        }

        val shareButton = MaterialButton(this).apply {
            text = "SHARE"
        }

        val clearButton = MaterialButton(this).apply {
            text = "CLEAR"
        }

        content.addView(copyButton)
        content.addView(pasteButton)
        content.addView(shareButton)
        content.addView(clearButton)

        encryptButton.setOnClickListener {
            decryptMode = false
            actionButton.text = "CONVERT (ENCRYPT)"
        }

        decryptButton.setOnClickListener {
            decryptMode = true
            actionButton.text = "CONVERT BACK (DECRYPT)"
        }

        actionButton.setOnClickListener {
            try {
                val text = input.text.toString().trim()
                val key = password.text.toString()

                val output = if (decryptMode) {
                    Crypto.decrypt(text, key)
                } else {
                    Crypto.encrypt(text, key)
                }

                result.setText(output)

            } catch (e: Exception) {
                Toast.makeText(
                    this,
                    e.message ?: "Operasi gagal.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        copyButton.setOnClickListener {
            val clipboard =
                getSystemService(Context.CLIPBOARD_SERVICE)
                        as ClipboardManager

            clipboard.setPrimaryClip(
                ClipData.newPlainText(
                    "TONYCODE",
                    result.text.toString()
                )
            )

            Toast.makeText(
                this,
                "Hasil disalin",
                Toast.LENGTH_SHORT
            ).show()
        }

        pasteButton.setOnClickListener {
            val clipboard =
                getSystemService(Context.CLIPBOARD_SERVICE)
                        as ClipboardManager

            val clip = clipboard.primaryClip

            if (clip != null && clip.itemCount > 0) {
                input.setText(
                    clip.getItemAt(0)
                        .coerceToText(this)
                )
            }
        }

        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"

                putExtra(
                    Intent.EXTRA_TEXT,
                    result.text.toString()
                )
            }

            startActivity(
                Intent.createChooser(
                    shareIntent,
                    "Bagikan TONYCODE"
                )
            )
        }

        clearButton.setOnClickListener {
            input.text.clear()
            password.text.clear()
            result.text.clear()
        }

        scroll.addView(content)

        root.addView(
            scroll,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        setContentView(root)
    }
}        val root = LinearLayout(this).apply {
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
