package net.sistransito.mobile.database;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import net.sistransito.R;
import net.sistransito.mobile.cnh.dados.DataFromCnh;

public class CnhRepository {

    private static final String TABLE_RENACH = "CONDUTOR_RENACH";
    private static final String CNH_ID = "ID";
    private static final String CNH_NAME = "NM_CONDUTOR";
    private static final String CNH_NUMBER = "CNH";
    private static final String CNH_CPF = "CPF";
    private static final String CNH_MATHERS_NAME = "NM_MAE";
    private static final String CNH_BIRTHDAY = "DTH_NASCIMENTO";
    private static final String CNH_REGISTER = "RG";
    private static final String CNH_STATE = "ID_ESTADO";
    private static final String CNH_VALIDITY = "DTH_VALID_CNH";
    private static final String CNH_CATEGORY = "CATEGORIA";
    private static final String CNH_SITUATION = "SITUACAO_CNH";
    private static final String CNH_POINTS = "PONTUACAO_ATIVA";
    private static final String CNH_BLOCK = "BLOQUEIO_ATIVO";

    private final DatabaseHelper dbHelper;
    private final Context context;

    public CnhRepository(DatabaseHelper dbHelper, Context context) {
        this.dbHelper = dbHelper;
        this.context = context;
    }

    public DataFromCnh getCnhData(DataFromCnh fromCNH, String typeSearch) {
        Cursor myCursor = getCnhCursor(fromCNH, typeSearch);

        if (myCursor != null && myCursor.getCount() > 0) {
            myCursor.moveToFirst();
            fromCNH = populateDataFromCnh(myCursor, fromCNH);
            myCursor.close();
        } else {
            if (myCursor != null) myCursor.close();
            return null;
        }

        return fromCNH;
    }

    private Cursor getCnhCursor(DataFromCnh fromCNH, String typeSearch) {
        String query;
        String[] args;

        switch (typeSearch) {
            case "cnh":
                query = "SELECT * FROM " + TABLE_RENACH + " INDEXED BY cnh WHERE " + CNH_NUMBER + "=?";
                args = new String[]{fromCNH.getRegister()};
                break;
            case "rg":
                query = "SELECT * FROM " + TABLE_RENACH + " INDEXED BY rg WHERE " + CNH_REGISTER + "=?";
                args = new String[]{fromCNH.getIdentity()};
                break;
            case "cpf":
                query = "SELECT * FROM " + TABLE_RENACH + " INDEXED BY cpf WHERE " + CNH_CPF + "=?";
                args = new String[]{fromCNH.getCpf()};
                break;
            default:
                query = "SELECT * FROM " + TABLE_RENACH + " INDEXED BY birthdate WHERE " + CNH_NAME + " LIKE ? AND " + CNH_MATHERS_NAME + " LIKE ? AND " + CNH_BIRTHDAY + " = ?";
                args = new String[]{"%" + fromCNH.getName() + "%", "%" + fromCNH.getMothersName() + "%", fromCNH.getBirthDate()};
        }

        return dbHelper.openDatabase().rawQuery(query, args);
    }

    private DataFromCnh populateDataFromCnh(Cursor cursor, DataFromCnh fromCNH) {
        fromCNH.setName(getColumnValue(cursor, CNH_NAME));
        fromCNH.setRegister(getColumnValue(cursor, CNH_NUMBER));
        fromCNH.setState(filter(getColumnValue(cursor, CNH_STATE), CNH_STATE));
        fromCNH.setBirthDate(getColumnValue(cursor, CNH_BIRTHDAY));
        fromCNH.setCpf(getColumnValue(cursor, CNH_CPF));
        fromCNH.setMothersName(getColumnValue(cursor, CNH_MATHERS_NAME));
        fromCNH.setIdentity(getColumnValue(cursor, CNH_REGISTER));
        fromCNH.setCnhSituation(getColumnValue(cursor, CNH_SITUATION));
        fromCNH.setCnhBlock(getColumnValue(cursor, CNH_BLOCK));
        fromCNH.setCnhCategory(getColumnValue(cursor, CNH_CATEGORY));
        fromCNH.setCnhValidity(getColumnValue(cursor, CNH_VALIDITY));
        fromCNH.setCnhPoints(getColumnValue(cursor, CNH_POINTS));

        return fromCNH;
    }

    private String getColumnValue(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex >= 0) {
            return cursor.getString(columnIndex);
        } else {
            Log.e(getClass().getName(), "Column " + columnName + " not found");
            return "";
        }
    }

    private String filter(String filterData, String xml) {
        try {
            int index = Integer.parseInt(filterData);
            String[] listXml = getXmlArray(xml);
            return listXml[index];
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException exception) {
            Log.d("Error: ", String.valueOf(exception));
            return null;
        }
    }

    private String[] getXmlArray(String xml) {
        switch (xml) {
            case CNH_STATE:
                return context.getResources().getStringArray(R.array.state_array);
            case CNH_CATEGORY:
                return context.getResources().getStringArray(R.array.list_category);
            default:
                throw new IllegalArgumentException("Invalid xml type");
        }
    }
}
