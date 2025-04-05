package fmt.cerulean.client.screen;

import java.util.List;

import fmt.cerulean.Cerulean;
import fmt.cerulean.client.ClientState;
import fmt.cerulean.net.packet.WinPacket;
import fmt.cerulean.solitaire.Board;
import fmt.cerulean.solitaire.Card;
import fmt.cerulean.solitaire.CardPos;
import fmt.cerulean.solitaire.CardStack;
import fmt.cerulean.solitaire.Suit;
import fmt.cerulean.util.Vec2i;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class SolitaireScreen extends Screen {
	public static final Identifier TEXTURE = Cerulean.id("textures/gui/solitaire.png");
	public static final int CARD_WIDTH = 25;
	public static final int CARD_HEIGHT = 40;
	private static Board board;
	private Vec2i holdOffset = new Vec2i(0, 0);
	private static long lastShuffle = System.currentTimeMillis();
	private static long lastSeed = -1;
	private static long lastWin = 0;

	private Rect2i restartRect;
	private Rect2i winsRect;

	public SolitaireScreen() {
		super(Text.literal("Solitaire"));
		if (ClientState.seed == -1) {
			requestDeal();
		}
	}

	@Override
	protected void init() {
		restartRect = new Rect2i(width - 72, height - 22, 70, 20);
		winsRect = new Rect2i(2, height - 22, 70, 20);
	}

	public void requestDeal() {
		ClientPlayNetworking.send(new WinPacket(-1, List.of()));
		board = null;
	}

	public void newBoard() {
		lastSeed = ClientState.seed;
		board = new Board(lastSeed);
		lastShuffle = System.currentTimeMillis();
	}

	@Override
	public boolean shouldPause() {
		return false;
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (this.client.options.inventoryKey.matchesKey(keyCode, scanCode)) {
			this.close();
			return true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (board != null) {
			Pair<CardPos, Vec2i> pair = getCardUnder((int) mouseX, (int) mouseY);
			if (pair != null) {
				board.pickup(pair.getLeft());
				holdOffset = pair.getRight();
				return true;
			} else if (restartRect.contains((int) mouseX, (int) mouseY) && System.currentTimeMillis() > lastShuffle + 10_000) {
				requestDeal();
				return true;
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}

	@Override 
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (board != null) {
			Pair<CardPos, Vec2i> pair = getCardUnder((int) mouseX, (int) mouseY);
			if (pair != null) {
				CardPos pos = pair.getLeft();
				board.drop(pos);
			} else {
				board.drop(null);
			}
		}
		System.out.println(board.hasWon());
		if (lastWin == 0 && board.hasWon()) {
			ClientPlayNetworking.send(new WinPacket(lastSeed, board.moves));
			lastWin = System.currentTimeMillis();
		}
		return super.mouseReleased(mouseX, mouseY, button);
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		renderDarkening(context);

		long now = System.currentTimeMillis();
		long diff = now - lastShuffle;
		if (diff > 9_000) {
			int alpha = 0xaa;
			if (diff < 16_000) {
				alpha = (int) (alpha * (diff - 9_000) / (16_000 - 9_000));
			}
			if (alpha > 32) {
				drawBoxedText(context, Text.literal("Restart"), (alpha << 24) | 0xffffff, true, restartRect);
			}
		}

		drawBoxedText(context, Text.literal("Wins: " + ClientState.wins), 0xaaffffff, true, winsRect);

		context.fill(width / 2, 0, width / 2 + 1, height, 0xaaffffff);

		if (lastSeed != ClientState.seed) {
			newBoard();
		}

		if (board == null) {
			return;
		}

		int cx = width / 2 - CARD_WIDTH / 2;
		int top = Math.max(0, (height - 260) / 2);
		for (int i = 0; i < Board.ROWS; i++) {
			renderStack(context, mouseX, mouseY, board.arts.get(i), cx - i * 30 - CARD_WIDTH, top);
			renderStack(context, mouseX, mouseY, board.dream.get(i), cx + i * 30 + CARD_WIDTH, top);
		}
		if (board.pickup != null) {
			context.getMatrices().push();
			context.getMatrices().translate(0, 0, 200);
			int amx = mouseX - holdOffset.x();
			if (board.pickup.type() == CardPos.Type.ARTS) {
				amx = Math.min(amx, cx - CARD_WIDTH / 2 - 1);
			} else {
				amx = Math.max(amx, cx + CARD_WIDTH / 2 - 1);
			}
			renderStack(context, mouseX, mouseY, board.held, amx, mouseY - holdOffset.z());
			renderStack(context, mouseX, mouseY, board.mirroredHeld, cx + (cx - amx), mouseY - holdOffset.z());
			context.getMatrices().pop();
		}
	}

	public void renderStack(DrawContext context, int mouseX, int mouseY, CardStack stack, int x, int y) {
		context.getMatrices().push();
		for (int i = 0; i < stack.cards.size(); i++) {
			Card card = stack.cards.get(i);
			drawFace(context, card, x, y + i * 12);
			context.getMatrices().translate(0, 0, 1);
		}
		context.getMatrices().pop();
	}

	public void drawFace(DrawContext context, Card card, int x, int y) {
		Suit suit = card.suit();
		float hue = suit.h / 360f;
		int textColor = 0xff000000 | MathHelper.hsvToRgb(hue, suit.s / 2, 1);
		int border = 0xff000000 | MathHelper.hsvToRgb(hue, suit.s, suit.v);
		int background = 0xff000000 | MathHelper.hsvToRgb(hue, suit.s / 4, suit.v / 6);
		context.fill(x, y, x + CARD_WIDTH, y + CARD_HEIGHT, background);
		context.drawBorder(x, y, CARD_WIDTH, CARD_HEIGHT, border);
		Text text = Text.literal("" + card.rank());
		context.getMatrices().push();
		context.getMatrices().translate(x, y, 0);
		context.drawText(textRenderer, text, 2, 2, textColor, true);
		context.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
		context.drawText(textRenderer, text, -23, -CARD_HEIGHT + 2, textColor, true);
		context.getMatrices().pop();
		int u = card.suit().ordinal() * 16;
		int v = 0;
		if (u >= 48) {
			u -= 48;
			v = 16;
		}
		context.setShaderColor(((textColor >> 16) & 0xff) / 255f, ((textColor >> 8) & 0xff) / 255f, ((textColor >> 0) & 0xff) / 255f, 1);
		context.drawTexture(TEXTURE, x + (CARD_WIDTH - 16) / 2 + 1, y + (CARD_HEIGHT - 16) / 2, u, v, 16, 16);
		context.setShaderColor(1, 1, 1, 1);
	}

	public Pair<CardPos, Vec2i> getCardUnder(int x, int y) {
		int cx = width / 2 - CARD_WIDTH / 2;
		int top = Math.max(0, (height - 260) / 2);
		for (int i = 0; i < Board.ROWS; i++) {
			CardStack stack = null;
			int dx = cx + i * 30 + CARD_WIDTH;
			int ox = cx - i * 30 - CARD_WIDTH;
			int offsetX = -1;
			CardPos.Type type = null;
			if (x >= dx && x <= dx + CARD_WIDTH) {
				type = CardPos.Type.DREAM;
				stack = board.dream.get(i);
				offsetX = x - dx;
			} else if (x >= ox && x <= ox + CARD_WIDTH) {
				type = CardPos.Type.ARTS;
				stack = board.arts.get(i);
				offsetX = x - ox;
			}
			if (type != null) {
				int depth = -1;
				Vec2i offset = null;
				if (stack.cards.isEmpty() && y > top && y < top + CARD_HEIGHT) {
					depth = 0;
					offset = new Vec2i(0, 0);
				}
				for (int cy = 0; cy < stack.cards.size(); cy++) {
					int dy = top + cy * 12;
					if (y >= dy && y <= dy + CARD_HEIGHT) {
						depth = cy;
						offset = new Vec2i(offsetX, y - dy);
					}
				}
				if (depth != -1) {
					return new Pair<CardPos, Vec2i>(new CardPos(type, i, depth), offset);
				}
			}
		}
		return null;
	}

	public void drawBoxedText(DrawContext context, Text text, int color, boolean center, Rect2i rect) {
		int x = rect.getX();
		int y = rect.getY();
		int width = rect.getWidth();
		int height = rect.getHeight();
		context.fill(x + 1, y, x + width - 1, y + 1, color);
		context.fill(x + 1, y + height, x + width - 1, y + height - 1, color);
		context.fill(x, y, x + 1, y + height, color);
		context.fill(x + width, y, x + width - 1, y + height, color);
		if (center) {
			int tx = x + width / 2;
			int ty = y + (height - 8) / 2;
			context.drawCenteredTextWithShadow(textRenderer, text, tx, ty, color);
		} else {
			int tx = x + 6;
			int ty = y + (height - 8) / 2;
			context.drawText(textRenderer, text, tx, ty, color, true);
		}
	}
}
