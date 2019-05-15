package com.github.baldogre.test.module.main

import android.app.AlertDialog
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.github.baldogre.test.R
import com.github.baldogre.test.common.OnItemClick
import com.github.baldogre.test.model.Robot
import com.github.baldogre.test.model.RobotsGetResponse
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : MvpAppCompatActivity(), MainView, OnItemClick<Robot> {
    @InjectPresenter
    lateinit var mainPresenter: MainPresenter

    lateinit var dialog: AlertDialog
    lateinit var robot: Robot

    private val robotsAdapter = RobotsAdapter(onItemClick = this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler.adapter = robotsAdapter

        val linearLayoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(this, linearLayoutManager.getOrientation())

        recycler.layoutManager = linearLayoutManager
        recycler.addItemDecoration(dividerItemDecoration)
        mainPresenter.getRobots()
    }

    override fun onResponse(response: RobotsGetResponse) {
        Log.d("MainActivity", response.toString())
        runOnUiThread {
            response.data?.let {
                robotsAdapter.robots.clear()
                robotsAdapter.robots.addAll(it)
            }
            robotsAdapter.notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.create_action -> createRobot()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createRobot() {
        val view = layoutInflater.inflate(R.layout.dialog_update, null, false)
        val name = view.findViewById<EditText>(R.id.name)
        val type = view.findViewById<EditText>(R.id.type)
        val year = view.findViewById<EditText>(R.id.year)

        AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("OK") { dialog, which ->
                    robot = Robot(0)
                    robot.name = name.text.toString()
                    robot.type = type.text.toString()
                    robot.year = year.text.toString()
                    mainPresenter.createRobot(robot)
                }
                .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
                .show()
    }

    override fun onClick(t: Robot, position: Int) {
        robot = t
        if (!::dialog.isInitialized) {
            initDialog()
        }

        dialog.show()

    }

    private fun initDialog() {
        val view = layoutInflater.inflate(R.layout.dialog, null, false)
        dialog = AlertDialog.Builder(this)
                .setView(view)
                .create()
        view.findViewById<TextView>(R.id.update).setOnClickListener {
            mainPresenter.onUpdateRobotClick(robot)
            dialog.dismiss()
        }
        view.findViewById<TextView>(R.id.remove).setOnClickListener {
            mainPresenter.removeRobot(robot)
            dialog.dismiss()
        }
    }

    override fun onFail() {
        runOnUiThread { Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show() }
    }

    override fun onDeleted() {
        runOnUiThread {
            robotsAdapter.robots.remove(robot)
            robotsAdapter.notifyDataSetChanged()
        }
    }

    override fun openUpdateDialog(robot: Robot) {
        val view = layoutInflater.inflate(R.layout.dialog_update, null, false)
        val name = view.findViewById<EditText>(R.id.name)
        val type = view.findViewById<EditText>(R.id.type)
        val year = view.findViewById<EditText>(R.id.year)
        name.setText(robot.name)
        type.setText(robot.type)
        year.setText(robot.year)

        AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("OK") { dialog, which ->
                    robot.name = name.text.toString()
                    robot.type = type.text.toString()
                    robot.year = year.text.toString()
                    mainPresenter.updateRobot(robot)
                }
                .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
                .show()
    }

    override fun onUpdated(data: Robot) {
        runOnUiThread { robotsAdapter.notifyDataSetChanged() }
    }

    override fun onCreated(robot: Robot) {
        runOnUiThread {
            robotsAdapter.robots.add(robot)
            robotsAdapter.notifyDataSetChanged()
        }
    }
}
