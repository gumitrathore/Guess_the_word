package com.example.test0.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.test0.R;
import com.example.test0.models.Word;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.SummaryViewHolder> {

    private ArrayList<Word> Words;
    private RecyclerView rv;
    private Activity activity;

    public SummaryAdapter(ArrayList<Word> Words, Activity activity) {
        this.Words = Words;
        this.activity = activity;

    }

    @NonNull
    @Override
    public SummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_summarylist, parent, false);
        rv = (RecyclerView) parent;
        return new SummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SummaryViewHolder summaryViewHolder, int i) {
        final Word Word = Words.get(i);
        summaryViewHolder.tvContent.setText(Word.getContent());
        summaryViewHolder.tvDescription.setText(Word.getHint());

    }

    @Override
    public int getItemCount() {
        return Words.size();
    }

    public class SummaryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvContent)
        TextView tvContent;
        @BindView(R.id.tvDescription)
        TextView tvDescription;
        public SummaryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }


}