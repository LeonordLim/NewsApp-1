package com.example.android.newsapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.ViewHolder> {

    public Highlight currentHighlight;
    public Bitmap currentThumbnail;
    //Creating a global highlightView for setting an onclicklistener (TEST basis)
    View highlightView;
    // Store a member variable for the contacts
    private ArrayList<Highlight> mHighlights;
    //Store the context for easy access
    private Context mContext;
    private ArrayList<Bitmap> mThumbnails;

    //Pass in the string array into the constructor
    public ThumbnailAdapter(Context context, ArrayList<Highlight> highlights, ArrayList<Bitmap> thumbnail) {
        mHighlights = highlights;
        mContext = context;
        mThumbnails = thumbnail;
    }

    //Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ThumbnailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //Inflate the custom layout
        highlightView = inflater.inflate(R.layout.list_item, parent, false);

        //Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(highlightView);
        return viewHolder;
    }

    //Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ThumbnailAdapter.ViewHolder viewHolder, int position) {
        //Get the data model based on position
        currentHighlight = mHighlights.get(position);
        int adapterposition = position;
        Log.v("Adapterposition", String.valueOf(adapterposition));

        //Set headline available at the current position
        TextView headlinetextView = viewHolder.headlineTextView;
        headlinetextView.setText(currentHighlight.getHeadline());
        //Set trailtext available at the current position
        TextView trailTextView = viewHolder.trailTextView;
        trailTextView.setText(currentHighlight.getTrailText());
        //Set date available at the current position
        TextView dateTextView = viewHolder.dateTextView;
        //Convert to a readable date format
        String jSDateFormat = currentHighlight.getlastModified();

        //Code to Parse the date into desired format
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat destFormat = new SimpleDateFormat("MMM d, yyyy hh:mm a");
        Date date = null;
        try {
            date = sourceFormat.parse(jSDateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = destFormat.format(date);
        //Setting the Text to the View
        dateTextView.setText(formattedDate);

        //Get the data model based on position
        currentThumbnail = mThumbnails.get(position);

        //Set the thumbnail on the position
        ImageView thumbnailView = viewHolder.thumbnailView;
        thumbnailView.setImageBitmap(currentThumbnail);
    }

    //Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mHighlights.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView headlineTextView;
        public TextView trailTextView;
        public TextView dateTextView;
        public ImageView thumbnailView;
        private Context context;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            headlineTextView = itemView.findViewById(R.id.headline);
            trailTextView = itemView.findViewById(R.id.trail_text);
            dateTextView = itemView.findViewById(R.id.last_modified);
            thumbnailView = itemView.findViewById(R.id.background_thumbnail);
            //Store the context
            context = getContext();
            //Attach a click listener to this entire row view
            itemView.setOnClickListener(this);
        }

        //Handles the row being clicked
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); //gets item position
            Highlight highlight = mHighlights.get(position);
            Log.v("ClickPosition", String.valueOf(position));
            String uri = highlight.getWebUrl();
            Uri parsed = Uri.parse(uri);
            Intent webIntent = new Intent(Intent.ACTION_VIEW, parsed);
            Log.v("IntentUri", parsed.toString());
            context.startActivity(webIntent);
        }
    }

}