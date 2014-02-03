package com.essentiallocalization.util.io;

/**
 * Created by Jake on 1/28/14.
 */
public class BasicConnection implements Connection {
    private int mState = STATE_NONE;
    private Listener mListener;

    public final void setConnectionListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public final synchronized int getState() {
        return mState;
    }

    @Override
    public final synchronized void setState(int state) {
        if (state == mState) return;
        int old = mState;
        mState = state;
        if (mListener != null) mListener.onStateChange(old, mState);
    }

    @Override
    public final synchronized boolean isConnecting() {
        return mState == STATE_CONNECTING;
    }

    @Override
    public final synchronized boolean isConnected() {
        return mState == STATE_CONNECTED;
    }

    @Override
    public final synchronized boolean isDisconnected() {
        return mState == STATE_DISCONNECTED;
    }
}