package com.nbehary.heartbeat.ui;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nbehary.heartbeat.R;
import com.nbehary.heartbeat.data.TodoItem;
import com.nbehary.heartbeat.model.TodoViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    LiveData<List<TodoItem>> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        RecyclerView recyclerView = findViewById(R.id.mr_list);
        TodoViewModel viewModel = ViewModelProviders.of(this).get(TodoViewModel.class);
        listItems = viewModel.getAllTodos();
        final TodoItemRecyclerViewAdapter adapter = new TodoItemRecyclerViewAdapter(this,listItems.getValue());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        viewModel.getAllTodos().observe(this, new Observer<List<TodoItem>>() {
            @Override
            public void onChanged(List<TodoItem> todoItems) {
                Log.e("MainActivity","onChanged called");
                adapter.setValues(todoItems);
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchhelper.attachToRecyclerView(recyclerView);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                DialogFragment newFragment = EditDialogFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putLong("ID",-1);
                bundle.putBoolean("NEW", true);
                newFragment.setArguments(bundle);
                newFragment.show(ft,"edit_dialog");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
