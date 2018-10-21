package com.bitvault.launcher.Adapter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bitvault.launcher.AllAppsScreen;
import com.bitvault.launcher.Model.AppModel;
import com.bitvault.launcher.R;
import com.bitvault.launcher.Utils.GlobalConfig;
import com.bitvault.launcher.database.DBManager;

import java.util.List;


/**
 * Created by Anshuman on 18/05/16.
 */
public class AppsListAdapter extends RecyclerView.Adapter<AppsListAdapter.MyViewHolder> {
    /**
     * Context of Activity
     */
    private Context mContext;
    /**
     * Debugging TAG
     */
    private String TAG = AppsListAdapter.class.getSimpleName();
    private List<AppModel> mAppData;
    Animation animShake = null;
    private String viewName = "";

    /**
     * This class for handling View handler
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView appIcon;
        public TextView appName;
        public RelativeLayout deleteAppsView, parentView;

        public MyViewHolder(View view) {
            super(view);
            appIcon = (ImageView) view.findViewById(R.id.icon);
            appName = (TextView) view.findViewById(R.id.appName);
            deleteAppsView = (RelativeLayout) view.findViewById(R.id.deleteAppsView);
            parentView = (RelativeLayout) view.findViewById(R.id.parentView);
        }
    }

    /**
     * Advent Calender adapter
     *
     * @param mContext
     * @param mAppData
     * @param viewName
     */
    public AppsListAdapter(Context mContext, List<AppModel> mAppData, String viewName) {
        this.mContext = mContext;
        this.mAppData = mAppData;
        this.viewName = viewName;
        animShake = AnimationUtils.loadAnimation(mContext, R.anim.shaking_animation);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.app_item_icon_text, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.appIcon.setImageDrawable(mAppData.get(position).getmAppIcon());
        holder.appName.setText(mAppData.get(position).getmAppName());

        if (viewName.equalsIgnoreCase(AllAppsScreen.class.getSimpleName()))
            holder.appName.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
        else
            holder.appName.setTextColor(mContext.getResources().getColor(R.color.colorWhite));

        if (GlobalConfig.isDeleteFeatureEnable) {
            holder.deleteAppsView.setVisibility(View.VISIBLE);
            holder.parentView.setAnimation(animShake);
        } else {
            holder.deleteAppsView.setVisibility(View.GONE);
            holder.parentView.clearAnimation();
        }

        /*Delete App form device*/
        holder.deleteAppsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DELETE);
                intent.setData(Uri.parse("package:" + mAppData.get(position).getmAppPackage()));
                mContext.startActivity(intent);
            }
        });

        /*Open App of Grid*/
        holder.appIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppModel app = (AppModel) mAppData.get(position);
                /*DataBase Work*/
                DBManager dbManager = new DBManager(mContext);
                dbManager.open();
                /*Update apps uses count*/
                dbManager.updateAppPriorities(app.getmAppName(), app.getmAppPackage());
                dbManager.close();


                if (app != null) {
                    ResolveInfo launchable = app.getAppInfo();
                    ActivityInfo activity = launchable.activityInfo;
                    ComponentName name = new ComponentName(activity.applicationInfo.packageName,
                            activity.name);
                    Intent i = new Intent(Intent.ACTION_MAIN);

                    i.addCategory(Intent.CATEGORY_LAUNCHER);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    i.setComponent(name);
                    mContext.startActivity(i);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mAppData.size();
    }
}