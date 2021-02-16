package com.nuke3dtv.nukesecon.setup;

import com.nuke3dtv.nukesecon.blocks.*;
import com.nuke3dtv.nukesecon.data.LootTableModifier;
import com.nuke3dtv.nukesecon.items.*;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.nuke3dtv.nukesecon.NukesEcon.MODID;

public class Registration {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MODID);
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MODID);
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);
    private static final DeferredRegister<GlobalLootModifierSerializer<?>> GLOBAL_LOOT_MODS = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, MODID);

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
       GLOBAL_LOOT_MODS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    // Blocks
    public static final RegistryObject<StrongBox> STRONGBOX = BLOCKS.register("strongbox", StrongBox::new);
    public static final RegistryObject<Item> STRONGBOX_ITEM = ITEMS.register("strongbox", () -> new BlockItem(STRONGBOX.get(), new Item.Properties().group(ModSetup.ITEM_GROUP)));
    public static final RegistryObject<TileEntityType<StrongBoxTile>> STRONGBOX_TILE = TILES.register("strongbox", () -> TileEntityType.Builder.create(StrongBoxTile::new, STRONGBOX.get()).build(null));

    // Containers
    public static final RegistryObject<ContainerType<StrongBoxContainer>> STRONGBOX_CONTAINER = CONTAINERS.register("strongbox", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntityWorld();
        return new StrongBoxContainer(windowId, world, pos, inv, inv.player);
    }));

    // Items
    public static final RegistryObject<IronLock> IRONLOCK = ITEMS.register("ironlock", IronLock::new);
    public static final RegistryObject<WoodCoin> WOODCOIN = ITEMS.register("woodcoin", WoodCoin::new);
    public static final RegistryObject<IronCoin> IRONCOIN = ITEMS.register("ironcoin", IronCoin::new);
    public static final RegistryObject<CopperCoin> COPPERCOIN = ITEMS.register("coppercoin", CopperCoin::new);
    public static final RegistryObject<GoldCoin> GOLDCOIN = ITEMS.register("goldcoin", GoldCoin::new);
    public static final RegistryObject<DiamondCoin> DIAMONDCOIN = ITEMS.register("diamondcoin", DiamondCoin::new);
    public static final RegistryObject<EmeraldCoin> EMERALDCOIN = ITEMS.register("emeraldcoin", EmeraldCoin::new);

    // Loot Tables
    public static final RegistryObject<LootTableModifier.Serializer> GLM_ZOMBIE = GLOBAL_LOOT_MODS.register("zombie", LootTableModifier.Serializer::new);

}
