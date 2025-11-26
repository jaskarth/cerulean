package fmt.cerulean.compat.emi;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import fmt.cerulean.Cerulean;
import fmt.cerulean.flow.FlowResource;
import fmt.cerulean.flow.FlowResource.Brightness;
import fmt.cerulean.flow.FlowResource.Color;
import fmt.cerulean.flow.FlowResources;
import fmt.cerulean.flow.recipe.*;
import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.registry.CeruleanItems;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CeruleanEmiPlugin implements EmiPlugin {
	private static final Set<Color> ALL_COLORS = Stream.of(Color.values()).collect(Collectors.toSet());
	private static final Set<Brightness> ALL_BRIGHTNESSES = Stream.of(Brightness.values()).collect(Collectors.toSet());
	private static final Set<Brightness> ALL_BRIGHTNESSES_EXCEPT_DIM = ALL_BRIGHTNESSES.stream().filter(b -> b != Brightness.DIM).collect(Collectors.toSet());
	private static final Set<Brightness> ALL_BRIGHTNESSES_EXCEPT_BRILLIANT = ALL_BRIGHTNESSES.stream().filter(b -> b != Brightness.BRILLIANT).collect(Collectors.toSet());
	private static final EmiIngredient ALL_STARS = of(Arrays.stream(Color.values()).collect(Collectors.toSet()), ALL_BRIGHTNESSES);
	public static final EmiRecipeCategory BRUSHING = new EmiRecipeCategory(Cerulean.id("brushing"), of(FlowResources.star(Color.CERULEAN, Brightness.BRILLIANT)));
	public static final EmiRecipeCategory PHOTOGRAPHIC_TRANSFORMATION = new EmiRecipeCategory(Cerulean.id("photographic_transformation"), EmiStack.of(CeruleanItems.PHOTOGRAPH));
	public static final EmiRecipeCategory STRONGBOX_RIPENING = new EmiRecipeCategory(Cerulean.id("strongbox_ripening"), EmiStack.of(CeruleanBlocks.STRONGBOX));

	@Override
	public void register(EmiRegistry registry) {
		registry.removeEmiStacks(EmiStack.of(CeruleanBlocks.INKY_VOID));
		registry.addCategory(BRUSHING);
		registry.addCategory(PHOTOGRAPHIC_TRANSFORMATION);
		registry.addCategory(STRONGBOX_RIPENING);
		registry.addWorkstation(BRUSHING, EmiStack.of(CeruleanBlocks.STAR_WELL));
		registry.addWorkstation(PHOTOGRAPHIC_TRANSFORMATION, EmiStack.of(CeruleanItems.CAMERA));
		registry.addWorkstation(STRONGBOX_RIPENING, EmiStack.of(CeruleanBlocks.STRONGBOX));
		for (BrushRecipe recipe : Stream.concat(BrushRecipes.SOLO_RECIPES.stream(), BrushRecipes.DUAL_RECIPES.stream()).toList()) {
			Identifier id = BrushRecipes.GET_ID.get(recipe);
			if (recipe instanceof InspirationBrushRecipe real) {
				registry.addRecipe(new EmiBrushRecipe(id,
					inputStars(real.canvas),
					compressy(real.input.stream().map(EmiIngredient::of).toList()),
					EmiStack.EMPTY,
					List.of(EmiStack.of(real.output)),
					real.canvas.validBlocks.isEmpty() ? List.of() : List.of(real.canvas.validBlocks.iterator().next().getDefaultState()),
					null
				));
			} else if (recipe instanceof BerryFlavoringBrushRecipe real) {
				registry.addRecipe(new EmiBrushRecipe(id,
					List.of(ALL_STARS),
					List.of(EmiStack.of(CeruleanItems.BERRIES)),
					EmiStack.EMPTY,
					List.of(),
					List.of(),
					Text.translatable("info.cerulean.berry_flavor")
				));
			} else if (recipe instanceof UnblightBrushRecipe real) {
				registry.addRecipe(new EmiBrushRecipe(id,
					inputStars(real.canvas),
					List.of(),
					of(Set.of(real.color), ALL_BRIGHTNESSES),
					List.of(),
					List.of(real.grown),
					Text.translatable("info.cerulean.unblight")
				));
			} else if (recipe instanceof ManifestationBrushRecipe real) {
				registry.addRecipe(new EmiBrushRecipe(id,
					List.of(of(Set.of(Color.LILAC), ALL_BRIGHTNESSES)),
					List.of(),
					EmiStack.EMPTY,
					List.of(),
					List.of(),
					Text.translatable("info.cerulean.manifestation")
				));
			} else if (recipe instanceof AnxietyManifestationBrushRecipe real) {
				registry.addRecipe(new EmiBrushRecipe(id,
					List.of(of(Set.of(Color.VIRIDIAN), ALL_BRIGHTNESSES)),
					List.of(EmiIngredient.of(TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "tools")))),
					EmiStack.EMPTY,
					List.of(),
					List.of(),
					Text.translatable("info.cerulean.anxiety_manifestation")
				));
			} else if (recipe instanceof ParadigmBrushRecipe real) {
				registry.addRecipe(new EmiBrushRecipe(id,
					List.of(of(Set.of(real.color), ALL_BRIGHTNESSES)),
					List.of(EmiStack.of(real.source)),
					EmiStack.EMPTY,
					real.collateral.isEmpty() ? List.of(EmiStack.of(real.destination)) : List.of(EmiStack.of(real.destination), EmiStack.of(real.collateral)),
					List.of(real.source.getDefaultState()),
					Text.translatable("info.cerulean.paradigm")
				));
			} else if (recipe instanceof AgoraphobicGardeningBrushRecipe real) {
				registry.addRecipe(new EmiBrushRecipe(id,
					inputStars(real.canvas),
					List.of(),
					of(Set.of(real.color), real.wiltChance > 0 ? ALL_BRIGHTNESSES : ALL_BRIGHTNESSES_EXCEPT_BRILLIANT),
					List.of(),
					List.of(real.block.getDefaultState()),
					real.wiltChance > 0 ? Text.translatable("info.cerulean.agoraphobic_gardening") : Text.translatable("info.cerulean.agoraphobic_gardening_simple")
				));
			} else if (recipe instanceof CinderingAfterglowBrushRecipe real) {
				registry.addRecipe(new EmiBrushRecipe(id,
					inputStars(real.canvas),
					List.of(),
					of(real.canvas.validColors, (real.twicetwice && real.twicetwice) ? Set.of(Brightness.INNOCUOUS) : ALL_BRIGHTNESSES_EXCEPT_DIM),
					List.of(),
					List.of(real.canvas.validBlocks.iterator().next().getDefaultState()),
					Text.translatable("info.cerulean.cindering_afterglow" + ((real.twicetwice && real.twicetwice) ? "_twice" : ""))
				));
			} else if (recipe instanceof BevvyTastingBrushRecipe real) {
				registry.addRecipe(new EmiBrushRecipe(id,
					List.of(of(ALL_COLORS, ALL_BRIGHTNESSES_EXCEPT_BRILLIANT)),
					List.of(),
					of(ALL_COLORS, ALL_BRIGHTNESSES_EXCEPT_DIM),
					List.of(),
					List.of(Blocks.BREWING_STAND.getDefaultState()),
					Text.translatable("info.cerulean.bevvy_tasting")
				));
			} else if (recipe instanceof EmpathyBrushRecipe real) {
				registry.addRecipe(new EmiBrushRecipe(id,
					inputStars(real.canvas),
					List.of(EmiStack.of(CeruleanItems.STAMP)),
					EmiStack.EMPTY,
					List.of(),
					real.canvas.validBlocks.isEmpty() ? List.of() : List.of(real.canvas.validBlocks.iterator().next().getDefaultState()),
					Text.translatable("info.cerulean.empathy_" + (real.strict ? "stress" : "relief"))
				));
			} else if (recipe instanceof TriviaStainingRecipe real) {
				registry.addRecipe(new EmiBrushRecipe(id,
						inputStars(real.canvas),
						List.of(EmiStack.of(real.input)),
						EmiStack.EMPTY,
						List.of(EmiStack.of(real.output)),
						real.canvas.validBlocks.isEmpty() ? List.of() : List.of(real.canvas.validBlocks.iterator().next().getDefaultState()),
						Text.translatable(real.misremembered ? "info.cerulean.trivia_staining.misremembered" : "info.cerulean.trivia_staining")
				));
			} else if (recipe instanceof ImprintingRecipe real) {
				registry.addRecipe(new EmiBrushRecipe(id,
						inputStars(real.canvas),
						List.of(EmiStack.of(CeruleanItems.PHOTONEGATIVE), EmiStack.of(Items.PAPER)),
						EmiStack.EMPTY,
						List.of(EmiStack.of(CeruleanItems.PHOTOGRAPH)),
						List.of(real.canvas.validBlocks.iterator().next().getDefaultState()),
						Text.translatable("info.cerulean.imprinting")
				));
			} else if (recipe instanceof StrainingBrushRecipe real) {
				registry.addRecipe(new EmiBrushRecipe(id,
						inputStars(real.canvas),
						List.of(EmiStack.of(CeruleanItems.CORAL_STRAINER)),
						EmiStack.EMPTY,
						List.of(EmiStack.of(CeruleanItems.CORAL_STRAINER)),
						List.of(),
						Text.translatable("info.cerulean.straining")
				));
			} else {
				throw new IllegalStateException("Unhandled recipe kind: " + recipe.getClass().getSimpleName());
			}
		}

		registry.addRecipe(EmiWorldInteractionRecipe.builder()
				.id(Cerulean.id("/halite_slashing"))
				.leftInput(EmiStack.of(CeruleanBlocks.HALITE_BLOCK))
				.rightInput(EmiIngredient.of(ItemTags.SWORDS), true)
				.output(EmiStack.of(CeruleanBlocks.SLASHED_HALITE_BLOCK))
				.build()
		);

		registry.addRecipe(EmiWorldInteractionRecipe.builder()
				.id(Cerulean.id("/item_inductor_filtering"))
				.leftInput(EmiStack.of(CeruleanBlocks.ITEM_INDUCTOR))
				.rightInput(EmiIngredient.of(Ingredient.ofItems(CeruleanItems.CORAL_STRAINER)), false)
				.output(EmiStack.of(CeruleanBlocks.ITEM_INDUCTOR))
				.build()
		);

		registry.addRecipe(EmiWorldInteractionRecipe.builder()
				.id(Cerulean.id("/item_expeller_filtering"))
				.leftInput(EmiStack.of(CeruleanBlocks.ITEM_EXPELLER))
				.rightInput(EmiIngredient.of(Ingredient.ofItems(CeruleanItems.CORAL_STRAINER)), false)
				.output(EmiStack.of(CeruleanBlocks.ITEM_EXPELLER))
				.build()
		);

		registry.addRecipe(new PhotographicTransformationRecipe(Cerulean.id("/lustrous_photographic"),
				EmiStack.of(CeruleanBlocks.LUSTROUS_BLOCK), null));

		registry.addRecipe(new PhotographicTransformationRecipe(Cerulean.id("/revelation"),
				null, Text.translatable("info.cerulean.recipe.revelation")));

		registry.addRecipe(new StrongboxRipeningRecipe(Cerulean.id("/reflective_lens"), EmiStack.of(CeruleanItems.GLASS_LENS), EmiStack.of(CeruleanItems.REFLECTIVE_LENS)));
 	}

	public static List<EmiIngredient> compressy(List<EmiIngredient> stacks) {
		List<EmiIngredient> ret = Lists.newArrayList();
		EmiIngredient last = null;
		for (EmiIngredient s : stacks) {
			if (last != null && s.equals(last)) {
				last.setAmount(last.getAmount() + s.getAmount());
			} else {
				ret.add(s);
				last = s;
			}
		}
		return ret;
	}

	public static EmiStack of(FlowResource resource) {
		return EmiStack.of(CeruleanItems.STARS.get(resource));
	}

	public static List<EmiIngredient> inputStars(CanvasRequirements canvas) {
		List<EmiIngredient> ingredients = Lists.newArrayList();
		ingredients.add(of(canvas.validColors, canvas.validBrightnesses));
		if (!canvas.validOpposingColors.isEmpty()) {
			ingredients.add(of(canvas.validOpposingColors, canvas.validOpposingBrightnesses));
		}
		return ingredients;
	}

	public static EmiIngredient of(Set<Color> colors, Set<Brightness> brightnesses) {
		List<EmiStack> stacks = Lists.newArrayList();
		for (Color c : colors) {
			for (Brightness b : brightnesses) {
				stacks.add(of(FlowResources.star(c, b)));
			}
		}
		return EmiIngredient.of(stacks);
	}
}