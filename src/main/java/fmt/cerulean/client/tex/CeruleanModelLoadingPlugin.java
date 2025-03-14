package fmt.cerulean.client.tex;

import fmt.cerulean.Cerulean;
import fmt.cerulean.client.render.item.EyeOfVenderer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;

public class CeruleanModelLoadingPlugin implements ModelLoadingPlugin {
	@Override
	public void onInitializeModelLoader(Context context) {
		EyeOfVenderer.registerModels(context);
		for (int i = 0; i < 8; i++) {
			context.addModels(
				Cerulean.id("item/cubie/blue_" + i),
				Cerulean.id("item/cubie/green_" + i),
				Cerulean.id("item/cubie/gray_" + i)
			);
		}
	}
}
