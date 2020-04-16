package com.bosch.glm100c.easy_connect;
import com.bosch.glm100c.easy_connect.bluetooth.BLEService;
import com.bosch.glm100c.easy_connect.data.*;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.bosch.glm100c.easy_connect.data.Level;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataService extends SQLiteOpenHelper {
    public static String dataName = "";
    public String internetConnectionType = "MOBILE";
    public String internetConnectionName = "";
    public int dbRequestor =1;
    public int toImageList =0;
    public int isProcessing =0;
    public int toProjects = 0;

    // Laser info
    public BLEService btService;
    public GLMDeviceController deviceController;
    public ArrayList<ProjectError> _error = new ArrayList<ProjectError>();
    public ArrayList<Project> uploadedProject = new ArrayList<Project>();

    // Database Info
    private static final String DATABASE_NAME = "Panoroma14.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_PROJECTS = "PROJECT";
    private static final String TABLE_LEVELS = "LEVEL";
    private static final String TABLE_ROOMS = "ROOM";
    private static final String TABLE_PASSWORD = "PASSWORD";

    // Project Table Columns
    private static final String KEY_PROJECT_ID = "Id";
    private static final String KEY_PROJECT_PROJECTID = "ProjectId";
    private static final String KEY_PROJECT_ADDRESS = "Address";
    private static final String KEY_PROJECT_CITY = "City";
    private static final String KEY_PROJECT_ZIPCODE = "ZIPCode";
    private static final String KEY_PROJECT_STATE = "State";
    private static final String KEY_PROJECT_STATUS = "Status";
    private static final String KEY_PROJECT_STATUS2 = "Status2";
    private static final String KEY_PROJECT_NOTES = "Notes";
    private static final String KEY_PROJECT_COMPLETED = "Completed";
    private static final String KEY_PROJECT_OUTPICS = "OutsidePictures";
    private static final String KEY_PROJECT_3DOUTPICS = "Outside3DPictures";
    private static final String KEY_PROJECT_RESOLUTION = "Resolution";

    // Level Table Columns
    private static final String KEY_LEVEL_ID = "Id";
    private static final String KEY_LEVEL_LEVELID = "LevelId";
    private static final String KEY_LEVEL_PROJECTID = "ProjectId";
    private static final String KEY_LEVEL_NAME = "Name";
    private static final String KEY_LEVEL_STATUS = "Status";
    private static final String KEY_LEVEL_STATUS2 = "Status2";
    private static final String KEY_LEVEL_PICNAME = "PicName";

    // Project Table Columns
    private static final String KEY_ROOM_ID = "Id";
    private static final String KEY_ROOM_ROOMID = "RoomId";
    private static final String KEY_ROOM_LEVELID = "LevelId";
    private static final String KEY_ROOM_PROJECTID = "ProjectId";
    private static final String KEY_ROOM_NAME = "Name";
    private static final String KEY_ROOM_LEVELNAME = "LevelName";
    private static final String KEY_ROOM_ADDRESS = "Address";
    private static final String KEY_ROOM_STATE = "State";
    private static final String KEY_ROOM_CITY = "City";
    private static final String KEY_ROOM_ZIP = "ZIP";
    private static final String KEY_ROOM_PICTURENAME = "PictureName";
    private static final String KEY_ROOM_ROOMLENGTH = "RoomLength";
    private static final String KEY_ROOM_ROOMWIDTH = "RoomWidth";
    private static final String KEY_ROOM_CONNECTORS = "Connectors";
    private static final String KEY_ROOM_CENTERX = "CenterX";
    private static final String KEY_ROOM_CENTERY = "CenterY";
    private static final String KEY_ROOM_SCALEX = "ScaleX";
    private static final String KEY_ROOM_SCALEY = "ScaleY";
    private static final String KEY_ROOM_ROTATION = "Rotation";
    private static final String KEY_ROOM_SHAPE = "Shape";
    private static final String KEY_ROOM_FLIPED = "Fliped";


    // Password Table Columns
    private static final String KEY_PASSWORD_ID = "Id";
    private static final String KEY_PASSWORD_NAME = "Name";
    private static final String KEY_PASSWORD_PASS = "Pass";


    private static DataService sInstance;
    public static synchronized DataService getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DataService(context.getApplicationContext());
        }
        return sInstance;
    }

    private DataService(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
        checkDatabase();
        //db = getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PROJECTS = "CREATE TABLE " + TABLE_PROJECTS +
                "(" +
                KEY_PROJECT_ID + " INTEGER PRIMARY KEY," +
                KEY_PROJECT_PROJECTID + " INT," +
                KEY_PROJECT_ADDRESS + " TEXT," +
                KEY_PROJECT_CITY + " TEXT," +
                KEY_PROJECT_STATE + " TEXT," +
                KEY_PROJECT_ZIPCODE + " TEXT," +
                KEY_PROJECT_STATUS + " INT," +
                KEY_PROJECT_STATUS2 + " TEXT," +
                KEY_PROJECT_NOTES + " TEXT," +
                KEY_PROJECT_COMPLETED + " TEXT," +
                KEY_PROJECT_OUTPICS + " TEXT," +
                KEY_PROJECT_3DOUTPICS + " TEXT," +
                KEY_PROJECT_RESOLUTION + " INT" +
                ")";

        String CREATE_LEVELS = "CREATE TABLE " + TABLE_LEVELS +
                "(" +
                KEY_LEVEL_ID + " INTEGER PRIMARY KEY," +
                KEY_LEVEL_LEVELID + " INT," +
                KEY_LEVEL_PROJECTID + " INT," +
                KEY_LEVEL_NAME + " TEXT," +
                KEY_LEVEL_STATUS + " INT," +
                KEY_LEVEL_STATUS2 + " TEXT," +
                KEY_LEVEL_PICNAME + " TEXT" +
                ")";

        String CREATE_ROOMS = "CREATE TABLE " + TABLE_ROOMS +
                "(" +
                KEY_ROOM_ID + " INTEGER PRIMARY KEY," +
                KEY_ROOM_ROOMID + " INT," +
                KEY_ROOM_LEVELID + " INT," +
                KEY_ROOM_PROJECTID + " INT," +
                KEY_ROOM_NAME + " TEXT," +
                KEY_ROOM_LEVELNAME + " TEXT," +
                KEY_ROOM_ADDRESS + " TEXT," +
                KEY_ROOM_STATE + " TEXT," +
                KEY_ROOM_CITY + " TEXT," +
                KEY_ROOM_ZIP + " TEXT," +
                KEY_ROOM_PICTURENAME + " TEXT," +
                KEY_ROOM_ROOMLENGTH + " TEXT," +
                KEY_ROOM_ROOMWIDTH + " TEXT," +
                KEY_ROOM_CONNECTORS + " TEXT," +
                KEY_ROOM_CENTERX + " TEXT," +
                KEY_ROOM_CENTERY + " TEXT," +
                KEY_ROOM_SCALEX + " TEXT," +
                KEY_ROOM_SCALEY + " TEXT," +
                KEY_ROOM_ROTATION + " TEXT," +
                KEY_ROOM_SHAPE + " TEXT," +
                KEY_ROOM_FLIPED + " TEXT" +
                ")";

        String CREATE_PASSWORD = "CREATE TABLE " + TABLE_PASSWORD +
                "(" +
                KEY_PASSWORD_ID + " INTEGER PRIMARY KEY," +
                KEY_PASSWORD_NAME + " TEXT," +
                KEY_PASSWORD_PASS + " TEXT" +
                ")";
       //==========================================================
       // db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECTS);
       // db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEVELS);
       // db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROOMS);
        // db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASSWORD);
        //==========================================================

        db.execSQL(CREATE_PROJECTS);
        db.execSQL(CREATE_LEVELS);
        db.execSQL(CREATE_ROOMS);
        db.execSQL(CREATE_PASSWORD);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEVELS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROOMS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASSWORD);
            onCreate(db);
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    //======================================================================================
    public String Name;
    public String Pass;
    public Level Level;
    public  Room Room;
    public  int LevelCount = 0;
    public  int RoomCount = 0;
    private List<Project> con;
    public Project Project;

    public List<Project> GetTestData(){
        return con;
    }
    public  void SetStaticLevel(Level _level)
    {
        Level = _level;
    }

    public  Level GetStaticLevel()
    {
        return Level;
    }

    public List<Project> GetAllProjectsData()
    {
            List<Project> projects = new ArrayList<Project>();
            String PROJECTS_SELECT_QUERY = String.format("SELECT * FROM %s", TABLE_PROJECTS);
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(PROJECTS_SELECT_QUERY, null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Project proj = new Project();
                        proj.Id = cursor.getInt(cursor.getColumnIndex(KEY_PROJECT_ID));
                        proj.ProjectId = cursor.getInt(cursor.getColumnIndex(KEY_PROJECT_PROJECTID));
                        proj.Address = cursor.getString(cursor.getColumnIndex(KEY_PROJECT_ADDRESS));
                        proj.City = cursor.getString(cursor.getColumnIndex(KEY_PROJECT_CITY));
                        proj.State = cursor.getString(cursor.getColumnIndex(KEY_PROJECT_STATE));
                        proj.ZIPCode = cursor.getString(cursor.getColumnIndex(KEY_PROJECT_ZIPCODE));
                        proj.Status = cursor.getInt(cursor.getColumnIndex(KEY_PROJECT_STATUS));
                        proj.Status2 = cursor.getString(cursor.getColumnIndex(KEY_PROJECT_STATUS2));
                        proj.Notes = cursor.getString(cursor.getColumnIndex(KEY_PROJECT_NOTES));
                        proj.Completed = cursor.getString(cursor.getColumnIndex(KEY_PROJECT_COMPLETED));
                        proj.OutsidePictures = cursor.getString(cursor.getColumnIndex(KEY_PROJECT_OUTPICS));
                        proj.Resolution = cursor.getInt(cursor.getColumnIndex(KEY_PROJECT_RESOLUTION));
                        proj.Outside3DPictures = cursor.getString(cursor.getColumnIndex(KEY_PROJECT_3DOUTPICS));
                        projects.add(proj);
                    } while (cursor.moveToNext());
                }
            }
            catch (Exception e) {

            }
            finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
        return projects;
    }
    public Project GetProjectData(int id)
    {
        Project k = new Project();
        k.Id = -20;
        List<Project> list = GetAllProjectsData();
        for (Project p: list) {
            if (p.ProjectId ==id) {
                k= p;
                break;
            }
        }
        return k;
    }
    public void DeleteAllProjects()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_PROJECTS, null, null);
            db.setTransactionSuccessful();
        }
        catch (Exception e) {
        } finally {
            db.endTransaction();
        }
    }
    public void DeleteProject(int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_PROJECTS, "Id=?", new String[]{Integer.toString(id)});
            db.setTransactionSuccessful();
        }
        catch (Exception e) {
        } finally {
            db.endTransaction();
        }
    }
    public void InsertProject(Project project)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_PROJECT_PROJECTID, project.ProjectId);
            values.put(KEY_PROJECT_ADDRESS, project.Address);
            values.put(KEY_PROJECT_CITY, project.City);
            values.put(KEY_PROJECT_STATE, project.State);
            values.put(KEY_PROJECT_ZIPCODE, project.ZIPCode);
            values.put(KEY_PROJECT_STATUS, project.Status);
            values.put(KEY_PROJECT_STATUS2, project.Status2);
            values.put(KEY_PROJECT_NOTES, project.Notes);
            values.put(KEY_PROJECT_COMPLETED, project.Completed);
            values.put(KEY_PROJECT_OUTPICS, project.OutsidePictures);
            values.put(KEY_PROJECT_RESOLUTION, project.Resolution);
            values.put(KEY_PROJECT_3DOUTPICS, project.Outside3DPictures);
            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.

            if(project.Id <-1) {
                db.insertOrThrow(TABLE_PROJECTS, null, values);
            }
            else{
                int rows = db.update(TABLE_PROJECTS, values, KEY_PROJECT_ID + "= ?", new String[]{project.Id + ""});
            }
            db.setTransactionSuccessful();
        }
        catch (Exception e) {
        }
        finally {
            db.endTransaction();
        }
    }
    public void UpdateProject(Project project)
    {
        InsertProject(project);
    }

    public List<Level> GetAllLevelsData()
    {
        List<Level> levels = new ArrayList<Level>();
        String LEVELS_SELECT_QUERY = String.format("SELECT * FROM %s", TABLE_LEVELS);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(LEVELS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Level level = new Level();
                    level.Id = cursor.getInt(cursor.getColumnIndex(KEY_LEVEL_ID));
                    level.LevelId = cursor.getInt(cursor.getColumnIndex(KEY_LEVEL_LEVELID));
                    level.ProjectId = cursor.getInt(cursor.getColumnIndex(KEY_LEVEL_PROJECTID));
                    level.Name  = cursor.getString(cursor.getColumnIndex(KEY_LEVEL_NAME));
                    level.Status  = cursor.getInt(cursor.getColumnIndex(KEY_LEVEL_STATUS));
                    level.Status2  = cursor.getString(cursor.getColumnIndex(KEY_LEVEL_STATUS2));
                    level.PicName  = cursor.getString(cursor.getColumnIndex(KEY_LEVEL_PICNAME));
                    levels.add(level);
                } while(cursor.moveToNext());
            }
        }
        catch (Exception e) {
        }
        finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        for(Level i: levels){
            if(i.LevelId != i.Id) {
                i.LevelId = i.Id;
                UpdateLevel(i);
            }
        }
        return levels;
    }

    public List<Level> GetAllLevelsData(int id)
    {
        List<Level> k = new ArrayList<Level>();
        List<Level> list = GetAllLevelsData();
        for (Level p: list) {
            if (p.ProjectId ==id) {
                k.add(p);
            }
        }
        return k;
    }
    public Level GetLevelData(int id)
    {
        Level k = new Level();
        List<Level> list = GetAllLevelsData();
        for (Level p: list) {
            if (p.LevelId ==id) {
                k =p;
                break;
            }
        }
        return k;
    }
    public Level GetLevelDataByProjectIdAndLevelName(int projectId, String levelName)
    {
        Level k = new Level();
        k.Id= -20;
        List<Level> list = GetAllLevelsData();
        for (Level p: list) {
            if (p.ProjectId == projectId && p.Name.trim().toUpperCase().equals(levelName.trim().toUpperCase())) {
                k =p;
                break;
            }
        }
        return k;
    }
    public void DeleteAllLevels()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_LEVELS, null, null);
            db.setTransactionSuccessful();
        }
        catch (Exception e) {
        } finally {
            db.endTransaction();
        }
    }
    public void DeleteLevel(int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_LEVELS, "Id=?", new String[]{Integer.toString(id)});
            db.setTransactionSuccessful();
        }
        catch (Exception e) {
        } finally {
            db.endTransaction();
        }
    }
    public void InsertLevel(Level level)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_LEVEL_LEVELID, level.LevelId);
            values.put(KEY_LEVEL_PROJECTID, level.ProjectId);
            values.put(KEY_LEVEL_NAME, level.Name);
            values.put(KEY_LEVEL_STATUS, level.Status);
            values.put(KEY_LEVEL_STATUS2, level.Status2);
            values.put(KEY_LEVEL_PICNAME, level.PicName);
            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.

            if(level.Id <-1) {
                db.insertOrThrow(TABLE_LEVELS, null, values);
            }
            else{
                int rows = db.update(TABLE_LEVELS, values, KEY_LEVEL_ID + "= ?", new String[]{level.Id + ""});
            }
            db.setTransactionSuccessful();
        }
        catch (Exception e) {
        }
        finally {
            db.endTransaction();
        }
    }
    public void UpdateLevel(Level level)
    {
        InsertLevel(level);
    }

    public List<Room> GetAllRoomsData()
    {
        List<Room> rooms = new ArrayList<Room>();
        String ROOMS_SELECT_QUERY = String.format("SELECT * FROM %s", TABLE_ROOMS);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ROOMS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Room room = new Room();
                    room.Id = cursor.getInt(cursor.getColumnIndex(KEY_ROOM_ID));
                    room.RoomId = cursor.getInt(cursor.getColumnIndex(KEY_ROOM_ROOMID));
                    room.LevelId = cursor.getInt(cursor.getColumnIndex(KEY_ROOM_LEVELID));
                    room.ProjectId = cursor.getInt(cursor.getColumnIndex(KEY_ROOM_PROJECTID));
                    room.Name  = cursor.getString(cursor.getColumnIndex(KEY_ROOM_NAME));
                    room.LevelName  = cursor.getString(cursor.getColumnIndex(KEY_ROOM_LEVELNAME));
                    room.Address  = cursor.getString(cursor.getColumnIndex(KEY_ROOM_ADDRESS));
                    room.City  = cursor.getString(cursor.getColumnIndex(KEY_ROOM_CITY));
                    room.State  = cursor.getString(cursor.getColumnIndex(KEY_ROOM_STATE));
                    room.ZIP  = cursor.getString(cursor.getColumnIndex(KEY_ROOM_ZIP));
                    room.PictureName  = cursor.getString(cursor.getColumnIndex(KEY_ROOM_PICTURENAME));
                    room.RoomLength  = cursor.getString(cursor.getColumnIndex(KEY_ROOM_ROOMLENGTH));
                    room.RoomWidth  = cursor.getString(cursor.getColumnIndex(KEY_ROOM_ROOMWIDTH));
                    room.Connectors  = cursor.getString(cursor.getColumnIndex(KEY_ROOM_CONNECTORS));

                    room.CenterX  = cursor.getString(cursor.getColumnIndex(KEY_ROOM_CENTERX));
                    room.CenterY  = cursor.getString(cursor.getColumnIndex(KEY_ROOM_CENTERY));
                    room.ScaleX  = cursor.getString(cursor.getColumnIndex(KEY_ROOM_SCALEX));
                    room.ScaleY  = cursor.getString(cursor.getColumnIndex(KEY_ROOM_SCALEY));
                    room.Rotation  = cursor.getString(cursor.getColumnIndex(KEY_ROOM_ROTATION));
                    room.Shape  = cursor.getString(cursor.getColumnIndex(KEY_ROOM_SHAPE));
                    room.Fliped  = cursor.getString(cursor.getColumnIndex(KEY_ROOM_FLIPED));
                    rooms.add(room);
                } while(cursor.moveToNext());
            }
        }
        catch (Exception e) {
        }
        finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return rooms;
    }
    public List<Room> GetAllRoomsDataByProjectIdAndLevelId(int pId, int lId)
    {
        List<Room> k = new ArrayList<Room>();
        List<Room> list = GetAllRoomsData();
        for (Room p: list) {
            if (p.ProjectId ==pId && p.LevelId == lId) {
                k.add(p);
            }
        }
        return k;
    }

    public Room GetRoomDataByProjectIdAndLevelIdAndRoomName(int pId, int lId, String name)
    {
        Room k = new Room();
        k.Id=-20;
        List<Room> list = GetAllRoomsData();
        for (Room p: list) {
            if (p.ProjectId == pId && p.Name.trim().toUpperCase().equals(name.trim().toUpperCase()) && p.LevelId ==lId) {
                k =p;
                break;
            }
        }
        return k;
    }

    public Room GetRoomData(int id)
    {
        Room k = new Room();
        List<Room> list = GetAllRoomsData();
        for (Room p: list) {
            if ( p.LevelId ==id) {
                k =p;
                break;
            }
        }
        return k;
    }
    public void DeleteAllRooms()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_ROOMS, null, null);
            db.setTransactionSuccessful();
        }
        catch (Exception e) {
        } finally {
            db.endTransaction();
        }
    }
    public void DeleteRoom(int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_ROOMS, "Id=?", new String[]{Integer.toString(id)});
            db.setTransactionSuccessful();
        }
        catch (Exception e) {
        } finally {
            db.endTransaction();
        }
    }
    public void InsertRoom(Room room)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ROOM_ROOMID, room.RoomId);
            values.put(KEY_ROOM_LEVELID, room.LevelId);
            values.put(KEY_ROOM_PROJECTID, room.ProjectId);
            values.put(KEY_ROOM_NAME, room.Name);
            values.put(KEY_ROOM_LEVELNAME, room.LevelName);
            values.put(KEY_ROOM_ADDRESS, room.Address);
            values.put(KEY_ROOM_CITY, room.City);
            values.put(KEY_ROOM_STATE, room.State);
            values.put(KEY_ROOM_ZIP, room.ZIP);

            values.put(KEY_ROOM_PICTURENAME, room.PictureName);
            values.put(KEY_ROOM_ROOMLENGTH, room.RoomLength);
            values.put(KEY_ROOM_ROOMWIDTH, room.RoomWidth);
            values.put(KEY_ROOM_CONNECTORS, room.Connectors);

            values.put(KEY_ROOM_CENTERX, room.CenterX);
            values.put(KEY_ROOM_CENTERY, room.CenterY);
            values.put(KEY_ROOM_SCALEX, room.ScaleX);
            values.put(KEY_ROOM_SCALEY, room.ScaleY);
            values.put(KEY_ROOM_ROTATION, room.Rotation);
            values.put(KEY_ROOM_SHAPE, room.Shape);
            values.put(KEY_ROOM_FLIPED, room.Fliped);




            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            if(room.Id <-1) {
                db.insertOrThrow(TABLE_ROOMS, null, values);
            }
            else{
                int rows = db.update(TABLE_ROOMS, values, KEY_ROOM_ID + "= ?", new String[]{room.Id + ""});
            }
            db.setTransactionSuccessful();
            int k =0;
        }
        catch (Exception e) {
            int k =0;
        }
        finally {
            db.endTransaction();
        }
    }
    public void UpdateRoom(Room room)
    {
        InsertRoom(room);
    }


    public void SetLevelCount(int value)
    {
        this.LevelCount = value;
    }
    public int GetLevelCount()
    {

        return this.LevelCount;
    }
    public void SetRoomCount(int value)
    {
        this.RoomCount = value;
    }
    public int GetRoomCount()
    {

        return this.RoomCount;
    }
    public void SetProject(Project value)
    {
        this.Project = value;
    }
    public Project GetProject()
    {
        return this.Project;
    }


    public void SetRoom(Room value)
    {
        this.Room = value;
    }
    public Room GetRoom()
    {
        return this.Room;
    }

    public List<RoomName> GetAllRoomNameData(){

        String[] items = {"Attic", "Basement", "Master Bathroom", "Bathroom 1",
        "Bathroom 2", "Bathroom 3",  "Bathroom 4", "Bathroom 5", "Bathroom 6", "Master Bedroom",
        "Bedroom 1",  "Bedroom 2", "Bedroom 3",  "Bedroom 4","Bedroom 5", "Bedroom 6",
        "Bedroom 7", "Bedroom 8", "Deck",  "Den", "Dining Room", "Front Yard", "Back Yard",
        "Right Side Yard", "Left Side Yard",  "Garage 1", "Garage 2", "Garage 3","Garage 4",
        "Hallway 1","Hallway 2","Hallway 3","Hallway 4", "Kitchen","Laundry", "Library 1", "Library 2","Library 3","Library 4",
                "Family Room", "Living Room", "Office 1", "Office 2","Office 3","Office 4", "Pantry 1", "Pantry 2","Pantry 3",
                "Pantry 4","Patio", "Playroom", "Porch", "Staircase 1", "Staircase 2", "Staircase 3",
        "Study","Sun Room","TV Room","Workshop","Craft Room","Classroom", "Foyer", "Storage 1", "Storage 2", "Storage 3","Storage 4",
         "Crawl Space", "Walk-in Closet 1", "Walk-in Closet 2","Walk-in Closet 3","Walk-in Closet 4","Walk-in Closet 5","Walk-in Closet 6",
        "Utility Room 1","Utility Room 2","Utility Room 3","Utility Room 4"};




       Arrays.sort(items);
        List<RoomName> list = new ArrayList<RoomName>();
        for (String item: items ) {
            RoomName name = new RoomName();
            name.Text = item.trim().toUpperCase();
            name.Value = item.trim().toUpperCase();
            name.isCheck = false;
            list.add(name);
        }
        return list;
    }
    private void checkDatabase()
    {
        List<Project> data = GetAllProjectsData();
        if(data != null && data.size()> 0)
        {
            return;
        }
        int counter = 0;
        while (counter <0)
        {
            Project project = new Project();
            project.ProjectId = counter + 1;
            project.Address = "45231 Blue Spruce Ct";
            project.City = "Shelby Township";
            project.State = "MI";
            project.ZIPCode = "48317";
            project.Status = 0;
            project.Status2 = "Created";
            project.Id = -2;
            project.Notes = "";
            project.Completed="No";
            project.OutsidePictures = "[]";
            project.Outside3DPictures = "";
            project.Resolution = 1;
            InsertProject(project);
            counter = counter + 1;
        }
        con = GetAllProjectsData();
    }
    public void ResetData(Data _data){
        for(Project p:_data.projects){
            Project t = GetProjectData(p.ProjectId);
            if(t.Id==-20){
                InsertProject(p);
            }
            else{
                t.Notes= p.Notes;
                UpdateProject(t);
            }
        }
    }

    public void InsertPasswword(Password password)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_PASSWORD_NAME, password.Name);
            values.put(KEY_PASSWORD_PASS, password.Pass);
            if(password.Id <-1) {
                db.insertOrThrow(TABLE_PASSWORD, null, values);
            }
            else{
                int rows = db.update(TABLE_PASSWORD, values, KEY_PASSWORD_ID + "= ?", new String[]{password.Id + ""});
            }
            db.setTransactionSuccessful();
        }
        catch (Exception e) {
        }
        finally {
            db.endTransaction();
        }
    }
    public void UpdatePassword(Password password)
    {
        InsertPasswword(password);
    }

    public void DeletePassword(int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_PASSWORD, "Id=?", new String[]{Integer.toString(id)});
            db.setTransactionSuccessful();
        }
        catch (Exception e) {
        } finally {
            db.endTransaction();
        }
    }

    public List<Password> GetAllPasswordsData()
    {
        List<Password> passwords = new ArrayList<Password>();
        String PASS_SELECT_QUERY = String.format("SELECT * FROM %s", TABLE_PASSWORD);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(PASS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Password pass = new Password();
                    pass.Id = cursor.getInt(cursor.getColumnIndex(KEY_PASSWORD_ID));
                    pass.Name = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD_NAME));
                    pass.Pass = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD_PASS));
                    passwords.add(pass);
                } while(cursor.moveToNext());
            }
        }
        catch (Exception e) {
        }
        finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return passwords;
    }
    private String hasRequired(int projectId){
        List<Room> rooms = GetAllRoomsData();
        int bath =0;
        int kitchen =0;
        int bed =0;
        for(Room r: rooms){
            if(r.ProjectId ==projectId){
                if(r.Name.trim().toLowerCase().contains("bathroom")){
                    bath =1;
                }
                if(r.Name.trim().toLowerCase().contains("kitchen")){
                    kitchen =1;
                }
                if(r.Name.trim().toLowerCase().contains("bedroom")){
                    bed =1;
                }
            }
        }
        String req = "";
        if(bed==0){
            req = "Bedroom";
        }
        if(bath==0){
            req = req + ", Bathroom";
        }
        if(kitchen==0){
            req = req + ", Kitchen";
        }
        return req;
    }

    public ProjectError assignedProjectCompleted(Project p){
        ProjectError pError = new ProjectError();
        String req = hasRequired(p.ProjectId);
        req = req.trim();
        if ((p.OutsidePictures.equals("")== false) && (req.length() == 0) && (p.Outside3DPictures.equals("")== false)) {
            if (p.Completed.equals("Yes")) {
                pError.MissingCheck = "No";
            }
            else{
                pError.MissingCheck = "Yes";
            }
        }
        else{
            pError.ProjectId = p.ProjectId;
            if (p.Completed.equals("Yes")) {
                pError.MissingCheck = "No";
            }
            else{
                pError.MissingCheck = "Yes";
            }
            if  (p.OutsidePictures.equals("")) {
                pError.MissingOutsidePics = "Yes";
            }
            if  (p.Outside3DPictures.equals("")) {
                pError.MissingOutside3DPics = "Yes";
            }
            if(req.length() > 0){
                pError.BK = req;
            }
        }

        ArrayList<Room> badRoom = new ArrayList<Room>();
        ArrayList<Room> goodRoom = new ArrayList<Room>();
        ArrayList<Room> rooms2 = new ArrayList<Room>();

        List<Room> rooms = GetAllRoomsData();
        for(Room r : rooms) {
            if(r.ProjectId == p.ProjectId){
                rooms2.add(r);
                String name = r.Name.trim();
                String name2 = name.toLowerCase();

                String PictureName = r.PictureName.trim();
                String PictureName2 = PictureName.toLowerCase();

                String RoomLength = r.RoomLength.trim();
                String RoomLength2 = RoomLength.toLowerCase();
                if(name2.contains("crawl space") || name2.contains("attic") || name2.contains("staircase")){
                    goodRoom.add(r);
                    continue;
                }
                else if((PictureName2.equals("") == false ) && (RoomLength2.equals("") == false)){
                    goodRoom.add(r);
                    continue;
                }
                else{
                    badRoom.add(r);
                    if(PictureName2.equals("") ){
                        if(pError.MissingPicture.equals("")){
                            pError.MissingPicture = r.LevelName + ": "  +  r.Name;
                        }
                        else{
                            pError.MissingPicture = pError.MissingPicture + ", " + r.LevelName + ": "  +  r.Name;
                        }
                    }

                    if(RoomLength2.equals("")){
                        if(pError.MissingMeasure.equals("")){
                            pError.MissingMeasure =  r.LevelName + ": "  +  r.Name;
                        }
                        else{
                            pError.MissingMeasure = pError.MissingMeasure + ", " + r.LevelName + ": "  +  r.Name;
                        }
                    }
                }
            }
        }


        ArrayList<Level> badLevel = new ArrayList<Level>();
        ArrayList<Level> goodLevel = new ArrayList<Level>();
        List<Level> levels = GetAllLevelsData(p.ProjectId);

        for(Level l: levels){
            boolean isGoodLevel = false;
            for(Room r: rooms2 ){
                if (r.ProjectId == l.ProjectId && r.LevelId == l.LevelId) {
                    isGoodLevel = true;
                    break;
                }
            }
            if(isGoodLevel==false) {
                if(pError.EmptyLevels.equals("")){
                    pError.EmptyLevels = l.Name;
                }
                else{
                    pError.EmptyLevels = pError.EmptyLevels + ", " + l.Name;
                }
                badLevel.add(l);
            }
            else{
                goodLevel.add(l);
            }
        }

        String eptl = pError.EmptyLevels.trim();
        String mMeasure = pError.MissingMeasure.trim();
        String mPicture = pError.MissingPicture.trim();
        String bk = pError.BK.trim();

        if ((bk.equals("")) && (p.OutsidePictures.equals("") == false) && (p.Outside3DPictures.equals("") == false) && (mPicture.equals("")) && (mMeasure.equals("")) && (eptl.equals(""))
                && (levels.size() > 0) && (levels.size() == goodLevel.size()) && (rooms2.size() > 0) && (rooms2.size() == goodRoom.size())) {
            pError = new ProjectError();
            pError.ReturnType = 3;
            return pError;
        }
        else if ((p.OutsidePictures.equals("")) && (p.Outside3DPictures.equals("")) && (levels.size() == 0)) {
            pError = new ProjectError();
            pError.ReturnType = 1;
            return pError;
        }
        else {
            pError.Address = p.Address;
            pError.City = p.City  + ", " + p.State + ", " + p.ZIPCode;
            pError.ReturnType = 2;
            return pError;
        }
    }


    public ProjectError needInfoProjectCompleted(Project p){
        ProjectError pError = new ProjectError();
        if ((p.OutsidePictures.equals("") == false) &&  (p.Outside3DPictures.equals("") == false)) {
            if (p.Completed.equals("Yes")) {
                pError.MissingCheck = "No";
            }
            else{
                pError.MissingCheck = "Yes";
            }
        }
        else{
            pError.ProjectId = p.ProjectId;
            if (p.Completed.equals("Yes")) {
                pError.MissingCheck = "No";
            }
            else{
                pError.MissingCheck = "Yes";
            }
        }

        ArrayList<Room> badRoom = new ArrayList<Room>();
        ArrayList<Room> goodRoom = new ArrayList<Room>();
        ArrayList<Room> rooms2 = new ArrayList<Room>();
        List<Room> rooms = GetAllRoomsData();

        for(Room r :rooms) {
            if(r.ProjectId == p.ProjectId){
                rooms2.add(r);
                String name = r.Name.trim();
                String name2 = name.toLowerCase();

                String PictureName = r.PictureName.trim();
                String PictureName2 = PictureName.toLowerCase();

                String RoomLength = r.RoomLength.trim();
                String RoomLength2 = RoomLength.toLowerCase();

                if(name2.contains("crawl space") || name2.contains("attic") || name2.contains("staircase")){
                    goodRoom.add(r);
                    continue;
                }
                else if((PictureName2.equals("") == false) && (RoomLength2.equals("") == false)){
                    goodRoom.add(r);
                    continue;
                }
                else{
                    badRoom.add(r);
                    if(PictureName2.equals("")){
                        if(pError.MissingPicture.equals("")){
                            pError.MissingPicture = r.LevelName + ": "  +  r.Name;
                        }
                        else{
                            pError.MissingPicture = pError.MissingPicture + ", " + r.LevelName + ": "  +  r.Name;
                        }
                    }
                    if(RoomLength2.equals("")){
                        if(pError.MissingMeasure.equals("")){
                            pError.MissingMeasure =  r.LevelName + ": "  +  r.Name;
                        }
                        else{
                            pError.MissingMeasure = pError.MissingMeasure + ", " + r.LevelName + ": "  +  r.Name;
                        }
                    }
                }
            }
        }


        ArrayList<Level> badLevel = new ArrayList<Level>();
        ArrayList<Level> goodLevel = new ArrayList<Level>();
        List<Level> levels = GetAllLevelsData(p.ProjectId);


        for(Level l:levels){
            boolean isGoodLevel = false;
            for( Room r :rooms2 ){
                if (r.ProjectId == l.ProjectId && r.LevelId == l.LevelId) {
                    isGoodLevel = true;
                    break;
                }
            }
            if(isGoodLevel==false) {
                if(pError.EmptyLevels.equals("")){
                    pError.EmptyLevels = l.Name;
                }
                else{
                    pError.EmptyLevels = pError.EmptyLevels + ", " + l.Name;
                }
                badLevel.add(l);
            }
            else{
                goodLevel.add(l);
            }
        }
        if ((p.OutsidePictures.equals("")) && (p.Outside3DPictures.equals("")) && (levels.size() == 0)) {
            pError = new ProjectError();
            pError.ReturnType = 1;
            return pError;
        }
        else if (((p.OutsidePictures.equals("") == false) || (p.Outside3DPictures.equals("") == false)) && (levels.size() == 0)) {

            pError = new ProjectError();
            pError.ReturnType = 3;
            return pError;
        }
        else if((levels.size() > 0) && (levels.size() == goodLevel.size()) && (rooms2.size() > 0) && (rooms2.size() == goodRoom.size())){
            pError = new ProjectError();
            pError.ReturnType = 3;
            return pError;
        }
        else{
            pError.Address = p.Address;
            pError.City = p.City  + ", " + p.State + ", " + p.ZIPCode;
            pError.ReturnType = 2;
            return pError;
        }
    }


    public ProjectError projectCompleted(Project p){
        if(p.Status == 0){
            return assignedProjectCompleted(p);
        }
        else
        {
            return needInfoProjectCompleted(p);
        }
    }
}
