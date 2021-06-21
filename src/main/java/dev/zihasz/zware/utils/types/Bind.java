package dev.zihasz.zware.utils.types;

import org.lwjgl.input.Keyboard;

public class Bind {

	public int code;
	public char character;

	public Bind(int code, char character) {
		this.code = code;
		this.character = character;
	}

	public Bind(int code) {
		this.code = code;
		this.character = (char) Keyboard.CHAR_NONE;
	}

	public int getCode() { return code; }
	public char getCharacter() { return character; }

	public void setCode(int code) { this.code = code; }
	public void setCharacter(char character) { this.character = character; }

}
