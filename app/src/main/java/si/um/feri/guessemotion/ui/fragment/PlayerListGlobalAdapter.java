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

class PlayerListGlobalAdapter extends ArrayAdapter<PlayerScore> {
    private static final String TAG = "PlayerListGlobalAdapter";
    private Context mContext;
    int mResource;

    public PlayerListGlobalAdapter(@NonNull Context context, int resource, @NonNull ArrayList<PlayerScore> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        int score = getItem(position).getScore();
        String country = getItem(position).getCountry();

        PlayerScore playerScore = new PlayerScore(name, score, country);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView txName = (TextView) convertView.findViewById(R.id.textViewGlobalName);
        TextView txScore = (TextView) convertView.findViewById(R.id.textViewGlobalScore);
        TextView txCountry = (TextView) convertView.findViewById(R.id.textViewGlobalCountry);

        txName.setText(name);
        txScore.setText(String.valueOf(score));
        txCountry.setText(country);

        return convertView;
    }
}
