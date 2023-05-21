package sudy.studyexample.finalapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.widget.Toast;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

public class DatabaseAccess {
    private SQLiteDatabase database;
    private SQLiteAssetHelper openHelper;

    private static DatabaseAccess instance;

    private DatabaseAccess(Context context) {
        this.openHelper = new CarDatabase(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        if (instance == null)
            instance = new DatabaseAccess(context);
        return instance;
    }

    public void open() {
        this.database = this.openHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null)
            database.close();
    }

    public boolean insertCar(Car car) {

        ContentValues values = new ContentValues();
        values.put(CarDatabase.CAR_CLN_MODEL, car.getModel());
        values.put(CarDatabase.CAR_CLN_COLOR, car.getColor());
        values.put(CarDatabase.CAR_CLN_DESCRIPTION, car.getDescription());
        values.put(CarDatabase.CAR_CLN_IMAGE, car.getImage().toString());
        values.put(CarDatabase.CAR_CLN_DPL, car.getDpl());

        long result = database.insert(CarDatabase.CAR_TB_NAME, null, values);

        return result != -1;
    }

    public boolean updateCar(Car car) {

        ContentValues values = new ContentValues();
        values.put(CarDatabase.CAR_CLN_MODEL, car.getModel());
        values.put(CarDatabase.CAR_CLN_COLOR, car.getColor());
        values.put(CarDatabase.CAR_CLN_DESCRIPTION, car.getDescription());
        values.put(CarDatabase.CAR_CLN_IMAGE, car.getImage().toString());
        values.put(CarDatabase.CAR_CLN_DPL, car.getDpl());

        int result = database.update(CarDatabase.CAR_TB_NAME, values, "id=?", new String[]{(car.getId() + "")});

        return result > 0;
    }

    public long getCarsCount() {
        return DatabaseUtils.queryNumEntries(database, CarDatabase.CAR_TB_NAME);
    }

    public boolean deleteCar(int car) {

        int result = database.delete(CarDatabase.CAR_TB_NAME, "id=?", new String[]{car + ""});

        return result > 0;
    }

    public ArrayList<Car> getAllCars() {
        ArrayList<Car> cars = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + CarDatabase.CAR_TB_NAME, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt((cursor.getColumnIndex(CarDatabase.CAR_CLN_ID)));
                String model = cursor.getString((cursor.getColumnIndex(CarDatabase.CAR_CLN_MODEL)));
                String color = cursor.getString((cursor.getColumnIndex(CarDatabase.CAR_CLN_COLOR)));
                String description = cursor.getString((cursor.getColumnIndex(CarDatabase.CAR_CLN_DESCRIPTION)));
                String image = cursor.getString((cursor.getColumnIndex(CarDatabase.CAR_CLN_IMAGE)));
                double dpl = cursor.getDouble((cursor.getColumnIndex(CarDatabase.CAR_CLN_DPL)));

                Car car = new Car(id, model, color, description, Uri.parse(image), dpl);
                cars.add(car);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return cars;
    }

    public ArrayList<Car> getCars(String modelSearch) {
        ArrayList<Car> cars = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + CarDatabase.CAR_TB_NAME + " WHERE " + CarDatabase.CAR_CLN_MODEL + " LIKE ?", new String[]{modelSearch + "%"});


        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt((cursor.getColumnIndex(CarDatabase.CAR_CLN_ID)));
                String model = cursor.getString((cursor.getColumnIndex(CarDatabase.CAR_CLN_MODEL)));
                String color = cursor.getString((cursor.getColumnIndex(CarDatabase.CAR_CLN_COLOR)));
                String description = cursor.getString((cursor.getColumnIndex(CarDatabase.CAR_CLN_DESCRIPTION)));
                String image = cursor.getString((cursor.getColumnIndex(CarDatabase.CAR_CLN_IMAGE)));
                double dpl = cursor.getDouble((cursor.getColumnIndex(CarDatabase.CAR_CLN_DPL)));

                Car car = new Car(id, model, color, description, Uri.parse(image), dpl);
                cars.add(car);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return cars;
    }

    public Car getIdCar(int carId) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + CarDatabase.CAR_TB_NAME + " WHERE " + CarDatabase.CAR_CLN_ID + "=?", new String[]{carId + ""});


        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt((cursor.getColumnIndex(CarDatabase.CAR_CLN_ID)));
            String model = cursor.getString((cursor.getColumnIndex(CarDatabase.CAR_CLN_MODEL)));
            String color = cursor.getString((cursor.getColumnIndex(CarDatabase.CAR_CLN_COLOR)));
            String description = cursor.getString((cursor.getColumnIndex(CarDatabase.CAR_CLN_DESCRIPTION)));
            String image = cursor.getString((cursor.getColumnIndex(CarDatabase.CAR_CLN_IMAGE)));
            double dpl = cursor.getDouble((cursor.getColumnIndex(CarDatabase.CAR_CLN_DPL)));

            Car car = new Car(id, model, color, description, Uri.parse(image), dpl);
            cursor.close();
            return car;
        }
        return null;
    }
}
