package com.craftix.portals.mixin;

import net.minecraft.ResourceLocationException;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.levelgen.feature.EndGatewayFeature;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.EndGatewayConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(EndGatewayFeature.class)
public abstract class EndGatewayMix {

    @Inject(method = "place", at = @At("HEAD"), cancellable = true)
    private void injected(FeaturePlaceContext<EndGatewayConfiguration> p_159715_, CallbackInfoReturnable<Boolean> cir) {
        BlockPos blockpos = p_159715_.origin();
        WorldGenLevel worldgenlevel = p_159715_.level();
        EndGatewayConfiguration endgatewayconfiguration = p_159715_.config();

        StructureManager structuremanager = worldgenlevel.getServer().getStructureManager();

        Optional<StructureTemplate> optional;
        try {
            optional = structuremanager.get(new ResourceLocation("portals", "end_gateway"));
            if (optional.isPresent()) {

                blockpos = blockpos.offset(-optional.get().getSize().getX() / 2, 0, -optional.get().getSize().getZ() / 2);
                StructurePlaceSettings structureplacesettings = (new StructurePlaceSettings()).setMirror(Mirror.NONE).setRotation(Rotation.NONE).setIgnoreEntities(false);
                optional.get().placeInWorld(worldgenlevel, blockpos, blockpos, structureplacesettings, worldgenlevel.getRandom(), 2);


                for (BlockPos blockpos1 : BlockPos.betweenClosed(blockpos.offset(-5, -5, -5), blockpos.offset(5, 5, 5))) {
                    if (worldgenlevel.getBlockState(blockpos1).is(Blocks.END_GATEWAY)) {
                        endgatewayconfiguration.getExit().ifPresent((p_65699_) -> {
                            BlockEntity blockentity = worldgenlevel.getBlockEntity(blockpos1);
                            if (blockentity instanceof TheEndGatewayBlockEntity) {
                                TheEndGatewayBlockEntity theendgatewayblockentity = (TheEndGatewayBlockEntity) blockentity;
                                theendgatewayblockentity.setExitPosition(p_65699_, endgatewayconfiguration.isExitExact());
                                blockentity.setChanged();
                            }

                        });
                    }


                }

                cir.setReturnValue(true);
                return;

            }
        } catch (ResourceLocationException resourcelocationexception) {
            System.out.println("Can't place portal");
        }

    }


}
