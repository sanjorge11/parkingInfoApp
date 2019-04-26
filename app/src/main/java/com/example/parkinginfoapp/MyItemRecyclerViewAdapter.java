package com.example.parkinginfoapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.parkinginfoapp.ListFragment.OnListFragmentInteractionListener;
import com.example.parkinginfoapp.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> implements Filterable {

    private List<Lot> mValues;
    private final OnListFragmentInteractionListener mListener;
    private List<Lot> mValuesFull;

    public MyItemRecyclerViewAdapter(List<Lot> items, OnListFragmentInteractionListener listener) {
        mValues = new ArrayList<>(items);
        mListener = listener;
        this.mValuesFull = new ArrayList<>(mValues);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        //holder.mIdView.setText(mValues.get(position).object_id.toString());
//        String text = mValues.get(position).lot_name +  "\n" +
//                "Available with " + mValues.get(position).permit_type + " permit";
        String text =
                "Available with " + mValues.get(position).permit_type + " permit";

        holder.mContentView.setText(mValues.get(position).lot_name);

        holder.mSubContentView.setText(text);

//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(holder.mItem);
//                }
//            }
//        });

        holder.mCarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public Filter getFilter() {
        return lotFilter;
    }

    private Filter lotFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Lot> filteredLots = new ArrayList<>();

            if(constraint == null || constraint.length() == 0 || constraint.toString().equals("All")) {
                filteredLots.addAll(mValuesFull);
            } else {
                String filterPattern = constraint.toString().trim();

                for(Lot lot : mValuesFull) {
                    if(lot.getPermitType().equals(filterPattern)) {
                        filteredLots.add(lot);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredLots;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mValues.clear();
            mValues.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final Button mCarIcon;
       // public final TextView mIdView;
        public final TextView mContentView;
        public final TextView mSubContentView;
        public Lot mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCarIcon = (Button) view.findViewById(R.id.carIcon);
           // mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
            mSubContentView = (TextView) view.findViewById(R.id.subcontent);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
