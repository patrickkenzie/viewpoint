package com.patrickkenzie.viewpoint

import android.content.Context
import android.util.Log
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.*


val LOG_TAG = "ViewpointLog"

class ConnectionClient(val context: Context, val observer: ConnectionObserver, val isHosting: Boolean) {
    interface ConnectionObserver {
        fun onClientInit()
        fun onConnectionCreated()
        fun onConnectionFailed(errorCode: Int)
    }

    val CONNECTION_STRATEGY = Strategy.P2P_STAR
    val SERVICE_ID = context.packageName
    val client = Nearby.getConnectionsClient(context)

    var endpointName = ""
    var endpointId = ""
    val hostPayloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(id: String, payload: Payload) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onPayloadTransferUpdate(id: String, update: PayloadTransferUpdate) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }

    val cameraPayloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(id: String, payload: Payload) {
            if (payload.type == Payload.Type.BYTES) {
                val bytes = payload.asBytes()
                val message = bytes ?: ""
                handleMessage(message)
            }
        }

        override fun onPayloadTransferUpdate(id: String, update: PayloadTransferUpdate) {
            TODO("not implemented")
        }
    }

    val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionResult(id: String, resolution: ConnectionResolution) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onDisconnected(id: String) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onConnectionInitiated(id: String, info: ConnectionInfo) {
            val payloadCallback = if (isHosting) hostPayloadCallback else cameraPayloadCallback
            client.acceptConnection(id, payloadCallback)

            observer.onConnectionCreated()
        }
    }

    val endpointFound: EndpointDiscoveryCallback = object : EndpointDiscoveryCallback() {
        override fun onEndpointFound(id: String, endpoint: DiscoveredEndpointInfo) {
            Log.d(LOG_TAG, "endpoint found: ${endpoint.endpointName} ($id)")

            endpointName = endpoint.endpointName
            val name = "Test"

            client.requestConnection(name, id, connectionLifecycleCallback)
        }

        override fun onEndpointLost(id: String) {
            Log.d(LOG_TAG, "endpoint lost: $id")
            if (id == endpointId) {
                // TODO(Disconnect)
            }
        }
    }

    fun start() {
        Log.d(LOG_TAG, "start")
        if (this.isHosting) this.startAdvertising() else this.startDiscovery()

        observer.onClientInit()
    }

    fun stop() {
        Log.d(LOG_TAG, "stop")
        if (this.isHosting) this.stopAdvertising() else this.stopDiscovery()
    }

    fun handleMessage(message: Any) {

    }

    fun startAdvertising() {
        val opts = AdvertisingOptions.Builder().setStrategy(CONNECTION_STRATEGY).build()
        client.startAdvertising("Viewpoint Host", SERVICE_ID, connectionLifecycleCallback, opts)
                .addOnSuccessListener {
                    Log.d(LOG_TAG, "startAdvertising success")
                }
                .addOnFailureListener {
                    Log.e(LOG_TAG, "startAdvertising failed", it)
                }
    }

    fun stopAdvertising() {
        client.stopAdvertising()
    }

    fun startDiscovery() {
        // Spinner
        val opts = DiscoveryOptions.Builder().setStrategy(CONNECTION_STRATEGY).build()
        client.startDiscovery(SERVICE_ID, endpointFound, opts)
                .addOnSuccessListener {
                    Log.d(LOG_TAG, "startDiscovery success")
                }
                .addOnFailureListener {
                    Log.e(LOG_TAG, "startDiscovery failed", it)
                }
    }

    fun stopDiscovery() {
        client.stopDiscovery()
    }
}
