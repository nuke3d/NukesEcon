package com.nuke3dtv.nukesecon.capabilities;

/*
    This interface describes a method of saving value to an object. This grew from being a way to add/change
    the value of in game coins but now will be able to be used on almost everything. (Want to trade a stack of thingamajigs
    for a stack of thingamabobs? This will allow the system to see the relative difference in value and make suggestions
    or NPC trading possible.)
*/

public interface INukeValue {
    int getValue();

    void setValue(int inValue);
}
