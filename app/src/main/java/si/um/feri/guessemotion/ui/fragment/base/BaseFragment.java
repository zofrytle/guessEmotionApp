package si.um.feri.guessemotion.ui.fragment.base;

import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import si.um.feri.guessemotion.util.AndroidUtils;

/**
 * Created by:
 *
 * Diana Sellárová,
 * Marek Urban,
 * Stanislav Blaško
 *
 * as assignment for Mobile communication services subject on Erasmus+ study.
 */

public abstract class BaseFragment extends Fragment {

    /**
     * Returns root layout resource for this fragment.
     */
    @LayoutRes
    public abstract int getLayoutRes();

    /**
     * Place to setup all views. This is called within {@link Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * after inflating layout provided by {@link BaseFragment#getLayoutRes()} and binding views using ButterKnife.
     *
     * @param rootView root view of this fragment, which is not attached to the fragment just yet.
     */
    public abstract void setupViews(View rootView);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            getExtras(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutRes(), container, false);
        ButterKnife.bind(this, rootView);

        // set up keyboard hiding on this fragment if it's necessary. Don't forget to implement
        // hideKeyboardOnTouchOutside() method if you need different behavior.
        if (hideKeyboardOnTouchOutside())
            AndroidUtils.hideKeyboardOnTouchOutside(rootView, getActivity());

        // Bind data to views.
        setupViews(rootView);

        return rootView;
    }


    /**
     * Called during onCreate if needs to resolve arguments. If arguments == null method is not called.
     *
     * @param args bundle passed to this fragment
     */
    public void getExtras(@NonNull Bundle args) {

    }

    /**
     * Returns true if keyboard has to be dismissed when tapped outside of EditText in this fragment.
     */
    public boolean hideKeyboardOnTouchOutside() {
        return true;
    }


    /**
     * Safe version of displaying toast. If fragment host is null
     * (fragment is not attached to activity) toast is not displayed.
     *
     * @param message Test ot be displayed.
     */
    public void showToast(String message) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Convenience method for {@link BaseFragment#showToast(String)}
     *
     * @param stringResource string resource to display in toast message.
     */
    public void showToast(int stringResource) {
        if (getActivity() != null) {
            showToast(getActivity().getString(stringResource));
        }
    }

    /**
     * Tells activity if back button is allowed for this fragment. This is meant to be called within
     * and if this is true, then pop this fragment or do nothing
     * if back is not allowed.
     */
    public boolean allowBackButton() {
        return true;
    }

    /**
     * Returns default entering animation resource for this fragment. Animation is played when fragment
     * is added to fragment manager. Animation can be changed by overriding this method.
     */
    @AnimRes
    public int getEnterAnimation() {
        return 0;
    }

    /**
     * Returns default exit animation resource for this fragment. Animation is played when fragment
     * is added to fragment manager. Animation can be changed by overriding this method.
     */
    @AnimRes
    public int getExitAnimation() {
        return 0;
    }
}
