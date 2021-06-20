package dev.zihasz.zware.ui.rewrite.component;

public interface Component {

	void render(int x, int y);
	void update(int x, int y);
	void mouseClicked(int x, int y, int button);
	void mouseReleased(int x, int y, int button);
	void keyTyped(char typedChar, int button);
	int height();
	String getDescription();
	void guiClosed();

}
