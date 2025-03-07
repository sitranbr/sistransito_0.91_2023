package net.sistransito.mobile.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "talonario.db";
    private static String DB_PATH; // Caminho dinâmico para o armazenamento privado
    private SQLiteDatabase database;
    private final Context context;

    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
        // Use o armazenamento externo privado do app
        DB_PATH = context.getExternalFilesDir("database").getAbsolutePath() + "/";
        Log.d(TAG, "Database path set to: " + DB_PATH);

        // Mova o arquivo para o novo local na primeira execução, se necessário
        //moveDatabaseIfNeeded();
    }

    private void moveDatabaseIfNeeded() {
        File newFile = new File(DB_PATH + DB_NAME);
        if (!newFile.exists()) {
            try {
                // Caminho antigo no /sdcard/Download/database (ajuste conforme necessário)
                File oldFile = new File("/sdcard/Download/database/" + DB_NAME);
                if (oldFile.exists()) {
                    File parentDir = new File(DB_PATH);
                    if (!parentDir.exists()) {
                        parentDir.mkdirs();
                    }
                    // Copie o arquivo para o novo local
                    FileInputStream inputStream = new FileInputStream(oldFile);
                    FileOutputStream outputStream = new FileOutputStream(newFile);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                    inputStream.close();
                    outputStream.close();
                    Log.d(TAG, "Database moved to: " + DB_PATH + DB_NAME);
                    // Opcional: delete o arquivo antigo
                    oldFile.delete();
                }
            } catch (IOException e) {
                Log.e(TAG, "Erro ao mover o banco de dados: " + e.getMessage());
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate chamado");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade chamado da versão " + oldVersion + " para " + newVersion);
    }

    public SQLiteDatabase openDatabase() {
        String path = DB_PATH + DB_NAME;
        File dbFile = new File(path);

        if (!dbFile.exists()) {
            Log.e(TAG, "Arquivo do banco de dados não existe em: " + path);
            throw new RuntimeException("Database file does not exist.");
        }

        if (database == null || !database.isOpen()) {
            Log.d(TAG, "Abrindo banco de dados em: " + path);
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
            Log.d(TAG, "Banco de dados aberto com sucesso");
        } else {
            Log.d(TAG, "Usando instância existente do banco de dados");
        }

        return database;
    }

    @Override
    public synchronized void close() {
        if (database != null) {
            Log.d(TAG, "Fechando banco de dados");
            database.close();
        }
        super.close();
    }
}