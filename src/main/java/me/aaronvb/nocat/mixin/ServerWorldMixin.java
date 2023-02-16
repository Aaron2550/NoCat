package me.aaronvb.nocat.mixin;

import me.aaronvb.nocat.NoCatInitializer;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.spawner.CatSpawner;
import net.minecraft.world.spawner.Spawner;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

	@Mutable @Shadow @Final private List<Spawner> spawners;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(MinecraftServer server, Executor workerExecutor, LevelStorage.Session session, ServerWorldProperties properties, RegistryKey worldKey, DimensionOptions dimensionOptions, WorldGenerationProgressListener worldGenerationProgressListener, boolean debugWorld, long seed, List spawners, boolean shouldTickTime, CallbackInfo ci) {
		List<Spawner> newSpawners = new ArrayList<>();

		for (Spawner spawner : this.spawners) {
			if (!(spawner instanceof CatSpawner)) {
				newSpawners.add(spawner);
			} else {
				NoCatInitializer.LOGGER.info("NoCat now prevents cat spawning in this World");
			}
		}

		this.spawners = newSpawners;
	}
}
