package com.github.baldogre.test.module.main

import com.arellomobile.mvp.MvpView
import com.github.baldogre.test.model.Robot
import com.github.baldogre.test.model.RobotsGetResponse

interface MainView : MvpView {

    fun onFail()
    fun onResponse(response: RobotsGetResponse)
    fun onDeleted()
    fun onUpdated(data: Robot)
    fun onCreated(robot: Robot)
    fun openUpdateDialog(robot: Robot)
}
