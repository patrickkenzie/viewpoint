package com.patrickkenzie.viewpoint

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.*

val LOG_TAG = "Viewpoint"

class ConnectionClient(val context: Context) :
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    val CONNECTION_STRATEGY = Strategy.P2P_STAR;

    val client: GoogleApiClient = GoogleApiClient.Builder(context)
            .addApi(Nearby.CONNECTIONS_API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()

    var endpointName = ""
    var endpointId = ""

    val payloadCallback = object : PayloadCallback() {
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionSuspended(id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionFailed(result: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun connect() = client.connect()
    fun disconnect() = client.disconnect()

    fun handleMessage(message: Any) {

    }

    fun startHosting() {

    }

    fun startLooking(serviceId: String) {
        // Spinner
        val discovery = DiscoveryOptions(CONNECTION_STRATEGY)
        Nearby.Connections.startDiscovery(client, serviceId, endpointFound, discovery)
                .setResultCallback { result ->
                    Log.d(LOG_TAG, "StartLooking: " + result.statusMessage)
                }
    }
}
