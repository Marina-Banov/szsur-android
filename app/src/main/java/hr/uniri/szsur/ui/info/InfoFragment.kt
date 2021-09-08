package hr.uniri.szsur.ui.info

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import hr.uniri.szsur.R
import hr.uniri.szsur.databinding.FragmentInfoBinding
import hr.uniri.szsur.databinding.LayoutContactsButtonBinding


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
                    binding.buttonContainer.addView(
                        createButton(web, R.drawable.ic_globe)
                    )
                }
                if (mail.isNotEmpty()) {
                    binding.buttonContainer.addView(
                        createButton(mail, R.drawable.ic_mail)
                    )
                }
                if (instagram.isNotEmpty()) {
                    binding.buttonContainer.addView(
                        createButton(instagram, R.drawable.ic_instagram)
                    )

                }
                if (facebook.isNotEmpty()) {
                    binding.buttonContainer.addView(
                        createButton(facebook, R.drawable.ic_facebook)
                    )

                }
                if (messenger.isNotEmpty()) {
                    val url = messenger.replace("m.me/", "fb://messaging/")
                    binding.buttonContainer.addView(
                        createButton(url, R.drawable.ic_messenger)
                    )
                }
                if (linkedin.isNotEmpty()) {
                    binding.buttonContainer.addView(
                        createButton(linkedin, R.drawable.ic_linkedin)
                    )
                }
            }
        })

        return binding.root
    }

    private fun createButton(target: String, icon: Int): ImageButton {
        val b: LayoutContactsButtonBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.layout_contacts_button,
            null,
            false
        )
        b.contactsButton.setImageResource(icon)
        b.contactsButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(target))
            startActivity(intent)
        }
        return b.contactsButton
    }
/*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       super.onViewCreated(view, savedInstanceState)

        var btn_feedback = view.findViewById<Button>(R.id.feedback_btn)
        btn_feedback.setOnClickListener {
            var email_info = "mailto:m.banov7@gmail.com?subject=SZSUR -  pitanja"
            var intent = Intent(Intent.ACTION_SENDTO).apply {
                setData(Uri.parse(email_info))
            }
            startActivity(intent)
        }
    }*/
}