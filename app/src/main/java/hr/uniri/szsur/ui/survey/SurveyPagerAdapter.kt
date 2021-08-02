package hr.uniri.szsur.ui.survey

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import hr.uniri.szsur.ui.survey_list.SurveyListFragment

class SurveyPagerAdapter(fm: FragmentManager)
    : FragmentPagerAdapter(fm,  BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int  = 2

    override fun getItem(position: Int): Fragment {
        return SurveyListFragment.newInstance(position != 0)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return if (position == 0) {
            "NOVE ANKETE"
        } else {
            "REZULTATI ANKETA"
        }
    }
}