package dev.zihasz.zware.ui.rewrite;

import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.manager.ModuleManager;
import dev.zihasz.zware.utils.render.Quad;
import dev.zihasz.zware.utils.render.Renderer2D;
import dev.zihasz.zware.utils.render.TextRenderer;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class Frame implements Component {
	public static final int FRAME_WIDTH = 100;
	public static final int FRAME_HEIGHT = 20;
	public boolean open = true;
	public boolean dragging = false;
	private ArrayList<ModuleComponent> buttons;
	private Category c;
	private int x, y, dragX, dragY;
	private int offset;

	public Frame(int x, int y, Category c) {
		this.c = c;
		this.x = x;
		this.y = y;
		this.offset = 0;

		buttons = new ArrayList<>();
		for (Module mod : ModuleManager.getModules(c)) {
			buttons.add(new ModuleComponent(this, mod, offset));
			offset += ModuleComponent.MODULE_HEIGHT;
		}
	}

	@Override
	public void render(int x, int y) {
		Renderer2D.drawRect(
				this.x,
				this.y,
				this.x + FRAME_WIDTH,
				this.y + FRAME_HEIGHT,
				(hovered(x, y) ?
						ClickGUI.defaultScheme.foreground.brighter() :
						ClickGUI.defaultScheme.foreground)
		);
		TextRenderer.drawCenteredString(
				getCategoryName(),
				this.x,
				this.y,
				FRAME_WIDTH,
				FRAME_HEIGHT,
				ClickGUI.defaultScheme.font,
				true
		);
		offset = 0;

		if (open) {
			for (ModuleComponent b : buttons) {
				b.setOffset(offset);
				b.render(x, y);
				offset += b.height();
			}
		}
	}

	@Override
	public void update(int x, int y) {
		if (open)
			buttons.forEach(e -> e.update(x, y));
		if (dragging) {
			this.x = x - dragX;
			this.y = y - dragY;
		}
	}

	@Override
	public void mouseClicked(int x, int y, int button) {
		if (hovered(x, y)) {
			switch (button) {
				case 0:
					dragX = x - this.x;
					dragY = y - this.y;
					dragging = true;
					break;
				case 1:
					open = !open;
			}
		}
		offset = 0;
		for (ModuleComponent b : buttons) {
			b.setOffset(offset);
			offset += b.height();
		}

		if (open)
			buttons.forEach(e -> e.mouseClicked(x, y, button));
	}

	@Override
	public void mouseReleased(int x, int y, int button) {
		dragging = false;
		if (open)
			buttons.forEach(e -> e.mouseReleased(x, y, button));
	}

	@Override
	public void keyTyped(char typedChar, int button) {
		buttons.forEach(e -> e.keyTyped(typedChar, button));
	}

	@Override
	public int height() {
		return 0;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public void guiClosed() {
		buttons.forEach(ModuleComponent::guiClosed);
		dragging = false;
	}

	private boolean hovered(int x, int y) {
		return new Quad(this.x, this.y, this.x + FRAME_WIDTH, this.y + FRAME_HEIGHT).isWithinIncluding(x, y);
	}

	public ArrayList<ModuleComponent> getButtons() {
		return buttons;
	}
	public void setButtons(ArrayList<ModuleComponent> buttons) {
		this.buttons = buttons;
	}
	public Category getCategory() {
		return c;
	}
	public void setCategory(Category c) {
		this.c = c;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public String getCategoryName() {
		return StringUtils.capitalize(c.toString().toLowerCase());
	}

}
