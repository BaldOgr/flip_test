package com.github.baldogre.test.module.main

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.github.baldogre.test.Const
import com.github.baldogre.test.common.LoggingInterceptor
import com.github.baldogre.test.model.Robot
import com.github.baldogre.test.model.RobotsGetResponse
import com.github.baldogre.test.model.RobotsUpdateResponse
import com.google.gson.Gson
import okhttp3.*

import java.io.IOException

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {
    private val okHttpClient = OkHttpClient.Builder().addInterceptor(LoggingInterceptor()).build()

    fun getRobots() {
        val request = Request.Builder().url(Const.URL).get().build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                viewState.onFail()
            }

            override fun onResponse(call: Call, response: Response) {
                val string = response.body()?.string()
                val robotsGetResponse = Gson().fromJson<RobotsGetResponse>(string, RobotsGetResponse::class.java)
                if (robotsGetResponse.success) {
                    viewState.onResponse(robotsGetResponse)
                } else {
                    viewState.onFail()
                }
            }

        })
    }

    fun updateRobot(robot: Robot) {
        val request = Request.Builder().url(Const.URL + "/" + robot.id)
                .put(RequestBody.Companion.create(MediaType.parse("application/json"), Gson().toJson(robot)))
                .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body()?.string()
                val robot = Gson().fromJson<RobotsUpdateResponse>(responseString, RobotsUpdateResponse::class.java)
                if (robot.success) {
                    viewState.onUpdated(robot.data)
                } else {
                    viewState.onFail()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                viewState.onFail()
            }

        })
    }

    fun createRobot(robot: Robot) {
        val request = Request.Builder().url(Const.URL)
                .post(RequestBody.Companion.create(MediaType.parse("application/json"), Gson().toJson(robot)))
                .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseString = response.body()?.string()
                val robot = Gson().fromJson<RobotsUpdateResponse>(responseString, RobotsUpdateResponse::class.java)
                if (robot.success) {
                    viewState.onCreated(robot.data)
                } else {
                    viewState.onFail()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                viewState.onFail()
            }

        })
    }

    fun removeRobot(robot: Robot) {
        val request = Request.Builder().url(Const.URL + "/" + robot.id).delete().build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                viewState.onDeleted()
            }

            override fun onFailure(call: Call, e: IOException) {
                viewState.onFail()
            }

        })
    }

    fun onUpdateRobotClick(robot: Robot) {
        viewState.openUpdateDialog(robot)
    }
}
