package sudy.studyexample.finalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_CAR_REQ_CODE = 1;
    private static final int EDIT_CAR_REQ_CODE = 2;
    public static final String CAR_KEY = "car_key";
    private static final int PERMISSION_REQ_CODE = 5;

    private RecyclerView rv;
    private FloatingActionButton fab;
    private DatabaseAccess databaseAccess;
    private CarRVAdapter carRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQ_CODE);
        }


        rv = findViewById(R.id.main_rv);
        fab = findViewById(R.id.main_fab);

        databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        ArrayList<Car> cars = databaseAccess.getAllCars();
        databaseAccess.close();

        carRVAdapter = new CarRVAdapter(cars);
        rv.setAdapter(carRVAdapter);
        RecyclerView.LayoutManager manager = new GridLayoutManager(this, 2);
        rv.setLayoutManager(manager);
        rv.setHasFixedSize(true);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {



        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem item = menu.findItem(R.id.main_search);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                databaseAccess.open();
                ArrayList<Car> cars = databaseAccess.getCars(s);
                databaseAccess.close();
                carRVAdapter.setCars(cars);
                carRVAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                databaseAccess.open();
                ArrayList<Car> cars = databaseAccess.getCars(s);
                databaseAccess.close();
                carRVAdapter.setCars(cars);
                carRVAdapter.notifyDataSetChanged();
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                databaseAccess.open();
                ArrayList<Car> cars = databaseAccess.getAllCars();
                databaseAccess.close();
                carRVAdapter.setCars(cars);
                carRVAdapter.notifyDataSetChanged();
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void fabOnClick(View view) {
        Intent intent = new Intent(this, ViewCarActivity.class);
        startActivityForResult(intent, ADD_CAR_REQ_CODE);
    }

    public void cardViewOnClick(View view) {
        TextView editText = view.findViewById(R.id.custom_car_tv_model);
        Intent intent = new Intent(this, ViewCarActivity.class);
        intent.putExtra(CAR_KEY, (int) editText.getTag());
        startActivityForResult(intent, EDIT_CAR_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        databaseAccess.open();
        ArrayList<Car> cars = databaseAccess.getAllCars();
        databaseAccess.close();
        carRVAdapter.setCars(cars);
        carRVAdapter.notifyDataSetChanged();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQ_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
                }
        }
    }
}