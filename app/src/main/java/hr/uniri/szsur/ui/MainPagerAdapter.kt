package hr.uniri.szsur.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import hr.uniri.szsur.ui.favorites.FavoritesFragment
import hr.uniri.szsur.ui.home.HomeFragment
import hr.uniri.szsur.ui.info.InfoFragment
import hr.uniri.szsur.ui.survey.SurveyFragment

class MainPagerAdapter(fragmentManager: FragmentManager)
    : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int = 4

    override fun getItem(position: Int): Fragment {
        return when (position) {
            MainFragment.FAVORITES -> FavoritesFragment()
            MainFragment.SURVEY -> SurveyFragment()
            MainFragment.INFO -> InfoFragment()
            else -> HomeFragment()
        }
    }
}