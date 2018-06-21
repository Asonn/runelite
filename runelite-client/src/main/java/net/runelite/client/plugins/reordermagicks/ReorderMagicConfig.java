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

import net.runelite.api.Magic;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(
	keyName = ReorderMagicPlugin.CONFIG_GROUP_KEY,
	name = "Reorder Magics",
	description = "swap magics"
)
public interface ReorderMagicConfig extends Config
{

	@ConfigItem(
		keyName = ReorderMagicPlugin.CONFIG_UNLOCK_REORDERING_KEY,
		name = "Unlock Magic Reordering",
		description = "Configures whether or not you can reorder your magic spellbook",
		position = 1
	)
	default boolean unlockMagicReordering()
	{
		return false;
	}

	@ConfigItem(
		keyName = ReorderMagicPlugin.CONFIG_UNLOCK_REORDERING_KEY,
		name = "",
		description = ""
	)
	void unlockMagicReordering(boolean unlock);

	@ConfigItem(
		keyName = ReorderMagicPlugin.CONFIG_MAGIC_ORDER_KEY,
		name = "Magic spell Order",
		description = "Configures the order of your spells",
		hidden = true,
		position = 2
	)
    default String magicOrder()
	{
        return ReorderMagicPlugin.magicOrderToString(Magic.values());
	}

	@ConfigItem(
		keyName = ReorderMagicPlugin.CONFIG_MAGIC_ORDER_KEY,
		name = "",
		description = ""
	)
	void magicOrder(String magicOrder);

}
