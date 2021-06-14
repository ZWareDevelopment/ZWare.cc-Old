package dev.zihasz.zware.ui.clickgui.component;

public interface Component {

	void draw(int x, int y);
	void click(int x, int y, int button);
	void press(int code, char character);

}
