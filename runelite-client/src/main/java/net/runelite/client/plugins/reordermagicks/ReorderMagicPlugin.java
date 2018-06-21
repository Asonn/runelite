/*
 * Copyright (c) 2018, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.reordermagicks;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.menus.MenuManager;
import net.runelite.client.menus.WidgetMenuOption;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static net.runelite.api.widgets.WidgetConfig.DRAG;
import static net.runelite.api.widgets.WidgetConfig.DRAG_ON;

@Slf4j
@PluginDescriptor(name = "Reorder Magics")
public class ReorderMagicPlugin extends Plugin
{

	static final String CONFIG_GROUP_KEY = "reordermagics";

	static final String CONFIG_UNLOCK_REORDERING_KEY = "unlockMagicReordering";

	static final String CONFIG_MAGIC_ORDER_KEY = "magicOrder";

	private static final int MAGIC_WIDTH = 24;

	private static final int MAGIC_HEIGHT = 24;

	private static final int MAGIC_X_OFFSET = 25;

	private static final int MAGIC_Y_OFFSET = 25;

	private static final int MAGIC_COLUMN_COUNT = 7;

	private static final List<WidgetInfo> MAGIC_WIDGET_INFO_LIST = ImmutableList.of(
            WidgetInfo.MAGIC_LUMBRIDGE_HOME_TELEPORT,
            WidgetInfo.MAGIC_WIND_STRIKE,
            WidgetInfo.MAGIC_CONFUSE,
            WidgetInfo.MAGIC_ENCHANT_CROSSBOW_BOLT,
            WidgetInfo.MAGIC_WATER_STRIKE,
            WidgetInfo.MAGIC_LVL1_ENCHANT,
            WidgetInfo.MAGIC_EARTH_STRIKE,
            WidgetInfo.MAGIC_WEAKEN,
            WidgetInfo.MAGIC_FIRE_STRIKE,
            WidgetInfo.MAGIC_BONES_TO_BANANAS,
            WidgetInfo.MAGIC_WIND_BOLT,
            WidgetInfo.MAGIC_CURSE,
            WidgetInfo.MAGIC_BIND,
            WidgetInfo.MAGIC_LOW_LEVEL_ALCHEMY,
            WidgetInfo.MAGIC_WATER_BOLT,
            WidgetInfo.MAGIC_VARROCK_TELEPORT,
            WidgetInfo.MAGIC_LVL2_ENCHANT,
            WidgetInfo.MAGIC_EARTH_BOLT,
            WidgetInfo.MAGIC_LUMBRIDGE_TELEPORT,
            WidgetInfo.MAGIC_TELEKINETIC_GRAB,
            WidgetInfo.MAGIC_FIRE_BOLT,
            WidgetInfo.MAGIC_FALADOR_TELEPORT,
            WidgetInfo.MAGIC_CRUMBLE_UNDEAD,
            WidgetInfo.MAGIC_TELEPORT_TO_HOUSE,
            WidgetInfo.MAGIC_WIND_BLAST,
            WidgetInfo.MAGIC_SUPERHEAT_ITEM,
            WidgetInfo.MAGIC_CAMELOT_TELEPORT,
            WidgetInfo.MAGIC_WATER_BLAST,
            WidgetInfo.MAGIC_LVL3_ENCHANT,
            WidgetInfo.MAGIC_IBAN_BLAST,
            WidgetInfo.MAGIC_SNARE,
            WidgetInfo.MAGIC_MAGIC_DART,
            WidgetInfo.MAGIC_ARDOUGNE_TELEPORT,
            WidgetInfo.MAGIC_EARTH_BLAST,
            WidgetInfo.MAGIC_HIGH_LEVEL_ALCHEMY,
            WidgetInfo.MAGIC_CHARGE_WATER_ORB,
            WidgetInfo.MAGIC_LVL4_ENCHANT,
            WidgetInfo.MAGIC_WATCHTOWER_TELEPORT,
            WidgetInfo.MAGIC_FIRE_BLAST,
            WidgetInfo.MAGIC_CHARGE_EARTH_ORB,
            WidgetInfo.MAGIC_BONES_TO_PEACHES,
            WidgetInfo.MAGIC_SARADOMIN_STRIKE,
            WidgetInfo.MAGIC_CLAWS_OF_GUTHIX,
            WidgetInfo.MAGIC_FLAMES_OF_ZAMORAK,
            WidgetInfo.MAGIC_TROLLHEIM_TELEPORT,
            WidgetInfo.MAGIC_WIND_WAVE,
            WidgetInfo.MAGIC_CHARGE_FIRE_ORB,
            WidgetInfo.MAGIC_TELEPORT_TO_APE_TOLL,
            WidgetInfo.MAGIC_WATER_WAVE,
            WidgetInfo.MAGIC_CHARGE_AIR_ORB,
            WidgetInfo.MAGIC_VULNERABILITY,
            WidgetInfo.MAGIC_LVL5_ENCHANT,
            WidgetInfo.MAGIC_TELEPORT_TO_KOUREND,
            WidgetInfo.MAGIC_EARTH_WAVE,
            WidgetInfo.MAGIC_ENFEEBLE,
            WidgetInfo.MAGIC_TELEOTHER_LUMBRIDGE,
            WidgetInfo.MAGIC_FIRE_WAVE,
            WidgetInfo.MAGIC_ENTANGLE,
            WidgetInfo.MAGIC_STUN,
            WidgetInfo.MAGIC_CHARGE,
            WidgetInfo.MAGIC_WIND_SURGE,
            WidgetInfo.MAGIC_TELEOTHER_FALADOR,
            WidgetInfo.MAGIC_WATER_SURGE,
            WidgetInfo.MAGIC_TELE_BLOCK,
            WidgetInfo.MAGIC_TELEPORT_TO_BOUNTY_TARGET,
            WidgetInfo.MAGIC_LVL6_ENCHANT,
            WidgetInfo.MAGIC_TELEOTHER_CAMELOT,
            WidgetInfo.MAGIC_EARTH_SURGE,
            WidgetInfo.MAGIC_LVL7_ENCHANT,
            WidgetInfo.MAGIC_FIRE_SURGE
	);

	private static final String LOCK = "Lock";

	private static final String UNLOCK = "Unlock";

	private static final String MENU_TARGET = "Reordering";

	private static final WidgetMenuOption FIXED_MAGIC_TAB_LOCK = new WidgetMenuOption(LOCK,
			MENU_TARGET, WidgetInfo.FIXED_VIEWPORT_MAGIC_TAB);

	private static final WidgetMenuOption FIXED_MAGIC_TAB_UNLOCK = new WidgetMenuOption(UNLOCK,
			MENU_TARGET, WidgetInfo.FIXED_VIEWPORT_MAGIC_TAB);

	private static final WidgetMenuOption RESIZABLE_MAGIC_TAB_LOCK = new WidgetMenuOption(LOCK,
			MENU_TARGET, WidgetInfo.RESIZABLE_VIEWPORT_MAGIC_TAB);

	private static final WidgetMenuOption RESIZABLE_MAGIC_TAB_UNLOCK = new WidgetMenuOption(UNLOCK,
			MENU_TARGET, WidgetInfo.RESIZABLE_VIEWPORT_MAGIC_TAB);

	@Inject
	private Client client;

	@Inject
	private ReorderMagicConfig config;

	@Inject
	private MenuManager menuManager;

	private Magic[] magicOrder;

	static String magicOrderToString(Magic[] MagicOrder)
	{
        return Arrays.stream(MagicOrder)
				.map(Magic::name)
				.collect(Collectors.joining(","));
	}

	private static Magic[] stringToMagicOrder(String string)
	{
		return Arrays.stream(string.split(","))
				.map(Magic::valueOf)
				.toArray(Magic[]::new);
	}

	private static int getMagicIndex(Widget widget)
	{
		int x = (widget.getOriginalX() / MAGIC_X_OFFSET);
		int y = (widget.getOriginalY() / MAGIC_Y_OFFSET);
		return x + y * MAGIC_COLUMN_COUNT;
	}

	private static void setWidgetPosition(Widget widget, int x, int y)
	{
		widget.setRelativeX(x);
		widget.setRelativeY(y);
		widget.setOriginalX(x);
		widget.setOriginalY(y);
	}

	@Provides
    ReorderMagicConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ReorderMagicConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
        refreshMagicTabOption();
        magicOrder = stringToMagicOrder(config.magicOrder());
		reorderMagics();
	}

	@Override
	protected void shutDown() throws Exception
	{
		clearMagicTabMenus();
		magicOrder = Magic.values();
		reorderMagics(false);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() == GameState.LOGGED_IN)
		{
			reorderMagics();
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals(CONFIG_GROUP_KEY))
		{
			if (event.getKey().equals(CONFIG_MAGIC_ORDER_KEY))
			{
				magicOrder = stringToMagicOrder(config.magicOrder());
			}
			else if (event.getKey().equals(CONFIG_UNLOCK_REORDERING_KEY))
			{
				refreshMagicTabOption();
			}
			reorderMagics();
		}
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded event)
	{
		if (event.getGroupId() == WidgetID.MAGIC_GROUP_ID)
		{
			reorderMagics();
		}
	}

	@Subscribe
	public void onDraggingWidgetChanged(DraggingWidgetChanged event)
	{
		// is dragging widget and mouse button released
		if (event.isDraggingWidget() && client.getMouseCurrentButton() == 0)
		{
			Widget draggedWidget = client.getDraggedWidget();
			Widget draggedOnWidget = client.getDraggedOnWidget();
			if (draggedWidget != null && draggedOnWidget != null)
			{
				int draggedGroupId = WidgetInfo.TO_GROUP(draggedWidget.getId());
				int draggedOnGroupId = WidgetInfo.TO_GROUP(draggedOnWidget.getId());
				if (draggedGroupId != WidgetID.MAGIC_GROUP_ID || draggedOnGroupId != WidgetID.MAGIC_GROUP_ID
						|| draggedOnWidget.getWidth() != MAGIC_WIDTH || draggedOnWidget.getHeight() != MAGIC_HEIGHT)
				{
					return;
				}
				// reset dragged on widget to prevent sending a drag widget packet to the server
				client.setDraggedOnWidget(null);

				int fromMagicIndex = getMagicIndex(draggedWidget);
				int toMagicIndex = getMagicIndex(draggedOnWidget);

				Magic tmp = magicOrder[toMagicIndex];
				magicOrder[toMagicIndex] = magicOrder[fromMagicIndex];
				magicOrder[fromMagicIndex] = tmp;

				save();
			}
		}
	}

	@Subscribe
	public void onWidgetMenuOptionClicked(WidgetMenuOptionClicked event)
	{
		if (event.getWidget() == WidgetInfo.FIXED_VIEWPORT_MAGIC_TAB
				|| event.getWidget() == WidgetInfo.RESIZABLE_VIEWPORT_MAGIC_TAB)
		{
			config.unlockMagicReordering(event.getMenuOption().equals(UNLOCK));
		}
	}

	private void clearMagicTabMenus()
	{
		menuManager.removeManagedCustomMenu(FIXED_MAGIC_TAB_LOCK);
		menuManager.removeManagedCustomMenu(RESIZABLE_MAGIC_TAB_LOCK);
		menuManager.removeManagedCustomMenu(FIXED_MAGIC_TAB_UNLOCK);
		menuManager.removeManagedCustomMenu(RESIZABLE_MAGIC_TAB_UNLOCK);
	}

	private void refreshMagicTabOption()
	{
		clearMagicTabMenus();
		if (config.unlockMagicReordering())
		{
			menuManager.addManagedCustomMenu(FIXED_MAGIC_TAB_LOCK);
			menuManager.addManagedCustomMenu(RESIZABLE_MAGIC_TAB_LOCK);
		}
		else
		{
			menuManager.addManagedCustomMenu(FIXED_MAGIC_TAB_UNLOCK);
			menuManager.addManagedCustomMenu(RESIZABLE_MAGIC_TAB_UNLOCK);
		}
	}

	private MagicTabState getMagicTabState()
	{
		HashTable componentTable = client.getComponentTable();
		for (Node node : componentTable.getNodes())
		{
			WidgetNode widgetNode = (WidgetNode) node;
			if (widgetNode.getId() == WidgetID.MAGIC_GROUP_ID)
			{
				return MagicTabState.NORMAL;
			}
		}
		return MagicTabState.NONE;
	}

	private void save()
	{
		config.magicOrder(magicOrderToString(magicOrder));
	}

	private void reorderMagics()
	{
		reorderMagics(config.unlockMagicReordering());
	}

	private void reorderMagics(boolean unlocked)
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		MagicTabState MagicTabState = getMagicTabState();

		if (MagicTabState == MagicTabState.NORMAL)
		{
			List<Widget> MagicWidgets = MAGIC_WIDGET_INFO_LIST.stream()
					.map(client::getWidget)
					.filter(Objects::nonNull)
					.collect(Collectors.toList());

			if (MagicWidgets.size() != MAGIC_WIDGET_INFO_LIST.size())
			{
				return;
			}

			for (int index = 0; index < magicOrder.length; index++)
			{
				Magic Magic = magicOrder[index];
				Widget MagicWidget = MagicWidgets.get(Magic.ordinal());

				int widgetConfig = MagicWidget.getClickMask();
				if (unlocked)
				{
					// allow dragging of this widget
					widgetConfig |= DRAG;
					// allow this widget to be dragged on
					widgetConfig |= DRAG_ON;
				}
				else
				{
					// remove drag flag
					widgetConfig &= ~DRAG;
					// remove drag on flag
					widgetConfig &= ~DRAG_ON;
				}
				MagicWidget.setClickMask(widgetConfig);

				int x = index % MAGIC_COLUMN_COUNT;
				int y = index / MAGIC_COLUMN_COUNT;
				int widgetX = (x * MAGIC_X_OFFSET) + 9;
				int widgetY = (y * MAGIC_Y_OFFSET) + 9;
				setWidgetPosition(MagicWidget, widgetX, widgetY);
			}
		}
	}

}
