package si.um.feri.guessemotion.ui.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

import si.um.feri.guessemotion.R;

public class PlayerListAdapter extends ArrayAdapter<PlayerScore> {
    private static final String TAG = "PlayerListAdapter";
    private Context mContext;
    int mResource;

    public PlayerListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<PlayerScore> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        int score = getItem(position).getScore();

        PlayerScore playerScore = new PlayerScore(name, score);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView txName = convertView.findViewById(R.id.textViewName);
        TextView txScore = convertView.findViewById(R.id.textViewScore);

        txName.setText(name);
        txScore.setText(String.valueOf(score));

        return convertView;
    }
}
