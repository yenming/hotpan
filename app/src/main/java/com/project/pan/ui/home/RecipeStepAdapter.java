package com.project.pan.ui.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.pan.R;

import java.util.List;

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mRecipeSteps;

    public RecipeStepAdapter(Context context, List<String> data) {
        this.mContext = context;
        this.mRecipeSteps = data;
    }

    @Override
    public RecipeStepAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_text_line, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.stepNumber = (TextView) view.findViewById(R.id.recipe_step_number);
        holder.stepDetail = (TextView) view.findViewById(R.id.recipe_step_text);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepAdapter.ViewHolder holder, int position) {
        String number = (position + 1) +". ";
        String oneStep = mRecipeSteps.get(position);
        holder.stepNumber.setText(number);
        holder.stepDetail.setText(oneStep);
    }

    @Override
    public int getItemCount() {
        return mRecipeSteps.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView stepNumber;
        TextView stepDetail;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
