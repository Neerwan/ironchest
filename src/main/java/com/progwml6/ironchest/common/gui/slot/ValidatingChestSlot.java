/*******************************************************************************
 * Copyright (c) 2012 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * <p>
 * Contributors:
 * cpw - initial API and implementation
 ******************************************************************************/
package com.progwml6.ironchest.common.gui.slot;

import com.progwml6.ironchest.common.blocks.IronChestType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ValidatingChestSlot extends Slot
{
    private IronChestType type;

    public ValidatingChestSlot(IInventory inventoryIn, int slotIndex, int xPosition, int yPosition, IronChestType type)
    {
        super(inventoryIn, slotIndex, xPosition, yPosition);
        this.type = type;
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return this.type.acceptsStack(stack);
    }
}
