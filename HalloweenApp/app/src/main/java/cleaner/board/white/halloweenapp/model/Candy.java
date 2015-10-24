package cleaner.board.white.halloweenapp.model;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Akshay on 10/24/2015.
 */
public class Candy extends SugarRecord<Candy> {

    public static void add(Candy candy){
        Iterator<Candy> cit = SugarRecord.findAll(Candy.class);
        Candy c;

        while(cit.hasNext()){
            c = cit.next();
            if(c.name.equals(candy.name)){
                c.count++;
                c.save();
                return;
            }
        }

        c = new Candy(candy.name, candy.calories, 1);
        c.keyword = candy.keyword;
        c.save();
    }

    public static List<Candy> all(){
        List<Candy> clist = new ArrayList<Candy>();

        Iterator<Candy> cit = SugarRecord.findAll(Candy.class);
        while(cit.hasNext()){
            clist.add(cit.next());
        }
        Collections.sort(clist, new Comparator<Candy>() {
            @Override
            public int compare(Candy lhs, Candy rhs) {
                return rhs.count - lhs.count;
            }
        });
        return clist;
    }

    public String name;
    public String keyword;
    public int calories;
    public int count;

    public Candy(){}

    public Candy(String name, int calories, int count){
        this.name = name;
        this.calories = calories;
        this.count = count;
    }


}
