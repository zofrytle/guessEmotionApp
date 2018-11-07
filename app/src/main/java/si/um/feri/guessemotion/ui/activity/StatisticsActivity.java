package si.um.feri.guessemotion.ui.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;

import butterknife.BindView;
import butterknife.OnClick;
import si.um.feri.guessemotion.R;
import si.um.feri.guessemotion.ui.activity.base.BaseActivity;
import si.um.feri.guessemotion.ui.adapter.StatisticsAdapter;
import si.um.feri.guessemotion.ui.fragment.GlobalStatisticsFragment;
import si.um.feri.guessemotion.ui.fragment.LocalStatisticsFragment;
import si.um.feri.guessemotion.ui.view.NonSwipeableViewPager;

/**
 * Created by:
 *
 * Diana Sellárová,
 * Marek Urban,
 * Stanislav Blaško
 *
 * as assignment for Mobile communication services subject on Erasmus+ study.
 */

public class StatisticsActivity extends BaseActivity {

    @BindView(R.id.sliding_tabs) TabLayout mTabLayout;
    @BindView(R.id.viewpager) NonSwipeableViewPager mViewPager;

    private StatisticsAdapter mStatisticsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpViewPager();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_statistics;
    }

    private void setUpViewPager() {
        mStatisticsAdapter = new StatisticsAdapter(getSupportFragmentManager());
        mStatisticsAdapter.addFragment(LocalStatisticsFragment.newInstance(),getString(R.string.statistics_local));
        mStatisticsAdapter.addFragment(GlobalStatisticsFragment.newInstance(), getString(R.string.statistics_global));

        mViewPager.setAdapter(mStatisticsAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @OnClick(R.id.activity_button_back)
    public void onBackButtonClick() {
        onBackPressed();
    }
}
