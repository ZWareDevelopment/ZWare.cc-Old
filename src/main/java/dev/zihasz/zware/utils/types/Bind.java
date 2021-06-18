package dev.zihasz.zware.utils.types;

public class Bind {

	public int code;
	public char character;

	public Bind(int code, char character) {
		this.code = code;
		this.character = character;
	}

	public int getCode() { return code; }
	public char getCharacter() { return character; }

	public void setCode(int code) { this.code = code; }
	public void setCharacter(char character) { this.character = character; }

}
