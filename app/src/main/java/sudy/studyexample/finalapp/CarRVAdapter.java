package sudy.studyexample.finalapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CarRVAdapter extends RecyclerView.Adapter<CarRVAdapter.CarViewHolder> {

    private ArrayList<Car> cars;

    public CarRVAdapter(ArrayList<Car> cars) {
        this.cars = cars;
    }

    @Override
    public CarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_car_layout, null, false);
        return new CarViewHolder(view);
    }

    public void setCars(ArrayList<Car> cars) {
        this.cars = cars;
    }

    @Override
    public void onBindViewHolder(CarViewHolder holder, int position) {
        Car car = cars.get(position);
        try {
            if (car.getImage() != null) {
                holder.image.setImageURI(car.getImage());
            } else
                holder.image.setImageResource(R.drawable.ic_race_car_svgrepo_com);
        } catch (Exception e) {
            holder.image.setImageResource(R.drawable.ic_race_car_svgrepo_com);
        }

        holder.model_tv.setText(car.getModel());
        holder.dpl_tv.setText("" + car.getDpl());
        holder.color_tv.setText(car.getColor());


        holder.model_tv.setTag(cars.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    class CarViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView model_tv, color_tv, dpl_tv;


        public CarViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.custom_car_iv);
            model_tv = itemView.findViewById(R.id.custom_car_tv_model);
            color_tv = itemView.findViewById(R.id.custom_car_tv_color);
            dpl_tv = itemView.findViewById(R.id.custom_car_tv_dpl);
        }
    }
}
