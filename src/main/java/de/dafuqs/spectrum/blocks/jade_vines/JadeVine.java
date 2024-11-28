package de.dafuqs.spectrum.blocks.jade_vines;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import net.minecraft.block.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.state.property.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.*;
import net.minecraft.util.shape.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

public interface JadeVine {
	
	BooleanProperty DEAD = BooleanProperty.of("dead");
	VoxelShape BULB_SHAPE = Block.createCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	VoxelShape TIP_SHAPE = Block.createCuboidShape(2.0D, 2.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	
	static void spawnBloomParticlesClient(World world, BlockPos blockPos) {
		spawnParticlesClient(world, blockPos, SpectrumParticleTypes.JADE_VINES_BLOOM);
		
		Random random = world.random;
		double x = blockPos.getX() + 0.2 + (random.nextFloat() * 0.6);
		double y = blockPos.getY() + 0.2 + (random.nextFloat() * 0.6);
		double z = blockPos.getZ() + 0.2 + (random.nextFloat() * 0.6);
		world.addParticle(SpectrumParticleTypes.PINK_FALLING_SPORE_BLOSSOM, x, y, z, 0.0D, 0.0D, 0.0D);
	}
	
	static void spawnParticlesClient(World world, BlockPos blockPos) {
		spawnParticlesClient(world, blockPos, SpectrumParticleTypes.JADE_VINES);
	}
	
	private static void spawnParticlesClient(World world, BlockPos blockPos, ParticleEffect particleType) {
		Random random = world.random;
		double x = blockPos.getX() + 0.2 + (random.nextFloat() * 0.6);
		double y = blockPos.getY() + 0.2 + (random.nextFloat() * 0.6);
		double z = blockPos.getZ() + 0.2 + (random.nextFloat() * 0.6);
		
		double velX = 0.06 - random.nextFloat() * 0.12;
		double velY = 0.06 - random.nextFloat() * 0.12;
		double velZ = 0.06 - random.nextFloat() * 0.12;
		
		world.addParticle(particleType, x, y, z, velX, velY, velZ);
	}
	
	static void spawnParticlesServer(ServerWorld world, BlockPos blockPos, int amount) {
		SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity(world, Vec3d.ofCenter(blockPos), SpectrumParticleTypes.JADE_VINES, amount, new Vec3d(0.6, 0.6, 0.6), new Vec3d(0.12, 0.12, 0.12));
	}
	
	static boolean isExposedToSunlight(@NotNull World world, @NotNull BlockPos blockPos) {
		return world.getLightLevel(LightType.SKY, blockPos) > 8 && TimeHelper.isBrightSunlight(world);
	}
	
	boolean setToAge(World world, BlockPos blockPos, int age);
	
}
