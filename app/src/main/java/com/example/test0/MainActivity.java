package com.example.test0;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import com.example.test0.adapters.SummaryAdapter;
import com.example.test0.models.Level;
import com.example.test0.models.Word;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity{

    //MyAdapter mAdapter;
    CustomGridLayoutManager layoutManager;
    Handler handler;
    public int page = 0;
    TextView textView,tv,Thint,Timertv;
    ViewGroup _root;
    int x,y;
    String type="easy";
    int lvl = 1;
    AlarmManager builder1;
    private int _xDelta;
    private int _yDelta;
    RelativeLayout rlayout1;
    SummaryAdapter summaryAdapter;
    RelativeLayout rlayout2;
    RelativeLayout rlayoutParent;
    int windowwidth; // Actually the width of the RelativeLayout.
    int windowheight; // Actually the height of the RelativeLayout.
    boolean intersect = false;
    int timer;
    Runnable runnable;
    boolean paused = false;

    ArrayList<Word> s1 = new ArrayList<>();
    ArrayList<Word> temp = new ArrayList<>();
    ArrayList<Level> levels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            if (getIntent().getExtras() != null) {
                if(getIntent().hasExtra("type")){
                    type = getIntent().getStringExtra("type");
                    if(type.contentEquals("easy")){
                        lvl = 1;
                    } else if(type.contentEquals("medium")){
                        lvl = 2;
                    } else if(type.contentEquals("hard")){
                        lvl = 3;
                    } else if(type.contentEquals("genius")){
                        lvl = 4;
                    }
                }
                if (getIntent().hasExtra("levels")) {
                    Type arrayListType = new TypeToken<ArrayList<Level>>() {}.getType();
                    levels.addAll((ArrayList<Level>) new Gson().fromJson(getIntent().getStringExtra("levels"), arrayListType));
                    for(int i = 0; i< levels.size(); i++){
                        if(levels.get(i).getType().contentEquals(type)){
                            Level level = levels.get(i);
                            s1.clear();
                            s1.addAll(level.getWords());
                            break;
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        handler = new Handler();

        rlayoutParent = new RelativeLayout(this);
        rlayoutParent.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        rlayoutParent.setBackgroundResource(R.mipmap.bg);

        rlayout1 = new RelativeLayout(this);
        RelativeLayout.LayoutParams params1 =new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rlayout1.setLayoutParams(params1);

        rlayout2 = new RelativeLayout(this);
        RelativeLayout.LayoutParams params2 =new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rlayout2.setLayoutParams(params2);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        Thint = new TextView(this);
        Timertv = new TextView(this);
        windowwidth = size.x;
        windowheight = size.y;
        rlayout2.setY( windowheight / 2);
        Thint.setY((float) (windowheight / 1.33));
        Timertv.setX((float) (windowwidth / 1.2));
        Timertv.setY((float) (windowheight / 1.5));
        if(s1.size() > 0) {
            showData(s1.get(page));
        }

    }

    @Override
    protected void onPause(){
        paused = true;
        super.onPause();
        handler.removeCallbacks(runnable);

    }
    @Override
    public void onResume(){
        super.onResume();
       if(paused == true) {
           handler.postDelayed(runnable, 1000);
       }
    }


    public void showData(final Word word){

        rlayoutParent.removeAllViews();
        rlayout1.removeAllViews();
        rlayout2.removeAllViews();
        final String element = word.getContent();
        final String hint = word.getHint();
        timer = word.getTimer();
        final String[] chars = element.split("(?!^)");
        final List<String> list = Arrays.asList(chars);
        Collections.shuffle(list);
        final String[] chars2 = new String[list.size()];

        for (int i = 0; i < list.size(); i++) {
            tv = new TextView(MainActivity.this);
            RelativeLayout.LayoutParams params;
            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(150*(i), 10, 10, 10);

            tv.setId(i + list.size());

            tv.setLayoutParams(params);
            tv.setText("__");
            tv.setTextColor(getResources().getColor(R.color.green));
            tv.setBackgroundResource(R.drawable.rounded_rectangle);
            tv.setPadding(40, 50, 40, 50);
            int[] location = new int[2];

            rlayout2.addView(tv);

            Log.d("id", tv.getId() + "" );
        }

        for (int i = 0; i < list.size(); i++) {
            textView = new TextView(MainActivity.this);
            textView.setText(String.valueOf(list.get(i)));
            RelativeLayout.LayoutParams params;
            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(150*(i), 10, 10, 10);

            textView.setId(i);

            textView.setLayoutParams(params);
            textView.setTextColor(getResources().getColor(R.color.blue));
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
                            Toast.makeText(MainActivity.this, "OUT", Toast.LENGTH_SHORT).show();
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
                                if( page + 1 < s1.size()) {
                                    Toast.makeText(MainActivity.this, "Hurray !! You Won.", Toast.LENGTH_LONG).show();
                                    handler.removeCallbacks(runnable);
                                    page = page+1;
                                    showData(s1.get(page));
                                } else{
                                    handler.removeCallbacks(runnable);
                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                                    builder2.setTitle("CHOOSE ONE");
                                    builder2.setCancelable(false);
                                    builder2.setItems(new CharSequence[]
                                                    {"PLAY AGAIN", "NEXT LEVEL", "VIEW SOLUTIONS","EXIT"},
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // The 'which' argument contains the index position
                                                    // of the selected item
                                                    switch (which) {
                                                        case 0:
                                                            page = -1;
                                                            if( page + 1 < s1.size()) {
                                                                Toast.makeText(MainActivity.this, "Hurray !! You Won.", Toast.LENGTH_LONG).show();
                                                                handler.removeCallbacks(runnable);
                                                                page = page + 1;
                                                                showData(s1.get(page));
                                                            }
                                                            break;
                                                        case 1:
                                                            if (lvl >= 4) {
                                                                Toast.makeText(MainActivity.this, "Game Over", Toast.LENGTH_LONG).show();
                                                                Intent intent = new Intent(MainActivity.this, GameoverActivity.class);
                                                                startActivity(intent);
                                                            } else {
                                                                lvl = lvl + 1;
                                                                for (int i = 0; i < levels.size(); i++) {
                                                                    if (levels.get(i).getType().contentEquals(getType())) {
                                                                        Level level = levels.get(i);
                                                                        s1.clear();
                                                                        s1.addAll(level.getWords());
                                                                        break;
                                                                    }
                                                                }
                                                                page = -1;
                                                                if( page + 1 < s1.size()) {
                                                                    Toast.makeText(MainActivity.this, "Hurray !! You Won.", Toast.LENGTH_LONG).show();
                                                                    handler.removeCallbacks(runnable);
                                                                    page = page + 1;
                                                                    showData(s1.get(page));
                                                                }
                                                            }
                                                            break;
                                                        case 2:
                                                            timer = 9999999;
                                                            temp.addAll(s1);
                                                            final RecyclerView recyclerView;
                                                            setContentView(R.layout.activity_summary);
                                                            recyclerView = findViewById(R.id.summary_recyclerview);
                                                            recyclerView.setHasFixedSize(true);
                                                            LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL,false);
                                                            recyclerView.setNestedScrollingEnabled(false);
                                                            recyclerView.setLayoutManager(layoutManager);
                                                            recyclerView.setNestedScrollingEnabled(false);

                                                            summaryAdapter = new SummaryAdapter(temp,MainActivity.this);
                                                            recyclerView.setAdapter(summaryAdapter);
                                                            ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT| ItemTouchHelper.RIGHT) {
                                                                @Override
                                                                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                                                                    return false;
                                                                }


                                                                @Override
                                                                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                                                                    temp.remove(viewHolder.getAdapterPosition());
                                                                    if(temp.size() <= 0){
                                                                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                                                        builder.setTitle("CHOOSE ONE");
                                                                        builder.setCancelable(false);
                                                                        builder.setItems(new CharSequence[]
                                                                                        {"PLAY AGAIN", "NEXT LEVEL", "EXIT"},
                                                                                new DialogInterface.OnClickListener() {
                                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                                        // The 'which' argument contains the index position
                                                                                        // of the selected item
                                                                                        switch (which) {
                                                                                            case 0:
                                                                                                page = -1;
                                                                                                if( page + 1 < s1.size()) {
                                                                                                    Toast.makeText(MainActivity.this, "Hurray !! You Won.", Toast.LENGTH_LONG).show();
                                                                                                    handler.removeCallbacks(runnable);
                                                                                                    page = page + 1;
                                                                                                    showData(s1.get(page));
                                                                                                }
                                                                                                break;
                                                                                            case 1:
                                                                                                if (lvl >= 4) {
                                                                                                    Toast.makeText(MainActivity.this, "Game Over", Toast.LENGTH_LONG).show();
                                                                                                    Intent intent = new Intent(MainActivity.this, GameoverActivity.class);
                                                                                                    startActivity(intent);
                                                                                                } else {
                                                                                                    lvl = lvl + 1;
                                                                                                    for (int i = 0; i < levels.size(); i++) {
                                                                                                        if (levels.get(i).getType().contentEquals(getType())) {
                                                                                                            Level level = levels.get(i);
                                                                                                            s1.clear();
                                                                                                            s1.addAll(level.getWords());
                                                                                                            break;
                                                                                                        }
                                                                                                    }
                                                                                                    page = -1;
                                                                                                    if( page + 1 < s1.size()) {
                                                                                                        Toast.makeText(MainActivity.this, "Hurray !! You Won.", Toast.LENGTH_LONG).show();
                                                                                                        handler.removeCallbacks(runnable);
                                                                                                        page = page + 1;
                                                                                                        showData(s1.get(page));
                                                                                                    }
                                                                                                }
                                                                                                break;
                                                                                            case 2:
                                                                                                Intent a = new Intent(Intent.ACTION_MAIN);
                                                                                                a.addCategory(Intent.CATEGORY_HOME);
                                                                                                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                                                startActivity(a);
                                                                                                break;

                                                                                        }
                                                                                    }
                                                                                });
                                                                        builder.create().show();
                                                                    } else {
                                                                        recyclerView.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
                                                                    }
                                                                }

                                                                @Override
                                                                public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                                                                }
                                                            };
                                                            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
                                                            break;
                                                        case 3:
                                                            Intent a = new Intent(Intent.ACTION_MAIN);
                                                            a.addCategory(Intent.CATEGORY_HOME);
                                                            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(a);
                                                            break;

                                                    }
                                                }
                                            });
                                    builder2.create().show();
                                }
                                for (int j=0;j<chars2.length;j++){
                                    chars2[j] = "";
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
            rlayout1.setGravity(Gravity.CENTER);
        }

        RelativeLayout.LayoutParams params0;
        params0 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        Timertv.setLayoutParams(params0);
        Timertv.setTextColor(this.getResources().getColor(R.color.colorPrimaryDark));
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
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    handler.removeCallbacks(this);
                    builder1.setMessage("GAME OVER!");
                    builder1.setIcon(android.R.drawable.ic_dialog_alert);
                    builder1.setCancelable(false);
                    builder1.setPositiveButton(
                            "Try Again",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    showData(s1.get(page));
                                    dialog.cancel();
                                }
                            });

                    builder1.setNegativeButton(
                            "Skip",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if(page + 1 < s1.size()) {
                                        page = page+1;
                                        showData(s1.get(page));
                                    } else {
                                        handler.removeCallbacks(runnable);
                                        AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                                        builder2.setTitle("CHOOSE ONE");
                                        builder2.setCancelable(false);
                                        builder2.setItems(new CharSequence[]
                                                        {"PLAY AGAIN","VIEW SOLUTIONS","EXIT"},
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // The 'which' argument contains the index position
                                                        // of the selected item
                                                        switch (which) {
                                                            case 0:
                                                                page = -1;
                                                                if( page + 1 < s1.size()) {
                                                                    Toast.makeText(MainActivity.this, "Hurray !! You Won.", Toast.LENGTH_LONG).show();
                                                                    handler.removeCallbacks(runnable);
                                                                    page = page + 1;
                                                                    showData(s1.get(page));
                                                                }
                                                                break;
                                                            case 1:
                                                                timer = 9999999;
                                                                temp.addAll(s1);
                                                                final RecyclerView recyclerView;
                                                                setContentView(R.layout.activity_summary);
                                                                recyclerView = findViewById(R.id.summary_recyclerview);
                                                                recyclerView.setHasFixedSize(true);
                                                                LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL,false);
                                                                recyclerView.setNestedScrollingEnabled(false);
                                                                recyclerView.setLayoutManager(layoutManager);
                                                                recyclerView.setNestedScrollingEnabled(false);

                                                                summaryAdapter = new SummaryAdapter(temp,MainActivity.this);
                                                                recyclerView.setAdapter(summaryAdapter);
                                                                ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT| ItemTouchHelper.RIGHT) {
                                                                    @Override
                                                                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                                                                        return false;
                                                                    }


                                                                    @Override
                                                                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                                                                        temp.remove(viewHolder.getAdapterPosition());
                                                                        if(temp.size() <= 0){
                                                                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                                                            builder.setTitle("CHOOSE ONE");
                                                                            builder.setCancelable(false);
                                                                            builder.setItems(new CharSequence[]
                                                                                            {"PLAY AGAIN","EXIT"},
                                                                                    new DialogInterface.OnClickListener() {
                                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                                            // The 'which' argument contains the index position
                                                                                            // of the selected item
                                                                                            switch (which) {
                                                                                                case 0:
                                                                                                    page = -1;
                                                                                                    if( page + 1 < s1.size()) {
                                                                                                        Toast.makeText(MainActivity.this, "Hurray !! You Won.", Toast.LENGTH_LONG).show();
                                                                                                        handler.removeCallbacks(runnable);
                                                                                                        page = page + 1;
                                                                                                        showData(s1.get(page));
                                                                                                    }
                                                                                                    break;
                                                                                                case 1:
                                                                                                    Intent a = new Intent(Intent.ACTION_MAIN);
                                                                                                    a.addCategory(Intent.CATEGORY_HOME);
                                                                                                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                                                    startActivity(a);
                                                                                                    break;

                                                                                            }
                                                                                        }
                                                                                    });
                                                                            builder.create().show();
                                                                        } else {
                                                                            recyclerView.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                                                                    }
                                                                };
                                                                new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
                                                                break;
                                                            case 3:
                                                                Intent a = new Intent(Intent.ACTION_MAIN);
                                                                a.addCategory(Intent.CATEGORY_HOME);
                                                                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                startActivity(a);
                                                                break;

                                                        }
                                                    }
                                                });
                                        builder2.create().show();
                                    }
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = builder1.create();
                    alert.show();

                }
            }
        };

        RelativeLayout.LayoutParams paramsExample;
        paramsExample = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        Thint.setLayoutParams(paramsExample);
        Thint.setTextColor(this.getResources().getColor(R.color.colorPrimaryDark));
        Thint.setBackgroundResource(R.drawable.rounded_rectangle);
        Thint.setPadding(30, 30, 30, 30);
        Thint.setGravity(Gravity.CENTER);
        Thint.setTextSize(25);
        Thint.setText(hint);

        rlayoutParent.addView(rlayout2);
        rlayoutParent.addView(rlayout1);
        rlayoutParent.addView(Thint);
        rlayoutParent.addView(Timertv);

        setContentView(rlayoutParent);
        handler.postDelayed(runnable, 1000);

    }

    public String getType(){
        if(lvl == 1){
            return "easy";
        } else if(lvl == 2){
            return "medium";
        } else if(lvl == 3){
            return "hard";
        } else if(lvl == 4){
            return "genius";
        } else {
            lvl = 1;
            return "easy";
        }

    }

   public class CustomGridLayoutManager extends LinearLayoutManager {
       private boolean isScrollEnabled = true;

       public CustomGridLayoutManager(Context context, int orientation, boolean reverseLayout) {
           super(context, orientation, reverseLayout);
       }

       public void setScrollEnabled(boolean flag) {
           this.isScrollEnabled = flag;
       }

       @Override
       public boolean canScrollHorizontally() {
           //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
           return isScrollEnabled && super.canScrollHorizontally();
       }
   }
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

   public int getPage(){
     return page++;
   }

}
