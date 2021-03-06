package br.com.developen.sig.widget;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {


    private GestureDetector gestureDetector;

    private RecyclerClickListener clickListener;


    public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final RecyclerClickListener clickListener) {

        this.clickListener = clickListener;

        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            public void onLongPress(MotionEvent e) {

                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());

                if (child != null && clickListener != null)

                    clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));

            }

        });

    }


    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

        View child = rv.findChildViewUnder(e.getX(), e.getY());

        if (child != null && clickListener != null && gestureDetector.onTouchEvent(e))

            clickListener.onClick(child, rv.getChildAdapterPosition(child));

        return false;

    }


    public void onTouchEvent(RecyclerView rv, MotionEvent e) {}


    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}


}