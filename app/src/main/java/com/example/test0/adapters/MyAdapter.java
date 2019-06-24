package com.example.test0.adapters;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test0.activites.GameoverActivity;
import com.example.test0.MainActivity;
import com.example.test0.R;
import com.example.test0.models.Word;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<Word> mDataset;
    Activity activity;
    TextView textView,tv,Thint,Timertv;
    Handler handler;
    ViewGroup _root;
    int x,y;
    AlarmManager builder1;
    RecyclerView recyclerView;
    private int _xDelta;
    private int _yDelta;
    RelativeLayout rlayout1;
    RelativeLayout rlayout2;
    RelativeLayout rlayoutParent;
    int windowwidth; // Actually the width of the RelativeLayout.
    int windowheight; // Actually the height of the RelativeLayout.
    boolean intersect = false;
    int timer;
    Runnable runnable;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View v) {
            super(v);


        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Word> myDataset, Handler handler, Activity activity, RecyclerView recyclerView) {
        this.activity = activity;
        this.recyclerView = recyclerView;
        this.handler = handler;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        rlayoutParent = new RelativeLayout(activity);
        rlayoutParent.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        rlayoutParent.setBackgroundResource(R.mipmap.bg);
        MyViewHolder vh = new MyViewHolder(rlayoutParent);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Word word = mDataset.get(position);
        final String element = word.getContent();
        final String hint = word.getHint();
        timer = word.getTimer();

        rlayout1 = new RelativeLayout(activity);
        RelativeLayout.LayoutParams params1 =new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rlayout1.setLayoutParams(params1);

        rlayout2 = new RelativeLayout(activity);
        RelativeLayout.LayoutParams params2 =new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rlayout2.setLayoutParams(params2);
        // rlayout2.setPadding(40,40,40,40);

        Thint = new TextView(activity);
        Timertv = new TextView(activity);
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        windowwidth = size.x;
        windowheight = size.y;
        rlayout2.setY( windowheight / 2);
        Thint.setY((float) (windowheight / 1.33));
        Timertv.setX((float) (windowwidth / 1.2));

        RelativeLayout.LayoutParams params0;
        params0 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        Timertv.setLayoutParams(params0);
        Timertv.setTextColor(activity.getResources().getColor(R.color.colorPrimaryDark));
        Timertv.setBackgroundResource(R.drawable.rounded_rectangle);
        Timertv.setPadding(30, 30, 30, 30);
        Timertv.setTextSize(25);
        runnable = new Runnable() {
            public void run() {
                if (timer >= 0) {
                    Timertv.setText(String.valueOf(timer--));
                    handler.postDelayed(this,1000);
                }
                else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                    handler.removeCallbacks(this);
                    builder1.setMessage("GAME OVER!");
                    builder1.setIcon(android.R.drawable.ic_dialog_alert);
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Try Again",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    notifyItemChanged(holder.getAdapterPosition());
                                    dialog.cancel();
                                }
                            });

                    builder1.setNegativeButton(
                            "Skip",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    final int page = ((MainActivity) activity).getPage();
                                    if(page + 1 < mDataset.size()) {
                                        recyclerView.scrollToPosition(page + 1);
                                    } else {
                                        Toast.makeText(activity, "Game Over", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(activity, GameoverActivity.class);
                                        activity.startActivity(intent);
                                    }
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = builder1.create();
                    alert.show();

                }
            }
        };
        handler.postDelayed(runnable, 1000);

        RelativeLayout.LayoutParams paramsExample;
        paramsExample = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        Thint.setLayoutParams(paramsExample);
        Thint.setTextColor(activity.getResources().getColor(R.color.colorPrimaryDark));
        Thint.setBackgroundResource(R.drawable.rounded_rectangle);
        Thint.setPadding(30, 30, 30, 30);
        Thint.setGravity(Gravity.CENTER);
        Thint.setTextSize(25);
        Thint.setText(hint);

        rlayoutParent.removeAllViews();

        final String[] chars = element.split("(?!^)");
        final List<String> list = Arrays.asList(chars);
        Collections.shuffle(list);
        final String[] chars2 = new String[list.size()];

        for (int i = 0; i < list.size(); i++) {
            tv = new TextView(activity);
            RelativeLayout.LayoutParams params;
            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(150*(i), 10, 10, 10);

            tv.setId(i + list.size());

            tv.setLayoutParams(params);
            tv.setText(" ");
            tv.setTextColor(activity.getResources().getColor(R.color.green));
            tv.setBackgroundResource(R.drawable.rounded_rectangle);
            tv.setPadding(40, 40, 40, 40);
            int[] location = new int[2];

            rlayout2.addView(tv);

            Log.d("id", tv.getId() + "" );
        }

        for (int i = 0; i < list.size(); i++) {
            textView = new TextView(activity);
            textView.setText(String.valueOf(list.get(i)));
            RelativeLayout.LayoutParams params;
            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(150*(i), 10, 10, 10);

            textView.setId(i);

            textView.setLayoutParams(params);
            textView.setTextColor(activity.getResources().getColor(R.color.blue));
            textView.setBackgroundResource(R.drawable.rounded_rectangle);
            textView.setPadding(40, 40, 40, 40);


            textView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    final int X = (int) event.getRawX();
                    final int Y = (int) event.getRawY();

                    // Check if the image view is out of the parent view and report it if it is.
                    // Only report once the image goes out and don't stack toasts.
                    if (isOut(view)) {
                        if (!isOutReported) {
                            isOutReported = true;
                            Toast.makeText(activity, "OUT", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        isOutReported = false;
                    }
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                            // _xDelta and _yDelta record how far inside the view we have touched. These
                            // values are used to compute new margins when the view is moved.
                            _xDelta = X - view.getLeft();
                            _yDelta = Y - view.getTop();
                            break;
                        case MotionEvent.ACTION_UP:
                            int[] firstPosition = new int[2];
                            view.getLocationOnScreen(firstPosition);
                            intersect = false;
                            TextView v1 = (TextView) rlayout1.findViewById(view.getId());

                            for (int j=list.size();j<2*list.size();j++) {
                                int[] secondPosition = new int[2];

                                TextView tv = (TextView) rlayout2.findViewById(j);
                                tv.getLocationOnScreen(secondPosition);

                                // Rect constructor parameters: left, top, right, bottom
                                Rect rectFirstView = new Rect(firstPosition[0], firstPosition[1],
                                        firstPosition[0] + view.getMeasuredWidth(), firstPosition[1] + view.getMeasuredHeight());
                                Rect rectSecondView = new Rect(secondPosition[0], secondPosition[1],
                                        secondPosition[0] + tv.getMeasuredWidth(), secondPosition[1] + tv.getMeasuredHeight());
                                if (Rect.intersects(rectFirstView, rectSecondView)) {
                                    RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) view.getLayoutParams();
                                    layoutParams1.leftMargin = secondPosition[0];
                                    layoutParams1.topMargin = secondPosition[1]-50;
                                    view.setLayoutParams(layoutParams1);
                                    intersect = true;
                                    chars2[j - list.size()] = v1.getText().toString();
                                    break;
                                }
                            }
                            if(!intersect) {
                                RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) view.getLayoutParams();
                                layoutParams1.leftMargin = firstPosition[0];
                                layoutParams1.topMargin = firstPosition[1];
                                view.setLayoutParams(layoutParams1);
                            }

                            for (int k=0;k<list.size();k++){
                                int[] secondPosition = new int[2];

                                if(v1.getId() != view.getId()) {
                                    v1.getLocationOnScreen(secondPosition);

                                    Rect rectFirstView = new Rect(firstPosition[0], firstPosition[1],
                                            firstPosition[0] + view.getMeasuredWidth(), firstPosition[1] + view.getMeasuredHeight());
                                    Rect rectSecondView = new Rect(secondPosition[0], secondPosition[1],
                                            secondPosition[0] + v1.getMeasuredWidth(), secondPosition[1] + v1.getMeasuredHeight());

                                    if (Rect.intersects(rectFirstView, rectSecondView)) {
                                        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) view.getLayoutParams();
                                        layoutParams1.leftMargin = firstPosition[0];
                                        layoutParams1.topMargin = firstPosition[1];
                                        view.setLayoutParams(layoutParams1);
                                    }
                                }
                            }
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int j=0;j<chars2.length;j++){
                                stringBuilder.append(chars2[j]);
                            }
                            if(element.contentEquals(stringBuilder.toString())){
                                final int page = ((MainActivity) activity).getPage();
                                if(page + 1 < mDataset.size()) {
                                    Toast.makeText(activity, "Hurray !! You Won.", Toast.LENGTH_LONG).show();
                                    recyclerView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            handler.removeCallbacks(runnable);
                                            recyclerView.scrollToPosition(page + 1);
                                        }
                                    });
                                } else {
                                    Toast.makeText(activity, "Game Over !! You Won.", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(activity, GameoverActivity.class);
                                    activity.startActivity(intent);
                                }
                            }
                            break;
                        case MotionEvent.ACTION_POINTER_DOWN:
                            break;
                        // Do nothing
                        case MotionEvent.ACTION_POINTER_UP:
                            // Do nothing
                            break;
                        case MotionEvent.ACTION_MOVE:
                            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view
                                    .getLayoutParams();
                            // Image is centered to start, but we need to unhitch it to move it around.
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                lp.removeRule(RelativeLayout.CENTER_HORIZONTAL);
                                lp.removeRule(RelativeLayout.CENTER_VERTICAL);
                            } else {
                                lp.addRule(RelativeLayout.CENTER_HORIZONTAL, 0);
                                lp.addRule(RelativeLayout.CENTER_VERTICAL, 0);
                            }
                            lp.leftMargin = X - _xDelta;
                            lp.topMargin = Y - _yDelta;
                            // Negative margins here ensure that we can move off the screen to the right
                            // and on the bottom. Comment these lines out and you will see that
                            // the image will be hemmed in on the right and bottom and will actually shrink.
                            //lp.rightMargin = view.getWidth() - lp.leftMargin - windowwidth;
                            //lp.bottomMargin = view.getHeight() - lp.topMargin - windowheight;
                            view.setLayoutParams(lp);
                            break;
                    }
                    // invalidate is redundant if layout params are set or not needed if they are not set.
//        mRrootLayout.invalidate();
                    return true;
                }
            });
            rlayout1.addView(textView);

        }

        rlayoutParent.addView(rlayout1);
        rlayoutParent.addView(rlayout2);
        rlayoutParent.addView(Thint);
        rlayoutParent.addView(Timertv);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // Tracks when we have reported that the image view is out of bounds so we
    // don't over report.
    private boolean isOutReported = false;

    private boolean isOut(View view) {
        // Check to see if the view is out of bounds by calculating how many pixels
        // of the view must be out of bounds to and checking that at least that many
        // pixels are out.
        float percentageOut = 0.50f;
        int viewPctWidth = (int) (view.getWidth() * percentageOut);
        int viewPctHeight = (int) (view.getHeight() * percentageOut);

        return ((-view.getLeft() >= viewPctWidth) ||
                (view.getRight() - windowwidth) > viewPctWidth ||
                (-view.getTop() >= viewPctHeight) ||
                (view.getBottom() - windowheight) > viewPctHeight);
    }

    public Runnable getRunnable(){
        return runnable;
    }

}