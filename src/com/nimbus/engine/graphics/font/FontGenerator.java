package com.nimbus.engine.graphics.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.nimbus.engine.graphics.image.Image;

public class FontGenerator {

	public static NimbusFont getFont(String fontName, int size, int type) {
		if (!isFontExistsInEnv(fontName)) {
			System.out.println("No such font: " + fontName);
			return NimbusFont.DEFAULT_FONT;
		}
		Font font = new Font(fontName, type, size);

		BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = image.createGraphics();

		int width = calculateFontWidth(font);
		FontRenderContext fontRenderContext = graphics2D.getFontRenderContext();
		String alphabet = createAlphabet();
		GlyphVector glyphVector = font.createGlyphVector(fontRenderContext, alphabet);
		Rectangle bounds = glyphVector.getPixelBounds(null, 0, 0);
		int height = bounds.height + 1;
		size = (size + calculateMaximumFontSize(font, fontRenderContext)) / 2;
		return new NimbusFont(createFontImage(font, width, height, size));
	}

	private static boolean isFontExistsInEnv(String fontName) {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Font[] fonts = env.getAllFonts();
		for (Font f : fonts) {
			if (f.getName().equalsIgnoreCase(fontName)) {
				return true;
			}
		}
		return false;
	}

	private static int calculateFontWidth(Font font) {
		int width = 0;
		FontMetrics metrics = new JPanel().getFontMetrics(font);
		for (char i = 0; i < NimbusFont.FONT_CHAR_RANGE; i++) {
			width += metrics.charWidth(i) + 2;
		}
		return width;
	}

	private static String createAlphabet() {
		StringBuilder stringBuilder = new StringBuilder(NimbusFont.FONT_CHAR_RANGE);
		for (char i = 0; i < NimbusFont.FONT_CHAR_RANGE; i++) {
			stringBuilder.append(i);
		}
		return stringBuilder.toString();
	}

	private static int calculateMaximumFontSize(Font font, FontRenderContext fontRenderContext) {
		int maximumSize = 0;
		for (int i = 0; i < NimbusFont.FONT_CHAR_RANGE; i++) {
			GlyphVector glyphVector = font.createGlyphVector(fontRenderContext, String.valueOf(i));
			maximumSize = Math.max(glyphVector.getPixelBounds(null, 0, 0).height, maximumSize);
		}
		return maximumSize;
	}

	private static Image createFontImage(Font f, int width, int height, int fontSize) {
		BufferedImage fontImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = fontImage.createGraphics();
		graphics2D.setColor(new Color(0, true));
		graphics2D.fillRect(0, 0, width, height);
		int x = 0; 
		int y = 0;
		graphics2D.setFont(f);
		FontMetrics metrics = new JPanel().getFontMetrics(f);
		for (char character = 0; character < NimbusFont.FONT_CHAR_RANGE; character++) {
			y = 0;
			graphics2D.setColor(new Color(0f, 0f, 1f, 1f));
			graphics2D.drawLine(x, y, x, y);
			x++;
			
			y = 1;
			graphics2D.setColor(Color.WHITE);
			graphics2D.drawString(String.valueOf(character), x, y + fontSize);
			x += metrics.charWidth(character);
			
			y = 0;
			graphics2D.setColor(new Color(1f, 1f, 0f, 1f));
			graphics2D.drawLine(x, y, x, y);
			x++;
		}
		int[] pixels = fontImage.getRGB(0, 0, width, height, null, 0, width);
		
		return new Image(pixels, width, height);
	}

}
