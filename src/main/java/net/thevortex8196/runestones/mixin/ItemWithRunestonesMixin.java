package net.thevortex8196.runestones.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.thevortex8196.runestones.item.ModItems;
import net.thevortex8196.runestones.util.ModDataComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mixin(Item.class)
public abstract class ItemWithRunestonesMixin {
    @Unique
    Map<Class<? extends Item>, Integer> runeCapacities = Map.of(
            SwordItem.class, 1,
            MaceItem.class, 5,
            AxeItem.class, 1
    );

    @Inject(method = "appendTooltip", at = @At("TAIL"))
    private void onAppendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type, CallbackInfo ci) {

        Item item = stack.getItem();

        // Look up the max capacity from runeCapacities
        Integer max = runeCapacities.entrySet().stream()
                .filter(e -> e.getKey().isInstance(item))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);

        // Read existing max runestones from the stack
        Integer currentMax = stack.get(ModDataComponents.MAX_RUNESTONES);

        // If the component hasn't been set yet and we have a capacity, set it
        if (currentMax == null && max != null) {
            stack.set(ModDataComponents.MAX_RUNESTONES, max);
            currentMax = max;
        }

        // If still null (unsupported or unset), there's nothing more to do
        if (currentMax == null) return;

        // Retrieve your runestones
        String[] runestones = stack.get(ModDataComponents.RUNESTONES);
        if (runestones == null) {
            runestones = new String[0];
            stack.set(ModDataComponents.RUNESTONES, runestones);
        }

        int curr = runestones.length;


        tooltip.add(Text.literal(""));
        tooltip.add(Text.literal("§6Runestone Compatible §e(" + curr + "/" + currentMax + ")"));

        Map<String, String> colorMap = Map.of(
                "luck", "2",
                "dash", "7",
                "heart", "4",
                "strength", "e",
                "sky", "5"
        );

        for (String runestone : runestones) {
            String name = runestone.toLowerCase();
            String colorId = colorMap.getOrDefault(name, "0");
            String displayName = name.substring(0, 1).toUpperCase() + name.substring(1);
            tooltip.add(Text.literal("§" + colorId + "[" + displayName + "]"));
        }
    }

    @Inject(method = "inventoryTick", at = @At("TAIL"))
    private void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        // Exit early if item isn't rune-compatible or not currently selected
        if (stack.get(ModDataComponents.MAX_RUNESTONES) == null || !selected || !(entity instanceof LivingEntity living)) {
            return;
        }

        String[] runestones = stack.get(ModDataComponents.RUNESTONES);
        if (runestones == null) return;

        List<String> runestoneList = Arrays.asList(runestones);

        applyEffectIfPresent(runestoneList, "luck", living, StatusEffects.HERO_OF_THE_VILLAGE, 1200, 4);
        applyEffectIfPresent(runestoneList, "heart", living, StatusEffects.HEALTH_BOOST, 1200, 1);
        applyEffectIfPresent(runestoneList, "strength", living, StatusEffects.STRENGTH, 20, 1);
    }

    @Unique
    private void applyEffectIfPresent(List<String> runestones, String key, LivingEntity living,
                                      RegistryEntry<StatusEffect> effect, int duration, int amplifier) {
        if (runestones.contains(key)) {
            living.addStatusEffect(new StatusEffectInstance(effect, duration, amplifier, false, false, true));
        }
    }

    @Inject(method = "use", at = @At("TAIL"))
    private void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack main = user.getStackInHand(hand);
        Integer maxRunes = main.get(ModDataComponents.MAX_RUNESTONES);
        if (maxRunes == null) return;

        String[] currentRunes = Optional.ofNullable(main.get(ModDataComponents.RUNESTONES)).orElse(new String[0]);
        ItemStack offHand = user.getOffHandStack();
        List<String> runesList = Arrays.asList(currentRunes);

        // 1️⃣ Sneak + empty off-hand: remove last rune
        if (user.isSneaking() && offHand.isEmpty() && currentRunes.length > 0) {
            removeLastRune(main, user, currentRunes);
            return;
        }

        // 2️⃣ Sneak + off-hand rune item: add rune back into main
        if (user.isSneaking() && !offHand.isEmpty()) {
            addRuneFromOffHand(main, user, offHand, currentRunes, maxRunes);
            return;
        }

        // 3️⃣ Dash ability
        if (runesList.contains("dash")) {
            performDash(user, main, world);
        }
    }

    @Unique
    private void removeLastRune(ItemStack main, PlayerEntity user, String[] currentRunes) {
        int idx = currentRunes.length - 1;
        String last = currentRunes[idx];
        String[] updated = Arrays.copyOf(currentRunes, idx);
        main.set(ModDataComponents.RUNESTONES, updated);

        Item runeItem = switch (last) {
            case "luck"     -> ModItems.LUCK_RUNE;
            case "dash"     -> ModItems.DASH_RUNE;
            case "heart"    -> ModItems.HEART_RUNE;
            case "strength" -> ModItems.STRENGTH_RUNE;
            case "sky"      -> ModItems.SKY_RUNE;
            default         -> null;
        };
        if (runeItem != null) {
            user.setStackInHand(Hand.OFF_HAND, new ItemStack(runeItem));
        }
    }

    @Unique
    private void addRuneFromOffHand(ItemStack main, PlayerEntity user, ItemStack offHand, String[] currentRunes, int maxRunes) {
        Item offItem = offHand.getItem();
        String rune = null;

        if (offItem == ModItems.LUCK_RUNE) {
            rune = "luck";
        } else if (offItem == ModItems.DASH_RUNE) {
            rune = "dash";
        } else if (offItem == ModItems.HEART_RUNE) {
            rune = "heart";
        } else if (offItem == ModItems.STRENGTH_RUNE) {
            rune = "strength";
        } else if (offItem == ModItems.SKY_RUNE) {
            rune = "sky";
        }

        if (rune != null && currentRunes.length < maxRunes) {
            String[] updated = Arrays.copyOf(currentRunes, currentRunes.length + 1);
            updated[currentRunes.length] = rune;
            main.set(ModDataComponents.RUNESTONES, updated);
            user.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);
        }
    }

    @Unique
    private void performDash(PlayerEntity user, ItemStack main, World world) {
        Vec3d dv = user.getRotationVec(1.0F).multiply(2.0);
        user.setVelocity(dv);
        user.velocityModified = true;

        if (!world.isClient) {
            world.playSound(
                    null,
                    user.getX(), user.getY(), user.getZ(),
                    SoundEvents.ENTITY_PLAYER_TELEPORT,
                    SoundCategory.PLAYERS,
                    1.0f, 1.0f
            );
        }

        if (!user.isCreative()) {
            user.getItemCooldownManager().set(main.getItem(), 150);
        }
    }
}
