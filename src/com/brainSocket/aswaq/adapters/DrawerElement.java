package com.brainSocket.aswaq.adapters;

import com.brainSocket.aswaq.enums.DrawerItemType;

public class DrawerElement {
	int stringId;
	int iconId;
	int activeIconId;
	DrawerItemType itemType;

	public DrawerElement(int str, int ico, int activeIcon, DrawerItemType type) {
		stringId = str;
		iconId = ico;
		activeIconId = activeIcon;
		itemType = type;
	}
}
