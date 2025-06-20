package net.thevortex8196.runestones.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
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
        Integer max = runeCapacities.entrySet().stream()
                .filter(entry -> entry.getKey().isInstance(item))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);

        // If not a supported item, return
        if (max == null) {
            return;
        } else {
            stack.set(ModDataComponents.MAX_RUNESTONES, max);
        }

        // Retrieve your runestones
        String[] runestones = stack.get(ModDataComponents.RUNESTONES);
        if (runestones == null) {
            runestones = new String[0];
            stack.set(ModDataComponents.RUNESTONES, runestones);
        }

        int curr = runestones.length;


        tooltip.add(Text.literal(""));
        tooltip.add(Text.literal("§6Runestone Compatible §e(" + curr + "/" + max + ")"));

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

        int max;

        try {
            max = stack.get(ModDataComponents.MAX_RUNESTONES);
        } catch (Throwable e) {
            return;
        }

        String[] runestones = stack.get(ModDataComponents.RUNESTONES);
        List<String> runestonesL = Arrays.asList(runestones);

        if (runestones == null || !selected) {
            return;
        }

        if (runestonesL.contains("luck") && entity instanceof LivingEntity living) {
            living.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.HERO_OF_THE_VILLAGE,
                    1200,
                    4,
                    false,
                    false,
                    true
            ));
        }

        if (runestonesL.contains("heart") && entity instanceof LivingEntity living) {
            living.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.HEALTH_BOOST,
                    1200,
                    1,
                    false,
                    false,
                    true
            ));
        }

        if (runestonesL.contains("strength") && entity instanceof LivingEntity living) {
            living.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.STRENGTH,
                    20,
                    1,
                    false,
                    false,
                    true
            ));
        }
    }

    @Inject(method = "use", at = @At("TAIL"))
    private void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack main = user.getStackInHand(hand);
        int maxRunes;

        // Check if the main-hand item is rune-compatible
        try {
            maxRunes = main.get(ModDataComponents.MAX_RUNESTONES);
        } catch (Throwable e) {
            return;
        }

        String[] currentRunes = main.get(ModDataComponents.RUNESTONES);
        if (currentRunes == null) currentRunes = new String[0];

        ItemStack offHand = user.getOffHandStack();

        String[] runestones = main.get(ModDataComponents.RUNESTONES);
        List<String> runestonesL = Arrays.asList(runestones);

        if (runestonesL.contains("dash")) {
            Vec3d lookDirection = user.getRotationVec(1.0F);
            double strength = 2.0; // Adjust force here

            user.setVelocity(lookDirection.multiply(strength));
            user.velocityModified = true;

            if (!user.getWorld().isClient) {
                user.getWorld().playSound(
                        null,
                        user.getX(), user.getY(), user.getZ(),
                        SoundEvents.ENTITY_PLAYER_TELEPORT, // you can choose another
                        SoundCategory.PLAYERS,
                        1.0f, 1.0f
                );
            }
            if (!user.isCreative()) {user.getItemCooldownManager().set(main.getItem(), 150);}
        }

        // 🛠️ 1) Sneak + empty off-hand = remove last rune from main, give rune item to off-hand
        if (user.isSneaking() && offHand.isEmpty() && currentRunes.length > 0) {
            String last = currentRunes[currentRunes.length - 1];

            String[] updated = Arrays.copyOf(currentRunes, currentRunes.length - 1);  // removes last :contentReference[oaicite:1]{index=1}
            main.set(ModDataComponents.RUNESTONES, updated);

            Item runeItem = switch (last) {
                case "luck" -> ModItems.LUCK_RUNE;
                case "dash" -> ModItems.DASH_RUNE;
                case "heart" -> ModItems.HEART_RUNE;
                case "strength" -> ModItems.STRENGTH_RUNE;
                case "sky" -> ModItems.SKY_RUNE;
                default -> null;
            };

            if (runeItem != null) {
                user.setStackInHand(Hand.OFF_HAND, new ItemStack(runeItem));
            }
            return;  // Exit after performing removal
        }

        // 🧩 2) Off-hand has a rune (and player isn't sneaking) = add rune back into main component
        if (!offHand.isEmpty() && user.isSneaking()) {
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
                String[] appended = Arrays.copyOf(currentRunes, currentRunes.length + 1);
                appended[currentRunes.length] = rune;
                main.set(ModDataComponents.RUNESTONES, appended);
                user.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);
            }
        }
    }
}
