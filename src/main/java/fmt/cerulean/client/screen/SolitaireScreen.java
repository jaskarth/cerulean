package fmt.cerulean.client.screen;

import fmt.cerulean.solitaire.Board;
import fmt.cerulean.solitaire.Card;
import fmt.cerulean.solitaire.CardPos;
import fmt.cerulean.solitaire.CardStack;
import fmt.cerulean.solitaire.Suit;
import fmt.cerulean.util.Vec2i;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;

public class SolitaireScreen extends Screen {
	public static final int CARD_WIDTH = 24;
	public static final int CARD_HEIGHT = 40;
	private static Board board;
	private Vec2i holdOffset = new Vec2i(0, 0);

	public SolitaireScreen() {
		super(Text.literal("Solitaire"));
		board = new Board(System.currentTimeMillis());
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		Pair<CardPos, Vec2i> pair = getCardUnder((int) mouseX, (int) mouseY);
		if (pair != null) {
			board.pickup(pair.getLeft());
			holdOffset = pair.getRight();
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		Pair<CardPos, Vec2i> pair = getCardUnder((int) mouseX, (int) mouseY);
		if (pair != null) {
			CardPos pos = pair.getLeft();
			board.drop(pos);
		} else {
			board.drop(null);
		}
		return super.mouseReleased(mouseX, mouseY, button);
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		renderDarkening(context);

		int cx = width / 2 - CARD_WIDTH / 2;
		int top = Math.max(0, (height - 260) / 2);
		context.fill(width / 2 - 1, 0, width / 2 + 1, height, 0xaaffffff);
		for (int i = 0; i < Board.ROWS; i++) {
			renderStack(context, mouseX, mouseY, board.arts.get(i), cx - i * 30 - 24, top);
			renderStack(context, mouseX, mouseY, board.dream.get(i), cx + i * 30 + 24, top);
		}
		if (board.pickup != null) {
			context.getMatrices().push();
			context.getMatrices().translate(0, 0, 200);
			int amx = mouseX - holdOffset.x();
			if (board.pickup.type() == CardPos.Type.ARTS) {
				amx = Math.min(amx, cx - CARD_WIDTH / 2);
			} else {
				amx = Math.max(amx, cx + CARD_WIDTH / 2);
			}
			renderStack(context, mouseX, mouseY, board.held, amx, mouseY - holdOffset.z());
			renderStack(context, mouseX, mouseY, board.mirroredHeld, cx + (cx - amx), mouseY - holdOffset.z());
			context.getMatrices().pop();
		}
	}

	public void renderStack(DrawContext context, int mouseX, int mouseY, CardStack stack, int x, int y) {
		for (int i = 0; i < stack.cards.size(); i++) {
			Card card = stack.cards.get(i);
			drawFace(context, card, x, y + i * 12);
		}
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
		int tw = textRenderer.getWidth(text);
		context.drawText(textRenderer, "" + card.rank(), x + 22 - tw, y + 2, textColor, true);
	}

	public Pair<CardPos, Vec2i> getCardUnder(int x, int y) {
		int cx = width / 2 - CARD_WIDTH / 2;
		int top = Math.max(0, (height - 260) / 2);
		for (int i = 0; i < Board.ROWS; i++) {
			CardStack stack = null;
			int dx = cx + i * 30 + 24;
			int ox = cx - i * 30 - 24;
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
}
