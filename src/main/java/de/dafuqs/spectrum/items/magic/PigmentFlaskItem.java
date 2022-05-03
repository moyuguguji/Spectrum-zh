package de.dafuqs.spectrum.items.magic;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.energy.CappedElementalPigmentEnergyStorageItem;
import de.dafuqs.spectrum.energy.storage.IndividualAndTotalCappedElementalPigmentEnergyStorage;
import de.dafuqs.spectrum.energy.storage.SinglePigmentEnergyStorage;
import de.dafuqs.spectrum.items.trinkets.SpectrumTrinketItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PigmentFlaskItem extends SpectrumTrinketItem implements CappedElementalPigmentEnergyStorageItem {
	
	private final long maxEnergy;
	
	public PigmentFlaskItem(Settings settings, long maxEnergy) {
		super(settings, new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_pigment_flask"));
		this.maxEnergy = maxEnergy;
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(new TranslatableText("item.spectrum.pigment_flask.tooltip", this.maxEnergy));
		
		getEnergyStorage(stack).addTooltip(world, tooltip, context);
	}
	
	@Override
	public SinglePigmentEnergyStorage getEnergyStorage(ItemStack itemStack) {
		NbtCompound compound = itemStack.getNbt();
		if(compound != null && compound.contains("EnergyStore")) {
			return SinglePigmentEnergyStorage.fromNbt(compound.getCompound("EnergyStore"));
		}
		return new SinglePigmentEnergyStorage(this.maxEnergy);
	}
	
	@Override
	public void setEnergyStorage(ItemStack itemStack, IndividualAndTotalCappedElementalPigmentEnergyStorage storage) {
		NbtCompound compound = itemStack.getOrCreateNbt();
		compound.put("EnergyStore", storage.toNbt());
	}
	
}