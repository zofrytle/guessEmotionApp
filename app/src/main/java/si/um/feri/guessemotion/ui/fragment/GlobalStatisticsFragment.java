package si.um.feri.guessemotion.ui.fragment;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import si.um.feri.guessemotion.R;
import si.um.feri.guessemotion.game.data.domain.User;
import si.um.feri.guessemotion.service.firebase.FirebaseConnector;
import si.um.feri.guessemotion.ui.activity.MainMenuActivity;
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

    @BindView(R.id.globalListView)
    public ListView listView;

    public static GlobalStatisticsFragment newInstance() {
        return new GlobalStatisticsFragment();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_statistics_global;
    }

    @Override
    public void setupViews(View rootView) {

        final ArrayList<PlayerScore> playerGlobalList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                playerGlobalList.clear();

                DataSnapshot childrenSnapshot = dataSnapshot.child("users");

                for (DataSnapshot userSnapshot : childrenSnapshot.getChildren()) {
                    String name = (String) userSnapshot.child("name").getValue();
                    String country = (String) userSnapshot.child("country").getValue();
                    String uid = userSnapshot.getKey();
                    User user = new User(uid, name, country);

                    int maxScore = 0;
                    for (DataSnapshot childSnapshot : userSnapshot.child("RANKED").getChildren()) {

                        if (childSnapshot.hasChild("score")) {
                            Long score = (Long) childSnapshot.child("score").getValue();
                            int intScore = score.intValue();
                            if (intScore > maxScore) maxScore = intScore;
                        }
                    }

                    playerGlobalList.add(new PlayerScore(user.name, maxScore, user.country));

                    Collections.sort(playerGlobalList, new Comparator<PlayerScore>() {
                        @Override
                        public int compare(PlayerScore o1, PlayerScore o2) {
                            return -(o1.getScore() - o2.getScore());
                        }
                    });
                    PlayerListGlobalAdapter adapter = new PlayerListGlobalAdapter(GlobalStatisticsFragment.this.getContext(), R.layout.adapter_view_global_layout, playerGlobalList);
                    listView.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
