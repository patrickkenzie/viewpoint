package com.patrickkenzie.viewpoint.connections

data class ActiveHost(val id: String, val name: String) {
    override fun toString(): String = name
}
