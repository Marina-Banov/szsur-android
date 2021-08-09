package hr.uniri.szsur.ui.settings

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import hr.uniri.szsur.R
import hr.uniri.szsur.databinding.FragmentSettingsBinding
import hr.uniri.szsur.util.SharedPreferenceUtils


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    companion object {
        private const val NIGHT_MODE = "NIGHT_MODE"
        private const val RECEIVE_NOTIFICATIONS = "RECEIVE_NOTIFICATIONS"
        private const val RECEIVE_NOTIFICATIONS_SURVEYS = "RECEIVE_NOTIFICATIONS_SURVEYS"
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

        binding.logout.setOnClickListener {
            (activity as SettingsActivity).signOut()
        }
        
        binding.swDarkMode.isChecked = getDarkMode() == Configuration.UI_MODE_NIGHT_YES
        binding.swEventNotification.isChecked = SharedPreferenceUtils.getBoolean(
            RECEIVE_NOTIFICATIONS, true) == true
        binding.swSurveyNotification.isChecked = SharedPreferenceUtils.getBoolean(
            RECEIVE_NOTIFICATIONS_SURVEYS, true) == true

        binding.swDarkMode.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isShown) { //ako nema ovog nakon aktiviranja switcha kod se neprestano pokrece
                when (getDarkMode()) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        SharedPreferenceUtils.putInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_NO)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        SharedPreferenceUtils.putInt(NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_YES)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                }
            }
        }

        binding.swEventNotification.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isShown) { //ako nema ovog nakon aktiviranja switcha kod se neprestano pokrece
                val receiveNotification = SharedPreferenceUtils.getBoolean(RECEIVE_NOTIFICATIONS, true) == true
                SharedPreferenceUtils.putBoolean(RECEIVE_NOTIFICATIONS, !receiveNotification)
            }
        }

        binding.swSurveyNotification.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isShown) { //ako nema ovog nakon aktiviranja switcha kod se neprestano pokrece
                val receiveNotification = SharedPreferenceUtils.getBoolean(RECEIVE_NOTIFICATIONS_SURVEYS, true) == true
                SharedPreferenceUtils.putBoolean(RECEIVE_NOTIFICATIONS_SURVEYS, !receiveNotification)
            }
        }

        return binding.root
    }

    private fun getDarkMode(): Int {
        return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    }

}