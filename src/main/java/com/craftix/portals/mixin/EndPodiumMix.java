package com.craftix.portals.mixin;

import net.minecraft.ResourceLocationException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(EndPodiumFeature.class)
public abstract class EndPodiumMix {

    @Shadow
    @Final
    private boolean active;

    @Inject(method = "place", at = @At("HEAD"), cancellable = true)
    private void injected(FeaturePlaceContext<NoneFeatureConfiguration> p_159723_, CallbackInfoReturnable<Boolean> cir) {
        BlockPos blockpos = p_159723_.origin();
        WorldGenLevel worldgenlevel = p_159723_.level();

        StructureManager structuremanager = worldgenlevel.getServer().getStructureManager();

        Optional<StructureTemplate> optional;
        try {
            optional = structuremanager.get(new ResourceLocation("portals", active ? "end_portal" : "end_portal_unfilled"));
            if (optional.isPresent()) {

                blockpos = blockpos.offset(-optional.get().getSize().getX() / 2, 0, -optional.get().getSize().getZ() / 2);
                StructurePlaceSettings structureplacesettings = (new StructurePlaceSettings()).setMirror(Mirror.NONE).setRotation(Rotation.NONE).setIgnoreEntities(false);
                optional.get().placeInWorld(worldgenlevel, blockpos, blockpos, structureplacesettings, worldgenlevel.getRandom(), 2);
                cir.setReturnValue(true);
                return;

            }
        } catch (ResourceLocationException resourcelocationexception) {
            System.out.println("Can't place portal");
        }

    }


}