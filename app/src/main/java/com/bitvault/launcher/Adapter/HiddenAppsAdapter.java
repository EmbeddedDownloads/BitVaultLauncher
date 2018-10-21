package com.bitvault.launcher.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitvault.launcher.Model.AppModel;
import com.bitvault.launcher.R;

import java.util.ArrayList;

/**
 * show Audio list on GUI
 *
 * @author Anshuman
 */
public class HiddenAppsAdapter extends BaseAdapter {
    /**
     * Audio list server data
     */
    public ArrayList<AppModel> mHiddenAppsList;
    /**
     * Context object
     */
    private Context context;
    @SuppressWarnings("unused")
    private LayoutInflater mLayoutInflater;
    /**
     * Boolean to refresh GUI
     */
    boolean RefreshGUIRequired = true;

    /**
     * Debugging TAG
     */
    @SuppressWarnings("unused")
    private String TAG = HiddenAppsAdapter.class.getSimpleName();

    /**
     * Basic constructor of class
     *
     * @param activity
     */
    public HiddenAppsAdapter(Activity activity) {
        this.context = activity;
        try {
            mLayoutInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            mHiddenAppsList = new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add updated data on List
     */
    public void addUpdateDataIntoList(ArrayList<AppModel> mData) {
        mHiddenAppsList = new ArrayList<>();
        mHiddenAppsList = mData;
    }

    @Override
    public int getCount() {
        if (mHiddenAppsList != null) {
            return mHiddenAppsList.size();
        } else {
            return 0;
        }

    }

    @Override
    public AppModel getItem(int position) {
        if (mHiddenAppsList != null) {
            return mHiddenAppsList.get(position);
        } else {
            return null;
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context.getApplicationContext(),
                    R.layout.hide_apps_row, null);
            new ViewHolder(convertView);
        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        if (mHiddenAppsList != null) {
            final AppModel model = mHiddenAppsList.get(position);
            holder.appName.setText(model.getmAppName());
            holder.appIcon.setImageDrawable(model.getmAppIcon());
            RefreshGUIRequired = false;
            if (model.isHidden())
                holder.hideAppCheckbox.setChecked(true);
            else
                holder.hideAppCheckbox.setChecked(false);
            RefreshGUIRequired = true;

            holder.hideAppCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (RefreshGUIRequired) {
                        mHiddenAppsList.get(position).setHidden(isChecked);
                        notifyDataSetChanged();
                    }
                }
            });

        }
        return convertView;
    }

    /**
     * List view row object and its views
     *
     * @author Shruti
     */
    class ViewHolder {

        TextView appName;
        ImageView appIcon;
        CheckBox hideAppCheckbox;

        public ViewHolder(View view) {
            appName = (TextView) view
                    .findViewById(R.id.appName);
            appIcon = (ImageView) view.findViewById(R.id.appIcon);
            hideAppCheckbox = (CheckBox) view.findViewById(R.id.hideAppCheckbox);
            view.setTag(this);
        }
    }
}
