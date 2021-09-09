package hr.uniri.szsur.ui.info

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.gridlayout.widget.GridLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import hr.uniri.szsur.R
import hr.uniri.szsur.databinding.FragmentInfoBinding
import hr.uniri.szsur.databinding.LayoutContactsButtonBinding
import android.widget.Toast


class InfoFragment : Fragment() {

    private lateinit var binding: FragmentInfoBinding
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_info, container, false)
        binding.lifecycleOwner = this

        val viewModel = ViewModelProvider(this).get(InfoViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.organization.observe(viewLifecycleOwner, {
            it.contacts.apply {
                if (web.isNotEmpty()) {
                    val button = createButton(R.drawable.ic_web)
                    button.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(web))
                        startActivity(intent)
                    }
                }
                if (mail.isNotEmpty()) {
                    val url = "mailto:${mail}?subject=SZSUR - pitanja"
                    val button = createButton(R.drawable.ic_mail)
                    button.setOnClickListener {
                        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse(url))
                        startActivity(intent)
                    }
                }
                if (instagram.isNotEmpty()) {
                    val button = createButton(R.drawable.ic_instagram)
                    button.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(instagram))
                        startActivity(intent)
                    }
                }
                if (facebook.isNotEmpty()) {
                    val fbButton = createButton(R.drawable.ic_facebook)
                    fbButton.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/$facebook"))
                        startActivity(intent)
                    }

                    val messengerButton = createButton(R.drawable.ic_messenger)
                    messengerButton.setOnClickListener {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("fb-messenger://user/$facebook"))
                                .setPackage("com.facebook.orca")
                            startActivity(intent)
                        } catch (ex: ActivityNotFoundException) {
                            Toast.makeText(context, R.string.no_fb_messenger, Toast.LENGTH_LONG).show()
                        }
                    }
                }
                if (linkedin.isNotEmpty()) {
                    val button = createButton(R.drawable.ic_linkedin)
                    button.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkedin))
                        startActivity(intent)
                    }
                }
            }
        })

        return binding.root
    }

    private fun createButton(icon: Int): ImageButton {
        val b: LayoutContactsButtonBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.layout_contacts_button,
            null,
            false
        )
        val params = GridLayout.LayoutParams()
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        b.contactsButton.layoutParams = params
        b.contactsButton.setImageResource(icon)
        binding.buttonContainer.addView(b.contactsButton)
        (b.contactsButton.layoutParams as GridLayout.LayoutParams).setGravity(Gravity.CENTER)
        return b.contactsButton
    }
}