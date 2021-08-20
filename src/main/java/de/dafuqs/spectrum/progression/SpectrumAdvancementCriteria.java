package de.dafuqs.spectrum.progression;

import de.dafuqs.spectrum.progression.advancement.CompletedMultiblockCriterion;
import de.dafuqs.spectrum.progression.advancement.HadRevelationCriterion;
import de.dafuqs.spectrum.progression.advancement.HasAdvancementCriterion;
import de.dafuqs.spectrum.progression.advancement.PedestalCraftingCriterion;
import net.fabricmc.fabric.mixin.object.builder.CriteriaAccessor;

public class SpectrumAdvancementCriteria {

    public static HasAdvancementCriterion ADVANCEMENT_GOTTEN;
    public static HadRevelationCriterion HAD_REVELATION;
    public static PedestalCraftingCriterion PEDESTAL_CRAFTING;
    public static CompletedMultiblockCriterion COMPLETED_MULTIBLOCK;

    public static void register() {
        ADVANCEMENT_GOTTEN = CriteriaAccessor.callRegister(new HasAdvancementCriterion());
        HAD_REVELATION = CriteriaAccessor.callRegister(new HadRevelationCriterion());
        PEDESTAL_CRAFTING = CriteriaAccessor.callRegister(new PedestalCraftingCriterion());
        COMPLETED_MULTIBLOCK = CriteriaAccessor.callRegister(new CompletedMultiblockCriterion());
    }

}
