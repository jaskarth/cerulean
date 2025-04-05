package fmt.cerulean.command;

import com.mojang.brigadier.CommandDispatcher;
import fmt.cerulean.world.data.PhotoState;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collection;

public class ClearPhotosCommand {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(CommandManager.literal("cerulean:clearphotos")
			.requires(source -> source.hasPermissionLevel(2))
			.then(CommandManager.argument("targets", EntityArgumentType.entities())
				.executes(context -> execute(context.getSource(), EntityArgumentType.getEntities(context, "targets")))
		));
	}

	private static int execute(ServerCommandSource source, Collection<? extends Entity> targets) {
		for (Entity target : targets) {
			if (target instanceof ServerPlayerEntity spe) {
				PhotoState.get(source.getServer().getOverworld()).clearFor(spe);
				source.sendFeedback(() -> Text.literal("Cleared photos."), false);
			} else {
				source.sendFeedback(() -> Text.literal("Not a player."), false);
			}
		}

		return targets.size();
	}
}
