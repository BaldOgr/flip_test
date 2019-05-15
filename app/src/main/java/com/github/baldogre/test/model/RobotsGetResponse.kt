package com.github.baldogre.test.model

class RobotsGetResponse(val success: Boolean, val data: MutableList<Robot>? = null) {
    override fun toString(): String {
        return "RobotsResponse(success=$success, data=$data)"
    }
}
