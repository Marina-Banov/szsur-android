package hr.uniri.szsur.ui.info

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import hr.uniri.szsur.R

class InfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

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
    }
}