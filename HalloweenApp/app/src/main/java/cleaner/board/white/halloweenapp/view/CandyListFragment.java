package cleaner.board.white.halloweenapp.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cleaner.board.white.halloweenapp.R;
import cleaner.board.white.halloweenapp.model.Candy;

public class CandyListFragment extends Fragment  {

    @Bind(R.id.list) ListView list;
    @Bind(R.id.what) TextView nocandy;
    @Bind(R.id.total) TextView calorieTotal;

    private CandyAdapter adapter;

    public CandyListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new CandyAdapter(getActivity());


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_candy_list, container, false);
        ButterKnife.bind(this, view);

        list.setAdapter(adapter);

        update();

        return view;
    }

    public void update(){

        List<Candy> allCandy = Candy.all();

        adapter.clear();
        adapter.addAll(allCandy);
        adapter.notifyDataSetChanged();

        int total = 0;
        for(Candy c:allCandy){
            total += c.calories*c.count;
        }

        calorieTotal.setText(""+ total);

        if(!adapter.isEmpty()){
            nocandy.setVisibility(View.GONE);
        } else {
            nocandy.setVisibility(View.VISIBLE);
        }
    }

}
