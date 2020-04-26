package com.nimbus.engine.graphics.font;

import java.awt.Font;
import java.util.ArrayList;

import com.nimbus.engine.graphics.image.Image;

public class NimbusFont {
	
	public static final NimbusFont DEFAULT_FONT = FontGenerator.getFont("consolas", 12, Font.PLAIN);
	public static final int FONT_CHAR_RANGE = 256;
	
	private Image[] characterImages;
	private int height;
	
	public NimbusFont(Image fontImage) {
		int charStartIndex = 0;
		ArrayList<Image> tempCharacterImages = new ArrayList<Image>();
		for (int i = 0; i < fontImage.getWidth(); i++) {
			if (fontImage.getPixels()[i] == 0xff0000ff) {
				charStartIndex = i;
			} 
			else if (fontImage.getPixels()[i] == 0xffffff00) {
				int width = i - charStartIndex;
				int[] characterPixels = new int[width * (fontImage.getHeight() - 1)];
				for (int y = 0; y < fontImage.getHeight() - 1; y++) {
					for (int x = charStartIndex; x < charStartIndex + width; x++) {
						characterPixels[(x - charStartIndex) + y * width] = fontImage.getPixels()[x + (y + 1) * fontImage.getWidth()];
					}
				}
				tempCharacterImages.add(new Image(characterPixels, width, fontImage.getHeight() - 1));
			}
		}
		characterImages = new Image[tempCharacterImages.size()];
		tempCharacterImages.toArray(characterImages);
		height = getCharacter(0).getHeight();
	}
	
	public Image getCharacter(int unicode) {
		if (unicode > characterImages.length || unicode < 0) {
			return characterImages[0];
		} 
		else {
			return characterImages[unicode];
		}
	}
	
	public int getTextWidth(String text) {
		int result = 0;
		for(int i = 0; i < text.length(); i++) {
			result += getCharacter(text.codePointAt(i)).getWidth();
		}
		return result;
	}

	public int getHeight() {
		return height;
	}

}
