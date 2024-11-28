package de.dafuqs.spectrum.blocks.decoration;

import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.state.*;
import net.minecraft.state.property.*;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.*;

public class CardinalFacingBlock extends Block {
	
	public static final BooleanProperty CARDINAL_FACING = BooleanProperty.of("cardinal_facing");
	
	public CardinalFacingBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(CARDINAL_FACING, false));
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		Direction facing = ctx.getHorizontalPlayerFacing();
		boolean facingVertical = facing.equals(Direction.EAST) || facing.equals(Direction.WEST);
		return this.getDefaultState().with(CARDINAL_FACING, facingVertical);
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(CARDINAL_FACING);
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		boolean cardinal = state.get(CARDINAL_FACING);
		return state.with(CARDINAL_FACING, (rotation.ordinal() % 2 == 1) != cardinal);
	}
}
