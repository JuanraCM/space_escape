package com.example.juanra.gameproyect;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class AdapterRecycler
        extends RecyclerView.Adapter<AdapterRecycler.RecyclerViewHolder> {

    private List<PlayerInfo> datos;

    public AdapterRecycler(List<PlayerInfo> datos) {
        this.datos = datos;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(
            ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.listitem_player, viewGroup, false);

        RecyclerViewHolder tvh = new RecyclerViewHolder(itemView);
        return tvh;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, int pos) {
        final PlayerInfo item = datos.get(pos);
        viewHolder.bindPlayer(item);
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView lblPlayer;
        private TextView lblScore;

        public RecyclerViewHolder(View v) {
            super(v);
            lblPlayer = v.findViewById(R.id.playerTable);
            lblScore = v.findViewById(R.id.scoreTable);
        }

        public void bindPlayer(PlayerInfo p) {
            lblPlayer.setText(p.getAlias());
            lblScore.setText(p.getHiscore() + "");
        }
    }
}