package dev.zihasz.zware.ui.clickgui.component;

public interface Component {

	void draw();
	void click(int mX, int mY, int button);
	void press(int button, char keyChar);

}
