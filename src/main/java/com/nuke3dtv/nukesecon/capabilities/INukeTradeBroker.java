package com.nuke3dtv.nukesecon.capabilities;


/*
    This interface describes how to broker a trade between two objects.
 */

import com.nuke3dtv.nukesecon.capabilities.nukelock.INukeLock;

import javax.annotation.Nullable;

public interface INukeTradeBroker {

    boolean isTradeAvailable(@Nullable INukeLock inLock);



}
