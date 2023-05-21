package sudy.studyexample.finalapp;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


public class CarDatabase extends SQLiteAssetHelper {

    public static final String DATABASE_NAME = "car.db";
    public static final int DATABASE_VERSION = 1;

    public static final String CAR_TB_NAME = "car";
    public static final String CAR_CLN_ID = "id";
    public static final String CAR_CLN_MODEL = "model";
    public static final String CAR_CLN_COLOR = "color";
    public static final String CAR_CLN_DESCRIPTION = "description";
    public static final String CAR_CLN_IMAGE = "image";
    public static final String CAR_CLN_DPL = "distancePerLetter";


    public CarDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
