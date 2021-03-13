package com.nuke3dtv.nukesecon.capabilities;


/*
    This interface describes how to broker a trade between two objects.
 */

import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public interface INukeTradeBroker {

    boolean isTradeAvailable(@Nullable INukeLock inLock);



}
