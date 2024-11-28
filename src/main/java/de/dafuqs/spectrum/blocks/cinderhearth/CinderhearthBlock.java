package de.dafuqs.spectrum.blocks.cinderhearth;

import com.klikli_dev.modonomicon.api.multiblock.*;
import de.dafuqs.spectrum.compat.modonomicon.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.recipe.*;
import net.minecraft.screen.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.*;
import net.minecraft.util.hit.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public class CinderhearthBlock extends BlockWithEntity {
	
	public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
	
	public CinderhearthBlock(Settings settings) {
		super(settings);
		this.setDefaultState((this.stateManager.getDefaultState()).with(FACING, Direction.EAST));
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new CinderhearthBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		if (world.isClient) {
			return null;
		} else {
			return checkType(type, SpectrumBlockEntities.CINDERHEARTH, CinderhearthBlockEntity::serverTick);
		}
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			verifyStructure(world, pos, null);
			return ActionResult.SUCCESS;
		} else {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof CinderhearthBlockEntity cinderhearthBlockEntity) {
				cinderhearthBlockEntity.setOwner(player);
				if (verifyStructure(world, pos, (ServerPlayerEntity) player) != CinderhearthBlockEntity.CinderHearthStructureType.NONE) {
					player.openHandledScreen(cinderhearthBlockEntity);
				}
			}
			return ActionResult.CONSUME;
		}
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof CinderhearthBlockEntity cinderhearthBlockEntity) {
			if (placer instanceof PlayerEntity player) {
				cinderhearthBlockEntity.setOwner(player);
			}
			if (itemStack.hasCustomName()) {
				cinderhearthBlockEntity.setCustomName(itemStack.getName());
			}
		}
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!state.isOf(newState.getBlock())) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof CinderhearthBlockEntity cinderhearthBlockEntity) {
				if (world instanceof ServerWorld) {
					ItemScatterer.spawn(world, pos, cinderhearthBlockEntity);
				}
				world.updateComparators(pos, this);
			}
			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}
	
	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}
	
	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}
	
	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
	
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof CinderhearthBlockEntity cinderhearthBlockEntity) {
			Direction direction = state.get(FACING);
			Direction.Axis axis = direction.getAxis();
			double d = (double) pos.getX() + 0.5D;
			double e = pos.getY() + 0.4;
			double f = (double) pos.getZ() + 0.5D;
			
			Recipe<?> recipe = cinderhearthBlockEntity.getCurrentRecipe();
			if (recipe != null) {
				if (random.nextDouble() < 0.1D) {
					world.playSound(d, e, f, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 0.8F, false);
				}
				
				double g = 0.35D;
				double h = random.nextDouble() * 0.4D - 0.2D;
				double i = axis == Direction.Axis.X ? (double) direction.getOffsetX() * g : h;
				double j = random.nextDouble() * 4.0D / 16.0D;
				double k = axis == Direction.Axis.Z ? (double) direction.getOffsetZ() * g : h;
				world.addParticle(ParticleTypes.FLAME, d + i, e + j, f + k, 0.0D, 0.0D, 0.0D);
				world.addParticle(ParticleTypes.SMOKE, d + i, e + j, f + k, 0.0D, 0.0D, 0.0D);
				
				if (random.nextBoolean()) {
					double g2 = -3D / 16D;
					double h2 = 4D / 16D;
					double i2 = axis == Direction.Axis.X ? (double) direction.getOffsetX() * g2 : h2;
					double k2 = axis == Direction.Axis.Z ? (double) direction.getOffsetZ() * g2 : h2;
					world.addParticle(ParticleTypes.CLOUD, d + i2, pos.getY() + 1.1, f + k2, 0.0D, 0.06D, 0.0D);
				}
			}
			if (cinderhearthBlockEntity.structure == CinderhearthBlockEntity.CinderHearthStructureType.WITH_LAVA) {
				for (int v = 0; v < 2; v++) {
					double g3 = 1.5 - random.nextDouble() * 2.0;
					double h3 = 1.5 - random.nextDouble() * 3.0;
					double i3 = axis == Direction.Axis.X ? (double) direction.getOffsetX() * g3 : h3;
					double k3 = axis == Direction.Axis.Z ? (double) direction.getOffsetZ() * g3 : h3;
					world.addParticle(SpectrumParticleTypes.ORANGE_SPARKLE_RISING, d + i3, pos.getY() - 1.2, f + k3, 0.0D, 0.1D, 0.0D);
				}
			}
		}
	}
	
	public static CinderhearthBlockEntity.CinderHearthStructureType verifyStructure(World world, @NotNull BlockPos blockPos, @Nullable ServerPlayerEntity serverPlayerEntity) {
		BlockRotation rotation = Support.rotationFromDirection(world.getBlockState(blockPos).get(FACING).getOpposite());
		
		Multiblock multiblock = SpectrumMultiblocks.get(SpectrumMultiblocks.CINDERHEARTH);
		CinderhearthBlockEntity.CinderHearthStructureType completedStructure = CinderhearthBlockEntity.CinderHearthStructureType.NONE;
		
		if (multiblock.validate(world, blockPos.down(3), rotation)) {
			completedStructure = CinderhearthBlockEntity.CinderHearthStructureType.WITH_LAVA;
		} else {
			multiblock = SpectrumMultiblocks.get(SpectrumMultiblocks.CINDERHEARTH_WITHOUT_LAVA);
			if (multiblock.validate(world, blockPos.down(3), rotation)) {
				completedStructure = CinderhearthBlockEntity.CinderHearthStructureType.WITHOUT_LAVA;
			}
		}
		
		boolean structureValid = completedStructure != CinderhearthBlockEntity.CinderHearthStructureType.NONE;
		
		if (world.isClient) {
			if (!structureValid) {
				ModonomiconHelper.renderMultiblock(SpectrumMultiblocks.get(SpectrumMultiblocks.CINDERHEARTH), SpectrumMultiblocks.CINDERHEARTH_TEXT, blockPos.down(4), rotation);
			}
		} else if (structureValid && serverPlayerEntity != null) {
			SpectrumAdvancementCriteria.COMPLETED_MULTIBLOCK.trigger(serverPlayerEntity, multiblock);
		}
		
		return completedStructure;
	}
	
	@Override
	public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
		if (world.isClient()) {
			ModonomiconHelper.clearRenderedMultiblock(SpectrumMultiblocks.get(SpectrumMultiblocks.CINDERHEARTH));
		}
	}
	
}
