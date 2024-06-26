package az.edu.bhos.l14todoapp.flows

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import az.edu.bhos.l14todoapp.R
import az.edu.bhos.l14todoapp.entities.TodoBundle
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    private lateinit var searchBtn: View
    private lateinit var emptyView: View
    private lateinit var addNewBtn: View
    private lateinit var contentView: View
    private lateinit var todoList: RecyclerView

    private val adapter: TodoBundleAdapter by lazy { TodoBundleAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupUI()

        todoList.adapter = adapter

        viewModel.todoBundles.observe(this) { todoBundles ->
            if (todoBundles.isEmpty()) {
                emptyView.isVisible = true
                contentView.isVisible = false
            } else {
                emptyView.isVisible = false
                contentView.isVisible = true
                adapter.setData(todoBundles)
            }
        }
    }

    private fun setupUI() {
        searchBtn = findViewById(R.id.searchBtn)
        emptyView = findViewById(R.id.emptyView)
        addNewBtn = findViewById(R.id.addNewBtn)
        contentView = findViewById(R.id.contentView)
        todoList = findViewById(R.id.todoList)
    }
}

class TodoBundleAdapter : RecyclerView.Adapter<TodoBundleAdapter.TodoBundleViewHolder>() {

    private var todoBundleList: List<TodoBundle> = emptyList()

    class TodoBundleViewHolder(
        private val view: View
    ) : RecyclerView.ViewHolder(view) {
        private val title: TextView
        private val todoHolder: LinearLayout

        init {
            title = view.findViewById(R.id.title)
            todoHolder = view.findViewById(R.id.todoHolder)
        }

        fun setup(data: TodoBundle) {
            title.text = data.weekday

            todoHolder.removeAllViews()

            data.todos.forEach { todo ->
                val todoView: View =
                    LayoutInflater.from(view.context)
                        .inflate(R.layout.list_item_todo_entry, todoHolder, false)

                todoHolder.addView(todoView)

                val title: TextView = todoView.findViewById(R.id.title)
                title.text = todo.title

                if (todo.completed) {
                    title.paintFlags = title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    title.paintFlags = title.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }

                todoView.findViewById<ImageView>(R.id.checkIc)
                    .setImageResource(if (todo.completed) R.drawable.check_on else R.drawable.check_off)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoBundleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_view_todo_bundle, parent, false)
        return TodoBundleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todoBundleList.size
    }

    override fun onBindViewHolder(holder: TodoBundleViewHolder, position: Int) {
        holder.setup(todoBundleList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(bundles: List<TodoBundle>) {
        this.todoBundleList = bundles
        notifyDataSetChanged()
    }
}