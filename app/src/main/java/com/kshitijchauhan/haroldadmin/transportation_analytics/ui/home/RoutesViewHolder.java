package com.kshitijchauhan.haroldadmin.transportation_analytics.ui.home;

import android.view.View;
import android.widget.TextView;

import com.kshitijchauhan.haroldadmin.transportation_analytics.models.RouteRequest;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RoutesViewHolder extends RecyclerView.ViewHolder {

    public RoutesViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void bind(RouteRequest routeRequest) {
        ((TextView) itemView).setText(String.valueOf(routeRequest.getStartPointLat()));
    }
}
