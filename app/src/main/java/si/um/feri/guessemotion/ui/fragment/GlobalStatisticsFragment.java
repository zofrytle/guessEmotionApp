package si.um.feri.guessemotion.ui.fragment;

import android.view.View;

import si.um.feri.guessemotion.R;
import si.um.feri.guessemotion.ui.fragment.base.BaseFragment;

/**
 * Created by:
 *
 * Diana Sellárová,
 * Marek Urban,
 * Stanislav Blaško
 *
 * as assignment for Mobile communication services subject on Erasmus+ study.
 */

public class GlobalStatisticsFragment extends BaseFragment {

    public static GlobalStatisticsFragment newInstance() {
        return new GlobalStatisticsFragment();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_statistics_local;
    }

    @Override
    public void setupViews(View rootView) {

    }
}
