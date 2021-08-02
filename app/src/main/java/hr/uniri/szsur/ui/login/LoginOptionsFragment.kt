package hr.uniri.szsur.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import hr.uniri.szsur.R
import hr.uniri.szsur.databinding.FragmentLoginOptionsBinding

class LoginOptionsFragment : Fragment() {

    private lateinit var binding: FragmentLoginOptionsBinding
    private lateinit var activity: LoginActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = getActivity() as LoginActivity
        activity.loading.observe(viewLifecycleOwner, {
            binding.loginOptions.alpha = if (!it) 0.3F else 1.0F
            binding.loginOptions.animate().alpha(if (it) 0.3F else 1.0F).duration = 100
            binding.loading.isClickable = it
            binding.loading.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_options, container, false)

        binding.emailSignInButton.setOnClickListener {
            val email = binding.email.text
            email?.let {
                binding.email.clearFocus()
                activity.firebaseAuthSendEmail(email.toString())
            }
        }
        binding.googleSignInButton.setOnClickListener { activity.firebaseAuthGoogle() }
        binding.anonymousSignIn.setOnClickListener { activity.firebaseAuthAnonymous() }

        return binding.root
    }
}