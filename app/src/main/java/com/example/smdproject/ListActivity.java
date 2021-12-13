package com.example.smdproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    public ArrayList<Room> rooms;
    RecycleAdapter2 adapter;
    private DatabaseReference dataRef;
    private String Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Rooms");
        setContentView(R.layout.listactivity1);
        dataRef = FirebaseDatabase.getInstance().getReference().child("Rooms");
        Intent a = getIntent();
        Name = a.getStringExtra("Name");

        if(rooms == null) {
            rooms = new ArrayList<Room>();
        }

        dataRetrieve();
    }


    public void BuildRecycleView(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView2);
        adapter = new RecycleAdapter2(rooms);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecycleAdapter2.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String check1 = rooms.get(position).Hostel_ID;
                String check2 = rooms.get(position).ID;
                Intent a = new Intent(ListActivity.this, RoomInfo.class);
                a.putExtra("room",rooms.get(position));
                a.putExtra("Name",Name);
                startActivity(a);
                Toast.makeText(ListActivity.this, check1 + "("+ check2 +")" + "is clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void dataRetrieve(){
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(ListActivity.this, "Data Retrievel Started", Toast.LENGTH_SHORT).show();
                if(rooms!=null){
                    rooms.clear();
                }
                for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                    Room room = new Room();
                    if(postSnapShot.child("Status").getValue(Boolean.class)==false) {
                        room.AC = postSnapShot.child("AC").getValue(Boolean.class);
                        room.Status = postSnapShot.child("Status").getValue(Boolean.class);
                        room.WiFi = postSnapShot.child("WiFi").getValue(Boolean.class);
                        room.Price = postSnapShot.child("Price").getValue(String.class);
                        room.ID = postSnapShot.child("ID").getValue(String.class);
                        room.imageUrl = postSnapShot.child("imageUrl").getValue(String.class);
                        room.Location = postSnapShot.child("Location").getValue(String.class);
                        room.Hostel_ID = postSnapShot.child("Hostel_ID").getValue(String.class);
                        room.Ratings = postSnapShot.child("Ratings").getValue(String.class);
                        room.Dimensions = postSnapShot.child("Dimensions").getValue(String.class);
                        rooms.add(room);
                    }
                }
                BuildRecycleView();
                Toast.makeText(ListActivity.this, "Data Retrievel Ended", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ListActivity.this, "Data Retrievel Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchRoom = menu.findItem(R.id.searchicon);
        if(searchRoom != null){
            searchRoom.setOnActionExpandListener(new MenuItem.OnActionExpandListener(){
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    refreshSearch("");
                    return true;
                }
            });
        }
        SearchView search = (SearchView) searchRoom.getActionView();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    private void refreshSearch(String s) {
        adapter.getFilter().filter(s);
    }
}
