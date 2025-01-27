package fmt.cerulean.client.tex;

import fmt.cerulean.client.render.item.EyeOfVenderer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;

public class CeruleanModelLoadingPlugin implements ModelLoadingPlugin {
	@Override
	public void onInitializeModelLoader(Context context) {
		EyeOfVenderer.registerModels(context);
	}
}
