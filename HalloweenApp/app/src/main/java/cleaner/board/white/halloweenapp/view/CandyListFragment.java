package cleaner.board.white.halloweenapp.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cleaner.board.white.halloweenapp.R;
import cleaner.board.white.halloweenapp.model.Candy;

public class CandyListFragment extends Fragment  {

    @Bind(R.id.list) ListView list;
    @Bind(R.id.what) TextView nocandy;

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

        adapter.clear();
        adapter.addAll(Candy.all());
        adapter.notifyDataSetChanged();

        if(!adapter.isEmpty()){
            nocandy.setVisibility(View.GONE);
        } else {
            nocandy.setVisibility(View.VISIBLE);
        }
    }

}
