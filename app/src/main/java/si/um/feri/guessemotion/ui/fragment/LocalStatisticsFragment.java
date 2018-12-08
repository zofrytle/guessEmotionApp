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
import si.um.feri.guessemotion.ui.activity.MainMenuActivity;
import si.um.feri.guessemotion.ui.fragment.base.BaseFragment;

public class LocalStatisticsFragment extends BaseFragment {

    @BindView(R.id.localListView)
    public ListView listView;

    public static LocalStatisticsFragment newInstance() {
        return new LocalStatisticsFragment();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_statistics_local;
    }

    @Override
    public void setupViews(View rootView) {

        LayoutInflater inflater = LayoutInflater.from(this.getContext());

        final ArrayList<PlayerScore> playerList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                playerList.clear();

                DataSnapshot childrenSnapshot = dataSnapshot.child("users");

                for (DataSnapshot userSnapshot : childrenSnapshot.getChildren()) {
                    String name = (String) userSnapshot.child("name").getValue();
                    String country = (String) userSnapshot.child("country").getValue();
                    String uid = userSnapshot.getKey();
                    User user = new User(uid, name, country);


                    if (MainMenuActivity.getUser().country.equals(userSnapshot.child("country").getValue())) {

                        int maxScore = 0;
                        for (DataSnapshot childSnapshot : userSnapshot.child("RANKED").getChildren()) {

                            if (childSnapshot.hasChild("score")) {
                                Long score = (Long) childSnapshot.child("score").getValue();
                                int intScore = score.intValue();
                                if (intScore > maxScore) maxScore = intScore;
                            }
                        }

                        playerList.add(new PlayerScore(user.name, maxScore, user.country));

                        Collections.sort(playerList, new Comparator<PlayerScore>() {
                            @Override
                            public int compare(PlayerScore o1, PlayerScore o2) {
                                return -(o1.getScore() - o2.getScore());
                            }
                        });
                        PlayerListAdapter adapter = new PlayerListAdapter(LocalStatisticsFragment.this.getContext(), R.layout.adapter_view_layout, playerList);
                        listView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


     }


}
