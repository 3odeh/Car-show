package sudy.studyexample.finalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class ViewCarActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQ_CODE = 1;
    public static final int ADD_CAR_RESULT_CODE = 2;
    public static final int EDIT_CAR_RESULT_CODE = 3;
    private ImageView imageView;
    private TextInputEditText color_et;
    private TextInputEditText dpl_et;
    private TextInputEditText description_et;
    private TextInputEditText model_et;

    private int carId = -1;

    private Uri imageUri;

    private MenuItem save;
    private MenuItem edit;
    private MenuItem delete;


    private DatabaseAccess databaseAccess;

    private Car car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_car);

        imageView = findViewById(R.id.details_iv);
        model_et = findViewById(R.id.ed_details_model);
        color_et = findViewById(R.id.ed_details_color);
        description_et = findViewById(R.id.ed_details_des);
        dpl_et = findViewById(R.id.ed_details_dpl);



        databaseAccess = DatabaseAccess.getInstance(this);

        Intent intent = getIntent();
        carId = intent.getIntExtra(MainActivity.CAR_KEY, -1);

        if (carId == -1) {
            enabledFields(true);
            clearFields();
        } else {
            enabledFields(false);
            databaseAccess.open();
            Car car = databaseAccess.getIdCar(carId);
            this.car = car;

            databaseAccess.close();

            if (car != null) {
                fillCarToFields(car);
            }

        }
    }

    private void fillCarToFields(Car car) {
        try {
            if (car.getImage() != null )
                imageView.setImageURI(car.getImage());
            else
                imageView.setImageResource(R.drawable.ic_race_car_svgrepo_com);
        } catch (Exception e){
            System.out.println(car.getImage());
            imageView.setImageResource(R.drawable.ic_race_car_svgrepo_com);
        }


        model_et.setText(car.getModel());
        color_et.setText(car.getColor());
        description_et.setText(car.getDescription());
        dpl_et.setText(car.getDpl() + "");
    }

    private void enabledFields(boolean b) {
        imageView.setEnabled(b);
        model_et.setEnabled(b);
        color_et.setEnabled(b);
        dpl_et.setEnabled(b);
        description_et.setEnabled(b);
    }

    private void clearFields() {
        imageView.setImageResource(R.drawable.ic_race_car_svgrepo_com);
        model_et.setText("");
        color_et.setText("");
        dpl_et.setText("");
        description_et.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        save = menu.findItem(R.id.detail_menu_save);
        edit = menu.findItem(R.id.detail_menu_edit);
        delete = menu.findItem(R.id.detail_menu_delete);
        if (carId == -1) {
            save.setVisible(true);
            edit.setVisible(false);
            delete.setVisible(false);
        } else {
            save.setVisible(false);
            edit.setVisible(true);
            delete.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        String model, color, description, image;
        double dpl;
        switch (item.getItemId()) {
            case R.id.detail_menu_save:

                model = model_et.getText().toString();
                color = color_et.getText().toString();
                description = description_et.getText().toString();
                if (imageUri != null)
                    image = imageUri.toString();
                else
                    image = "";
                try {
                    dpl = Double.parseDouble(dpl_et.getText().toString());
                } catch (Exception e) {
                    dpl = 0;
                }

                Car car1 = new Car(carId, model, color, description, Uri.parse(image), dpl);
                Car car2 = new Car(model, color, description, Uri.parse(image), dpl);

                databaseAccess.open();
                boolean b;
                if (carId == -1) {
                    b = databaseAccess.insertCar(car2);
                    if (b) {
                        Toast.makeText(this, "Insert new car successfully", Toast.LENGTH_SHORT).show();
                        setResult(ADD_CAR_RESULT_CODE);
                    }
                } else {
                    b = databaseAccess.updateCar(car1);
                    if (b) {
                        Toast.makeText(this, "update car successfully", Toast.LENGTH_SHORT).show();
                        setResult(EDIT_CAR_RESULT_CODE);
                    }
                }
                databaseAccess.close();
                finish();
                return true;
            case R.id.detail_menu_edit:
                enabledFields(true);
                save.setVisible(true);
                edit.setVisible(false);
                delete.setVisible(false);

                return true;
            case R.id.detail_menu_delete:
                databaseAccess.open();
                if (carId != -1)
                    if (databaseAccess.deleteCar(carId)) {
                        Toast.makeText(this, "Delete car ", Toast.LENGTH_SHORT).show();
                        setResult(EDIT_CAR_RESULT_CODE);
                        finish();
                    }
                databaseAccess.close();
                return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQ_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                imageUri = data.getData();
                imageView.setImageURI(imageUri);
                if(car != null)
                    car.setImage(imageUri);

            }
        } else {
            imageView.setImageResource(R.drawable.ic_race_car_svgrepo_com);
        }

    }

    public void imageViewOnClick(View view) {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICK_IMAGE_REQ_CODE);
    }
}