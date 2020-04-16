package com.bosch.glm100c.easy_connect.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.Rectangle;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.bosch.glm100c.easy_connect.LevelActivity;
import com.bosch.glm100c.easy_connect.R;
import com.bosch.glm100c.easy_connect.data.GrahamScan;
import com.bosch.glm100c.easy_connect.data.NumberOfClusters;
import com.bosch.glm100c.easy_connect.data.Room;
import com.bosch.glm100c.easy_connect.data.Tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class CustomView extends View {
    private GestureDetector mGestureDetector;
    private  final int cellSize =9;
    private final int SQUARE_SIZE_DEF = 200;
    private Tile[][] tiles = new Tile[cellSize][cellSize];
    private int[][] emptyTiles = new int[cellSize][cellSize];
    private Context _context;
    private LevelActivity _Main;
    private int defaultColor;
    private boolean startRoomTilesCollection = false;
    private int numbOfSelection =0;
    private Set<Integer> exited = new HashSet<Integer>();
    private Set<Integer> exitedAll = new HashSet<Integer>();
    private List<Integer> myColors = new ArrayList<Integer>();
    public int snappSize = 0;
    public CustomView(Context context) {
        super(context);
        _context = context;
        _Main = (LevelActivity) _context;
        init(null);
    }
    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _context = context;
        _Main = (LevelActivity) _context;
        init(attrs);
    }
    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        _context = context;
        _Main = (LevelActivity) _context;
        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {
        if (set == null) {
            return;
        }
        getColor();
        defaultColor = Color.GRAY;
        TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.CustomView);
        int count = 1;
        for(int i=0; i<cellSize; i++){
            for(int j=0; j<cellSize; j++){
                Tile t = new Tile();
                t.Id =count;
                t.X =i;
                t.Y = j;
                t.mRectSquare = new Rect();
                t.mPaintSquare = new Paint(Paint.ANTI_ALIAS_FLAG);
                t.mSquareColor =  defaultColor;
                t.mSquareSize = ta.getDimensionPixelSize(R.styleable.CustomView_square_size, SQUARE_SIZE_DEF);
                t.mPaintSquare.setColor(t.mSquareColor);
                tiles[i][j] =t;
                count = count +1;
            }
        }
        ta.recycle();
        Android_Gesture_Detector  android_gesture_detector  =  new Android_Gesture_Detector();
        mGestureDetector = new GestureDetector(_context, android_gesture_detector);
        this.setOnTouchListener(mTouchListener);
    }

    private Tile setProperty(Tile t, List<Room> list3){
        ArrayList<Integer> area = new ArrayList<Integer>();
        for(Room l: list3){
            String ids = l.Connectors.trim();
            if(ids==""){
                continue;
            }
            String [] ids2 = ids.split(",");
            for(String u:ids2){
                area.add(Integer.parseInt(u.trim()));
            }
            if(area.contains(t.Id)){
                int h = l.Id % myColors.size();
                t.mSquareColor =  myColors.get(h);
                t.mPaintSquare.setColor(t.mSquareColor);
            }
            area.clear();
        }

        return t;
    }
    private void setSquareColor(){
       // TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.CustomView);
       // ta.recycle();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        exitedAll.clear();
        List<Room> list3 = _Main.getRooms();
        int w = canvas.getWidth()/cellSize;
        snappSize = w;
        canvas.drawColor(Color.WHITE);
        int startLeft =0;
        int startTop =0;
        for(int i=0; i<cellSize; i++){
            for(int j=0; j<cellSize; j++){
                Tile t = setProperty(tiles[i][j], list3);
                t.mRectSquare.left = startLeft;
                t.mRectSquare.top = startTop;
                t.mSquareSize = w;
                t.mRectSquare.right = t.mRectSquare.left + t.mSquareSize;
                t.mRectSquare.bottom = t.mRectSquare.top + t.mSquareSize;
                t.mPaintSquare.setStyle(Paint.Style.FILL);
                t.mPaintSquare.setColor(t.mSquareColor);
                t.mPaintSquare.setAlpha(95);;
                canvas.drawRect(t.mRectSquare, t.mPaintSquare);
                t.mPaintSquare.setStyle(Paint.Style.STROKE);
                t.mPaintSquare.setColor(Color.LTGRAY);
                canvas.drawRect(t.mRectSquare, t.mPaintSquare);
                startLeft = startLeft + w;
            }
            startLeft =0;
            startTop = startTop + w;
        }


        int counter =0;
       for(Room l: list3){
            String ids = l.Connectors.trim();
            if(ids==""){
                continue;
            }
            String [] ids2 = ids.split(",");
            ArrayList<Integer> area = new ArrayList<Integer>();
            for(String u:ids2){
                area.add(Integer.parseInt(u.trim()));
            }
           exitedAll.addAll(area);

            int[][] bound = new int[cellSize][cellSize];

            ArrayList<Point> areaPoints = new ArrayList<Point>();
            for(int i=0; i<cellSize; i++){
                for(int j=0; j<cellSize; j++){
                    Tile t = tiles[i][j];
                    if(area.contains(t.Id)){
                        bound[i][j] =1;
                        areaPoints.addAll(RectangleToPoints(t.mRectSquare));
                    }
                    else{
                        bound[i][j] =0;
                    }
                }
            }
            if(areaPoints.size()<1){
                continue;
            }

           ArrayList<Point> topData = new ArrayList<Point>();
           ArrayList<Point> bottomData = new ArrayList<Point>();
           for(int col=0; col<bound[0].length; col++){
               ArrayList<Rect> co = new ArrayList<Rect>();
               for(int row=0; row<bound.length; row++){
                   if(bound[row][col] ==1){
                       co.add(tiles[row][col].mRectSquare);
                   }
               }
               if(co.size()>0) {
                   Rect top = co.get(0);
                   Rect bottom = co.get(co.size()-1);
                   topData.add(new Point(top.left, top.top));
                   topData.add(new Point(top.right, top.top));
                   bottomData.add( new Point(bottom.left, bottom.bottom));
                   bottomData.add( new Point(bottom.right , bottom.bottom));
               }
           }
           for (int i = bottomData.size() - 1; i >= 0; i--) {
               topData.add(bottomData.get(i));
           }

           List<Point> convexHull = topData;
           Path path = new Path();
           int sumY =0;
           int sumX = 0;
            boolean first = true;
            for(Point point : convexHull){
                if(first){
                    first = false;
                    path.moveTo(point.x, point.y);
                    sumX = sumX + point.x;
                    sumY = sumY + point.y;
                }
                else{
                    path.lineTo(point.x, point.y);
                    sumX = sumX + point.x;
                    sumY = sumY + point.y;
                }
            }
            Point p = convexHull.get(0);
            path.lineTo(p.x, p.y);
            int stateColor = getStateColor(l);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(stateColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(8);
            canvas.drawPath(path, paint);

           int x = (sumX/convexHull.size())-(cellSize +5);
           int y = (sumY/convexHull.size());
           if(counter ==1){
               y = (sumY/convexHull.size() -(cellSize -15));
               counter = counter +1;
           }
           else if(counter ==2){
               y = (sumY/convexHull.size() -(cellSize +15));
               counter = 0;
           }
           else{
               counter = counter +1;
           }

           int oldX = (int)Double.parseDouble(l.CenterX.trim());
           int oldY = (int)Double.parseDouble(l.CenterY.trim());

           if(oldX==0 || oldY==0){
               l.CenterX = ("" + x).trim();
               l.CenterY = ("" + y).trim();
              // _Main.updateRoom2(l);
           }
           else{
               x = oldX;
               y = oldY;
           }

           l.CenterX = ("" + x).trim();
           l.CenterY = ("" + y).trim();
           _Main.updateRoom2(l);

           Paint paint2 = new Paint();
           paint2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
           paint2.setColor(Color.BLACK);
           paint2.setTextSize(33);
           canvas.drawText(l.Name, x, y, paint2);
       }
    }

    public void cleanDeleteRoom(Room r){
        String ids = r.Connectors.trim();
        if(ids==""){
            return;
        }
        String [] ids2 = ids.split(",");
        ArrayList<Integer> area = new ArrayList<Integer>();
        for(String u:ids2){
            area.add(Integer.parseInt(u.trim()));
        }
        for(int i=0; i<cellSize; i++){
            for(int j=0; j<cellSize; j++){
                Tile t = tiles[i][j];
                if(area.contains(t.Id)){
                    t.mSquareColor = defaultColor;
                }
            }
        }
        _Main.deleteSelectRoom();
        invalidate();
    }
    private int getStateColor(Room r){
        int k =  0;
        if (r.Name.trim().toLowerCase().contains("crawl space")||r.Name.trim().toLowerCase().contains("attic")||
                r.Name.trim().toLowerCase().contains("staircase")) {
            k= Color.BLACK;
        }
        else if ((r.PictureName.trim().equals("") == false) && (r.RoomLength.trim().equals("") ==false)) {
            k= Color.BLACK;;
        }
        else if ((r.PictureName.trim().equals("")) && (r.RoomLength.trim().equals(""))) {
            k = Color.WHITE;
        }
        else if (r.PictureName.trim().equals("")) {
            k = Color.RED;
        }
        else if (r.RoomLength.trim().equals("")) {
            k= Color.MAGENTA;
        }
        return k;
    }
    public void swapColor() {
       // mPaintSquare.setColor(mPaintSquare.getColor() == mSquareColor ? Color.RED : mSquareColor);
        postInvalidate();
    }
    public List<Point> RectangleToPoints(Rect rect) {
        List<Point> points = new ArrayList<>();
        points.add(new Point(rect.left, rect.top));
        points.add( new Point(rect.right , rect.top));
        points.add( new Point(rect.right , rect.bottom));
        points.add( new Point(rect.left, rect.bottom));
        return points;
    }
    public void saveLayout(){
        ArrayList<Integer> selected = new ArrayList<Integer>();
        for(int i =0; i<cellSize; i++){
            for(int j =0; j<cellSize; j++){
                if(emptyTiles[i][j] ==1){
                    exited.add(tiles[i][j].Id);
                    selected.add(tiles[i][j].Id);
                }
            }
        }
        if(selected.size()>0 && currentRoom!=null) {
            String ids = Arrays.toString(selected.toArray());
            currentRoom.Connectors = (ids.substring(1, ids.indexOf("]"))).trim();
            _Main.updateRoom(currentRoom);
            currentRoom = null;
        }
        getEmptyBoard();
        startRoomTilesCollection = false;
        numbOfSelection =0;
        postInvalidate();
    }
    public void deleteUnfinished(int x, int y){
        Tile t = tiles[x][y];
        t.mSquareColor = defaultColor;
        emptyTiles[x][y] =0;
        numbOfSelection = numbOfSelection -1;
        postInvalidate();
    }
    private boolean checkCluster(){
        NumberOfClusters solution = new NumberOfClusters();
        int k = solution.findNumberofClusters(emptyTiles.clone());
        if(k==1){
            return true;
        }
        return false;
    }
    public void getEmptyBoard() {
        for(int i=0; i<cellSize; i++){
            for(int j=0; j<cellSize; j++){
                emptyTiles[i][j] =0;
            }
        }
    }
    private void startRoomTiles(){
        startRoomTilesCollection = true;
        getEmptyBoard();
    }
    private Room currentRoom;
    public void startLayout(Room room, Point p){
        currentRoom = room;
        boolean isfound = false;
        for(int i=0; i<cellSize; i++){
            for(int j=0; j<cellSize; j++){
                Tile t = tiles[i][j];
                if(t.mRectSquare.contains(p.x,p.y)) {
                    startRoomTiles();
                    t.mSquareColor = Color.MAGENTA;
                    numbOfSelection =1;
                    t.RoomRoom = room.Name;
                    emptyTiles[t.X][t.Y] = 1;
                   // t.Id =-1;
                    isfound = true;
                    break;
                }
            }
            if(isfound){
                break;
            }
        }
        postInvalidate();
    }
    private OnTouchListener mTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            mGestureDetector.onTouchEvent(event);
            return true;
        }
    };
    private class Android_Gesture_Detector implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
        private float move_orgX =-1, move_orgY = -1;
        @Override
        public boolean onDown(MotionEvent event) {
            move_orgX = event.getRawX();
            move_orgY = event.getRawY();
            return true;
        }
        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            Point p = new Point(x, y);
            for(int i=0; i<cellSize; i++){
                for(int j=0; j<cellSize; j++){
                    Tile t = tiles[i][j];
                    if(t.mRectSquare.contains(p.x,p.y)) {
                        if(exited.contains(t.Id)){
                            _Main.handleMessage("This part belongs to another room!");
                            return true;
                        }
                    }
                }
            }

            if(startRoomTilesCollection) {
                boolean isfound = false;
                for (int i = 0; i < cellSize; i++) {
                    for (int j = 0; j < cellSize; j++) {
                        Tile t = tiles[i][j];
                        if (t.mRectSquare.contains(x, y)) {
                            if (t.mSquareColor == defaultColor) {
                                emptyTiles[i][j] =1;
                                if(checkCluster()){
                                    t.mSquareColor = Color.MAGENTA;
                                    numbOfSelection = numbOfSelection +1;
                                }
                                else{
                                    t.mSquareColor = defaultColor;
                                    emptyTiles[i][j] =0;
                                    _Main.handleMessage("Please select neighbor cells only!");
                                }
                            }
                            else {

                                if(numbOfSelection==1){
                                    _Main.deleteRoom(t.X, t.Y);
                                }
                                else{
                                    t.mSquareColor = defaultColor;
                                    emptyTiles[i][j] =0;
                                    numbOfSelection = numbOfSelection -1;
                                }
                            }
                            isfound = true;
                            break;
                        }
                    }
                    if (isfound) {
                        break;
                    }
                }
                postInvalidate();
            }
            else{
                _Main.handleMessage("Press & hold on empty cell to start a new room OR Double tap this cell to take measurements & pictures");
            }
            return true;
        }
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }
        @Override
        public void onShowPress(MotionEvent e) {
        }
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if(startRoomTilesCollection){
                _Main.handleMessage("Save your work before continue");
                return true;
            }
            int x = (int) e.getX();
            int y = (int) e.getY();
            for(int i=0; i<cellSize; i++){
                for(int j=0; j<cellSize; j++){
                    Tile t = tiles[i][j];
                    if(t.mRectSquare.contains(x,y)) {
                        List<Room>list = _Main.getRooms();
                        for(Room l: list) {
                            String ids = l.Connectors.trim();
                            if (ids == "") {
                                continue;
                            }
                            String[] ids2 = ids.split(",");
                            ArrayList<Integer> area = new ArrayList<Integer>();
                            for (String u : ids2) {
                                area.add(Integer.parseInt(u.trim()));
                            }
                            if(area.contains(t.Id)){
                                _Main.handleRoomDoubleTap(l);
                                return true;
                            }

                        }
                    }
                }
            }
            return true;
        }
        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return true;
        }
        @Override
        public void onLongPress(MotionEvent event) {
            if(startRoomTilesCollection){
                _Main.handleMessage("Save the existing layout before starting a new room");
                return;
            }
            numbOfSelection =0;
            int x = (int)event.getX();
            int y = (int)event.getY();
            Point p = new Point(x,y);

            for(int i=0; i<cellSize; i++){
                for(int j=0; j<cellSize; j++){
                    Tile t = tiles[i][j];
                    if(t.mRectSquare.contains(p.x,p.y)) {
                        if(exited.contains(t.Id)){
                            _Main.handleMessage("This part belongs to another room!");
                            return;
                        }
                        if(exitedAll.contains(t.Id)){
                            _Main.handleMessage("This part belongs to another room!");
                            return;
                        }
                    }
                }
            }

            List<Room> list5 =_Main.getRooms();
            ArrayList<Integer> covered = new ArrayList<Integer>();
            for(Room l: list5){
                String ids = l.Connectors.trim();
                if(ids==""){
                    continue;
                }
                String [] ids2 = ids.split(",");
                for(String u:ids2){
                    covered.add(Integer.parseInt(u.trim()));
                }
            }
            getEmptyBoard();
            for(int i=0; i<cellSize; i++){
                for(int j=0; j<cellSize; j++){
                    if(covered.contains(tiles[i][j].Id)){
                        emptyTiles[i][j] = 1;
                    }
                }
            }
            for(int i=0; i<cellSize; i++){
                for(int j=0; j<cellSize; j++){
                    Tile t = tiles[i][j];
                    if(t.mRectSquare.contains(p.x,p.y)) {
                        emptyTiles[i][j] = 1;
                        if(checkCluster() == false){
                            _Main.handleMessage("New room must share a boundary with existing room.");
                            return;
                        }
                    }
                }
            }
            _Main.getRoomName(p);
        }
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            List<Room>list = _Main.getRooms();
            for(Room r:list){
                int oldX = (int)Double.parseDouble(r.CenterX);
                int oldY = (int)Double.parseDouble(r.CenterY);
                if (((Math.abs(oldX - e2.getX()) < snappSize)) && (Math.abs(oldY - e2.getY()) < snappSize)) {
                    float offsetX = e2.getRawX() - move_orgX;
                    float offsetY = e2.getRawY() - move_orgY;
                    float a = oldX+offsetX;
                    float b = oldY + offsetY;
                    r.CenterX =("" + a);
                    r.CenterY =("" + b);
                    move_orgX = e2.getRawX();
                    move_orgY = e2.getRawY();
                     _Main.updateRoom2(r);
                    invalidate();
                    break;
                }
            }
            return true;
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return true;
        }
    }
    public void getColor(){
        myColors.add(Color.parseColor("#FDCC0D"));
        myColors.add(Color.parseColor("#dab600"));
        myColors.add(Color.parseColor("#a98600"));
        //myColors.add(Color.parseColor("#9A6324"));
       // myColors.add(Color.parseColor("#f58231"));
        //myColors.add(Color.parseColor("#ffd8b1"));
        //myColors.add(Color.parseColor("#808000"));
        //myColors.add(Color.parseColor("#ffe119"));
        //myColors.add(Color.parseColor("#fffac8"));
        //myColors.add(Color.parseColor("#42d4f4"));
        //myColors.add(Color.parseColor("#911eb4"));
        //myColors.add(Color.parseColor("#e6beff"));
        //myColors.add(Color.parseColor("#f032e6"));
    }
}
