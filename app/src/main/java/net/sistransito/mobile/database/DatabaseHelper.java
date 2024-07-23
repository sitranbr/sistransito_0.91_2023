package net.sistransito.mobile.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "talonario.db";
    private static String DB_PATH;
    private SQLiteDatabase database;
    private final Context context;

    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
        // Ajuste o caminho para o diretório do cartão SD
        DB_PATH = context.getExternalFilesDir(null).getAbsolutePath() + "/";
        Log.d(TAG, "Database path set to: " + DB_PATH);

        ensureDatabaseExists();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate called");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade called from version " + oldVersion + " to " + newVersion);
        // Implementar lógica de atualização de banco de dados, se necessário
    }

    public SQLiteDatabase openDatabase() {
        String path = DB_PATH + DB_NAME;
        Log.d(TAG, "Attempting to open database at: " + path);

        if (database == null) {
            Log.d(TAG, "Database instance is null, opening new database instance");
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
            Log.d(TAG, "Database opened successfully");
        } else {
            Log.d(TAG, "Using existing database instance");
        }

        return database;
    }

    @Override
    public synchronized void close() {
        if (database != null) {
            Log.d(TAG, "Closing database");
            database.close();
        }
        super.close();
    }

    private boolean checkDatabase() {
        File file = new File(DB_PATH + DB_NAME);
        boolean exists = file.exists();
        Log.d(TAG, "checkDatabase: Database " + (exists ? "exists" : "does not exist"));
        return exists;
    }

    private void ensureDatabaseExists() {
        if (!checkDatabase()) {
            Log.e(TAG, "Database file does not exist at path: " + DB_PATH + DB_NAME);
            throw new RuntimeException("Database file does not exist.");
        }
    }

}
