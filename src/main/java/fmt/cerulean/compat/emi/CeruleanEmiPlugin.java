package fmt.cerulean.compat.emi;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import fmt.cerulean.Cerulean;
import fmt.cerulean.flow.FlowResource;
import fmt.cerulean.flow.FlowResource.Brightness;
import fmt.cerulean.flow.FlowResource.Color;
import fmt.cerulean.flow.FlowResources;
import fmt.cerulean.flow.recipe.AnxietyManifestationBrushRecipe;
import fmt.cerulean.flow.recipe.BerryFlavoringBrushRecipe;
import fmt.cerulean.flow.recipe.BevvyTastingBrushRecipe;
import fmt.cerulean.flow.recipe.BrushRecipe;
import fmt.cerulean.flow.recipe.BrushRecipes;
import fmt.cerulean.flow.recipe.CanvasRequirements;
import fmt.cerulean.flow.recipe.CinderingAfterglowBrushRecipe;
import fmt.cerulean.flow.recipe.InspirationBrushRecipe;
import fmt.cerulean.flow.recipe.ManifestationBrushRecipe;
import fmt.cerulean.flow.recipe.ParadigmBrushRecipe;
import fmt.cerulean.flow.recipe.AgoraphobicGardeningBrushRecipe;
import fmt.cerulean.flow.recipe.UnblightBrushRecipe;
import fmt.cerulean.registry.CeruleanBlocks;
import fmt.cerulean.registry.CeruleanItems;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CeruleanEmiPlugin implements EmiPlugin {
	private static final Set<Color> ALL_COLORS = Stream.of(Color.values()).collect(Collectors.toSet());
	private static final Set<Brightness> ALL_BRIGHTNESSES = Stream.of(Brightness.values()).collect(Collectors.toSet());
	private static final Set<Brightness> ALL_BRIGHTNESSES_EXCEPT_DIM = ALL_BRIGHTNESSES.stream().filter(b -> b != Brightness.DIM).collect(Collectors.toSet());
	private static final Set<Brightness> ALL_BRIGHTNESSES_EXCEPT_BRILLIANT = ALL_BRIGHTNESSES.stream().filter(b -> b != Brightness.BRILLIANT).collect(Collectors.toSet());
	private static final EmiIngredient ALL_STARS = of(Arrays.stream(Color.values()).collect(Collectors.toSet()), ALL_BRIGHTNESSES);
	public static final EmiRecipeCategory BRUSHING = new EmiRecipeCategory(Cerulean.id("brushing"), of(FlowResources.star(Color.CERULEAN, Brightness.BRILLIANT)));

	@Override
	public void register(EmiRegistry registry) {
		registry.removeEmiStacks(EmiStack.of(CeruleanBlocks.INKY_VOID));
		registry.addCategory(BRUSHING);
		registry.addWorkstation(BRUSHING, EmiStack.of(CeruleanBlocks.STAR_WELL));
		for (BrushRecipe recipe : Stream.concat(BrushRecipes.SOLO_RECIPES.stream(), BrushRecipes.DUAL_RECIPES.stream()).toList()) {
			Identifier id = BrushRecipes.GET_ID.get(recipe);
			if (recipe instanceof InspirationBrushRecipe real) {
				registry.addRecipe(new EmiBrushRecipe(id,
					inputStars(real.canvas),
					compressy(real.input.stream().map(EmiIngredient::of).toList()),
					EmiStack.EMPTY,
					List.of(EmiStack.of(real.output)),
					List.of(),
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
					// TODO: make custom tool tag
					List.of(EmiIngredient.of(ItemTags.PICKAXES)),
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
			}
		}
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