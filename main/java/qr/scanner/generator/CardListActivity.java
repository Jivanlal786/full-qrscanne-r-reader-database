package qr.scanner.generator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;

import java.util.List;

import qr.scanner.generator.adapters.AadhaarCard;
import qr.scanner.generator.room.User;
import qr.scanner.generator.room.UserDatabase;

public class CardListActivity extends AppCompatActivity {

    RecyclerView recyclerCard;
    UserDatabase userDatabase;
    List<User> userlist;
    AadhaarCard aadhaarCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);

        recyclerCard = findViewById(R.id.recycler_card);

        userDatabase = Room.databaseBuilder(CardListActivity.this, UserDatabase.class, "mylikedb").allowMainThreadQueries().build();
        userlist = userDatabase.userDao().getFavoriteData();

        aadhaarCard= new AadhaarCard(CardListActivity.this,userlist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        recyclerCard.setLayoutManager(layoutManager);
        recyclerCard.setAdapter(aadhaarCard);
    }
}