package com.github.baldogre.test.common

interface OnItemClick<T> {
    fun onClick(t: T, position: Int)
}
