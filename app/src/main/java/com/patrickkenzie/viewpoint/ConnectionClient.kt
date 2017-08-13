package com.patrickkenzie.viewpoint

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.*

val LOG_TAG = "ViewpointLog"

class ConnectionClient(val context: Context, val isHosting: Boolean) :
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    val CONNECTION_STRATEGY = Strategy.P2P_STAR;
    val packageName = context.packageName

    val client: GoogleApiClient = GoogleApiClient.Builder(context)
            .addApi(Nearby.CONNECTIONS_API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()

    var endpointName = ""
    var endpointId = ""

    val hostPayloadCallback = object:PayloadCallback() {
        override fun onPayloadReceived(p0: String?, p1: Payload?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onPayloadTransferUpdate(p0: String?, p1: PayloadTransferUpdate?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }

    val cameraPayloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(id: String?, payload: Payload?) {
            if (payload?.type == Payload.Type.BYTES) {
                val bytes = payload.asBytes()
                val message = bytes ?: ""
                handleMessage(message)
            }
        }

        override fun onPayloadTransferUpdate(id: String?, update: PayloadTransferUpdate?) {
            TODO("not implemented")
        }
    }

    val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionResult(id: String?, resolution: ConnectionResolution?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onDisconnected(id: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onConnectionInitiated(id: String?, info: ConnectionInfo?) {
            val payloadCallback = if (isHosting) hostPayloadCallback else cameraPayloadCallback
            Nearby.Connections.acceptConnection(client, id, payloadCallback)
        }

    }

    val endpointFound: EndpointDiscoveryCallback = object : EndpointDiscoveryCallback() {
        override fun onEndpointFound(id: String?, endpoint: DiscoveredEndpointInfo?) {
            endpointName = endpoint?.endpointName ?: ""
            val name = "Test"

            Nearby.Connections.requestConnection(client, name, id, connectionLifecycleCallback)
        }

        override fun onEndpointLost(id: String?) {
            if (id == endpointId) {
                // TODO(Disconnect)
            }
        }
    }

    override fun onConnected(data: Bundle?) {
        Log.d(LOG_TAG, "onConnected")
        if (this.isHosting) this.startHosting() else this.startLooking()
    }

    override fun onConnectionSuspended(id: Int) {
        Log.d(LOG_TAG, "onConnectionSuspended")
    }

    override fun onConnectionFailed(result: ConnectionResult) {
        Log.d(LOG_TAG, "onConnectionFailed")
    }

    fun start() = client.connect()
    fun stop() = client.disconnect()

    fun handleMessage(message: Any) {

    }

    fun startHosting() {
        val advertise = AdvertisingOptions(CONNECTION_STRATEGY)
        Nearby.Connections.startAdvertising(client, packageName, "TestDevice", connectionLifecycleCallback, advertise)
                .setResultCallback { result ->
                    Log.d(LOG_TAG, "startHosting: " + result.status)
                }
    }

    fun startLooking() {
        // Spinner
        val discovery = DiscoveryOptions(CONNECTION_STRATEGY)
        Nearby.Connections.startDiscovery(client, packageName, endpointFound, discovery)
                .setResultCallback { result ->
                    Log.d(LOG_TAG, "startLooking: " + result.statusMessage)
                }
    }
}
