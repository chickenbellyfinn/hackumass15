package cleaner.board.white.halloweenapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cleaner.board.white.halloweenapp.model.Candy;
import cleaner.board.white.halloweenapp.R;

/**
 * Created by Akshay on 10/24/2015.
 */
public class FoodAdapter extends ArrayAdapter<Candy> {

    class Holder {
        @Bind(R.id.address) TextView name;
        @Bind(R.id.cals) TextView calories;
        public Holder(View view){
            ButterKnife.bind(this, view);
        }
    }

    public FoodAdapter(Context context) {
        super(context, R.layout.item_food_result);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup root){
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_food_result, null);
            view.setTag(new Holder(view));
        }

        Holder holder = (Holder)view.getTag();
        Candy item = getItem(position);

        holder.name.setText(item.name);
        holder.calories.setText(item.calories + " Calories");


        return view;
    }
}
