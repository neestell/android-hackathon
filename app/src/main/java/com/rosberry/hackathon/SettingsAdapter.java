package com.rosberry.hackathon;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.text1;

/**
 * Created by dmitry on 09.12.2017.
 */

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingViewHolder> {

    private List<AuthorizationType> items = new ArrayList<AuthorizationType>() {

        {
            add(AuthorizationType.FA2);
            add(AuthorizationType.FINGERPRINT);
        }
    };

    @Override
    public SettingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new SettingViewHolder(LayoutInflater.from(parent.getContext())
                                             .inflate(android.R.layout.test_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SettingViewHolder holder, int position) {
        final AuthorizationType item = items.get(position);

        holder.bindView(item.name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                switch (item) {
                    case FA2:
                        v.getContext().startActivity(new Intent(v.getContext(), FA2SettingsActivity.class) {

                            {
                                setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            }
                        });

                        break;
                    case FINGERPRINT:
                        v.getContext().startActivity(new Intent(v.getContext(), FingerprintSettingsActivity.class) {

                            {
                                setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            }
                        });
                        break;

                    // TODO: 09.12.2017 Add your authorization settings
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class SettingViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public SettingViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(text1);
            textView.setTextAppearance(itemView.getContext(), android.R.style.TextAppearance_Large);
        }

        public void bindView(String name) {
            textView.setText(name);
        }
    }

}
