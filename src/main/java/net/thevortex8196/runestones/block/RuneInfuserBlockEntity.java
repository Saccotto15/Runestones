package net.thevortex8196.runestones.block;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.thevortex8196.runestones.Runestones;
import net.thevortex8196.runestones.item.ModItems;
import net.thevortex8196.runestones.recipe.ModRecipes;
import net.thevortex8196.runestones.recipe.RuneInfuserRecipe;
import net.thevortex8196.runestones.recipe.RuneInfuserRecipeInput;
import net.thevortex8196.runestones.screen.RuneInfuserScreenHandler;
import org.jetbrains.annotations.Nullable;
import net.minecraft.inventory.SidedInventory;

import java.util.Optional;

public class RuneInfuserBlockEntity extends BlockEntity
        implements ExtendedScreenHandlerFactory<BlockPos>, ImplementedInventory, SidedInventory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);

    private static final int BLANK_RUNESTONE_SLOT = 0;
    private static final int INPUT_SLOT = 1;
    private static final int DIAMOND_SLOT = 2;
    private static final int OUTPUT_SLOT = 3;

    public RuneInfuserBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RUNE_INFUSER_BE, pos, state);
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
        return this.pos;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.runestones.rune_infuser");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new RuneInfuserScreenHandler(syncId, inv, this);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        Inventories.readNbt(nbt, inventory, registryLookup);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (hasRecipe()) {
            craftItem();
        } else if (!getStack(OUTPUT_SLOT).isEmpty()) {
            setStack(OUTPUT_SLOT, ItemStack.EMPTY);
            markDirty();
            world.updateListeners(pos, state, state, 3);
        }
    }

    private void craftItem() {
        Optional<RecipeEntry<RuneInfuserRecipe>> recipe = getCurrentRecipe();

        ItemStack output = recipe.get().value().output();

        this.setStack(OUTPUT_SLOT, new ItemStack(output.getItem(), 1));
    }


    private boolean hasRecipe() {
        Optional<RecipeEntry<RuneInfuserRecipe>> recipe = getCurrentRecipe();
        if(recipe.isEmpty()) {
            return false;
        }

        return true;
    }

    private Optional<RecipeEntry<RuneInfuserRecipe>> getCurrentRecipe() {
        return this.getWorld().getRecipeManager()
                .getFirstMatch(ModRecipes.RUNE_INFUSER_TYPE, new RuneInfuserRecipeInput(inventory.get(BLANK_RUNESTONE_SLOT), inventory.get(INPUT_SLOT), inventory.get(DIAMOND_SLOT)), this.getWorld());
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack result = Inventories.splitStack(this.inventory, slot, amount);

        if (slot == OUTPUT_SLOT && !result.isEmpty()) {
            this.getStack(BLANK_RUNESTONE_SLOT).decrement(1);
            this.getStack(INPUT_SLOT).decrement(1);
            this.getStack(DIAMOND_SLOT).decrement(1);
            markDirty();

            if (world != null) {
                world.updateListeners(pos, getCachedState(), getCachedState(), 3);
            }
        }

        return result;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }
}
