package com.nimbus.engine;

import java.awt.image.DataBufferInt;
import java.util.ArrayList;

import com.nimbus.engine.graphics.font.NimbusFont;
import com.nimbus.engine.graphics.image.Image;
import com.nimbus.engine.graphics.image.ImageJob;
import com.nimbus.engine.graphics.light.Light;
import com.nimbus.engine.graphics.light.LightJob;

public class Renderer {

	private NimbusSettings settings = NimbusSettings.INSTANCE;
	private NimbusFont font = NimbusFont.DEFAULT_FONT;
	private ArrayList<LightJob> lightJobs = new ArrayList<LightJob>();
	private ImageJob imageJobStack = null;
	
	private int pixelWidth;
	private int pixelHeight;
	private int[] pixels;
	private int[] zBuffer;
	private int[] lightMap;
	private int[] lightBlock;
	
	private int ambientColour = -1;
	private int zDepth = 0;
	private float alphaMod = 1f;
	private boolean processing = false;
	private int cameraX;
	private int cameraY;
	
	
	public Renderer(Nimbus nimbus) {
		pixelWidth = settings.WIDTH;
		pixelHeight = settings.HEIGHT;
		pixels = ((DataBufferInt) nimbus.getWindow().getImage().getRaster().getDataBuffer()).getData();
		zBuffer = new int[pixels.length];
		lightMap = new int[pixels.length];
		lightBlock = new int[pixels.length];
	}
	
	public void clear() {
		//Set all pixels to black!
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = 0xff000000;
			zBuffer[i] = 0xff000000;
			lightMap[i] = ambientColour;
			lightBlock[i] = Light.FULL;
		}
	}
	
	public void process() {
		processing = true;
		
		//The jobs need to be rendered in a certain order. Use a linked list to do this efficiently
		while(imageJobStack != null) {
			drawImage(imageJobStack.image, imageJobStack.offsetX, imageJobStack.offsetY, imageJobStack.zDepth);
			imageJobStack = imageJobStack.next;
		}
		
		//Draw lighting (We don't need to sort this, so an ArrayList is fine)
		for(int i = 0; i < lightJobs.size(); i++) {
			LightJob lightJob = lightJobs.get(i);
			drawLightJob(lightJob.getLight(), lightJob.getLocationX(), lightJob.getLocationY());
		}
		
		//After rendering all of our images and processing we will merge our pixel map and light map
		for(int i = 0; i < pixels.length; i++) {
			int light = lightMap[i];
			int pixel = pixels[i];
			float lightRed = getRed(light) / 255f;
			float lightGreen = getGreen(light) / 255f;
			float lightBlue = getBlue(light) / 255f;
			pixels[i] = ((int) (getRed(pixel) * lightRed) << 16 | (int) (getGreen(pixel) * lightGreen) << 8 | (int) (getBlue(pixel) * lightBlue));
		}
		
		lightJobs.clear();
		processing = false;
	}
	
	private void addImageJob(ImageJob imageJob) {
		if(imageJobStack == null) {
			imageJobStack = imageJob;
			return;
		}
		else {
			imageJobStack.setNext(imageJob);
		}
	}
	
	public void setPixel(int x, int y, int value) {
		//An alpha channel of 0 means transparent, 255 means solid
		//We then need to apply the current renderer alpha mod
		float alpha = (getAlphaChannel(value) / 255) - (1f - alphaMod);
		
		if(x < 0 || x >= pixelWidth || y < 0 || y >= pixelHeight) {
			return;
		}
		
		//0xffff00ff is considered transparent as well (good ol' super pink)
		if(value == 0xffff00ff || alpha == 0) {
			lightBlock[x + y * pixelWidth] = Light.FULL;
			lightMap[x + y * pixelWidth] = ambientColour;
			return;
		}
		
		int index = x + y * pixelWidth;
		
		if(zBuffer[index] > zDepth) {
			return;
		}
		
		zBuffer[index] = zDepth;
		
		//if the new pixel is solid alpha, just set it
		if(alpha == 1) {
			pixels[index] = value;
		}
		//Otherwise we need to blend the alpha channels
		else {
			int pixel = pixels[index];
			
			int newRed = getRed(pixel) - (int) ((getRed(pixel) - getRed(value)) * alpha);
			int newGreen = getGreen(pixel) - (int) ((getGreen(pixel) - getGreen(value)) * alpha);
			int newBlue = getBlue(pixel) - (int) ((getBlue(pixel) - getBlue(value)) * alpha);
			int newPixel = (newRed << 16 | newGreen << 8 | newBlue);
			
			pixels[index] = newPixel;		
		}
		
	}
	
	public void setLightMap(int x, int y, int value) {
		if(x < 0 || x >= pixelWidth || y < 0 || y >= pixelHeight) {
			return;
		}
		
		int baseColour = lightMap[x + y * pixelWidth];
		
		int maxRed = Math.max(getRed(baseColour), getRed(value));
		int maxGreen = Math.max(getGreen(baseColour), getGreen(value));
		int maxBlue = Math.max(getBlue(baseColour), getBlue(value));
		
		lightMap[x + y * pixelWidth] = (maxRed << 16 | maxGreen << 8 | maxBlue);				
	}
	
	public void setLightBlock(int x, int y, int value) {
		if(x < 0 || x >= pixelWidth || y < 0 || y >= pixelHeight) {
			return;
		}
		if(zBuffer[x + y * pixelWidth] > zDepth) {
			return;
		}
		
		lightBlock[x + y * pixelWidth] = value;				
	}
	
	public void drawImage(Image image, int offsetX, int offsetY, int zDepth) {
		//Change the offset to be relative to the camera rather than the canvas
		offsetX -= cameraX;
		offsetY -= cameraY;
		
		this.zDepth = zDepth;

		if(image.isAlpha() && !processing) {
			addImageJob(new ImageJob(image, zDepth, offsetX + cameraX, offsetY + cameraY));
			return;
		}
		
		//Don't render if the entire image is outside the frame
		if(!evaluateDrawOperation(offsetX, offsetY, image.getWidth(), image.getHeight())){
			return;
		}

		//Clip the image if it is partly outside the frame
		//Where in the image should we start drawing?
		int adjustedX = getAdjustedCoordinate(offsetX);
		int adjustedY = getAdjustedCoordinate(offsetY);
		//Where in the image should we stop drawing?
		int adjustedWidth = getAdjustedMeasurement(image.getWidth(), offsetX, pixelWidth);
		int adjustedHeight = getAdjustedMeasurement(image.getHeight(), offsetY, pixelHeight);
		
		//Draw the adjusted image
		for(int y = adjustedY; y < adjustedHeight; y++) {
			for(int x = adjustedX; x < adjustedWidth; x++) {
				int value = image.getPixels()[x + y * image.getWidth()];
				setLightBlock(x + offsetX, y + offsetY, image.getLightBlock());
				setPixel(x + offsetX, y + offsetY, value);
				if(value != 0xffff00ff && getAlphaChannel(value) != 0 && image.getLightBlock() == Light.SELF_ILLUMINATED) {
					setLightMap(x + offsetX, y + offsetY, -1);
				}
			}
		}
	}
	
	public void drawRectangle(int offsetX, int offsetY, int width, int height, int colour, int zDepth) {
		offsetX -= cameraX;
		offsetY -= cameraY;
		
		this.zDepth = zDepth;

		for(int y = 0; y <= height; y++) {
			setPixel(offsetX, y + offsetY, colour);
			setPixel(offsetX + width, y + offsetY, colour);
		}
		for(int x = 0; x <= width; x++) {
			setPixel(x + offsetX, offsetY, colour);
			setPixel(x + offsetX, offsetY + height, colour);
		}
	}
	
	public void drawFilledRectangle(int offsetX, int offsetY, int width, int height, int colour, int zDepth) {
		offsetX -= cameraX;
		offsetY -= cameraY;
		
		this.zDepth = zDepth;
		
		//Don't render if the entire image is outside the frame
		if(!evaluateDrawOperation(offsetX, offsetY, width, height)){
			return;
		}

		//Clip the image if it is partly outside the frame		
		int adjustedX = getAdjustedCoordinate(offsetX);
		int adjustedY = getAdjustedCoordinate(offsetY);
		int adjustedWidth = getAdjustedMeasurement(width, offsetX, pixelWidth);
		int adjustedHeight = getAdjustedMeasurement(height, offsetY, pixelHeight);
		
		for(int y = adjustedY; y < adjustedHeight; y++) {
			for(int x = adjustedX; x < adjustedWidth; x++) {
				setPixel(x + offsetX, y + offsetY, colour);
			}
		}
	}
	
	public void drawText(String text, int offsetX, int offsetY, int colour, int zDepth) {
		offsetX -= cameraX;
		offsetY -= cameraY;
		
		this.zDepth = zDepth;
		
		int unicode = 0;
		int nextOffset = 0;
		for (int i = 0; i < text.length(); i++) {
			unicode = text.codePointAt(i);
			Image characterImage = font.getCharacter(unicode);
			for(int y = 0; y < characterImage.getHeight(); y++) {
				for(int x = 0; x < characterImage.getWidth(); x++) {
					if(characterImage.getPixels()[x + y * characterImage.getWidth()] == 0xffffffff) {
						//if its white that means its a font character
						setPixel(x + offsetX + nextOffset, y + offsetY, colour);
					}
				}
			}
			nextOffset += font.getCharacter(unicode).getWidth();
		}
	}
	
	//Check if the image is completely outside the frame
	private boolean evaluateDrawOperation(int offsetX, int offsetY, int width, int height) {
		if(offsetX < -width) {return false;}
		if(offsetY < -height) {return false;}
		if(offsetX >= pixelWidth) {return false;}
		if(offsetY >= pixelHeight) {return false;}
		return true;
	}
	
	public void drawLight(Light light, int offsetX, int offsetY) {
		lightJobs.add(new LightJob(light, offsetX, offsetY));
	}
	
	//We need the offsets to calculate our screen position
	private void drawLightJob(Light light, int offsetX, int offsetY) {
		offsetX -= cameraX;
		offsetY -= cameraY;

		for(int i = 0; i <= light.getDiameter(); i++) {
			drawLightLine(light, light.getRadius(), light.getRadius(), i, 0, offsetX, offsetY); //Will move across the top of the light
			drawLightLine(light, light.getRadius(), light.getRadius(), i, light.getDiameter(), offsetX, offsetY); //Bottom
			drawLightLine(light, light.getRadius(), light.getRadius(), 0, i, offsetX, offsetY); //Left
			drawLightLine(light, light.getRadius(), light.getRadius(), light.getDiameter(), i, offsetX, offsetY); //Right
		}
	}
	
	private void drawLightLine(Light light, int xStart, int yStart, int xEnd, int yEnd, int offsetX, int offsetY) {
		int deltaX = Math.abs(xEnd - xStart);
		int deltaY = Math.abs(yEnd - yStart);
		
		//Slightly modified Bresenham's line algorithm... (basically we can draw a line with only ints and no floats)
		
		int sX = xStart < xEnd ? 1 : -1;
		int sY = yStart < yEnd ? 1 : -1;
		
		int err = deltaX - deltaY;
		int err2;
		
		boolean blocked = false;
		
		while(true) {
			int screenX = xStart - light.getRadius() + offsetX;
			int screenY = yStart - light.getRadius() + offsetY;
			
			if(screenX < 0 || screenX >= pixelWidth || screenY < 0 || screenY >= pixelHeight) {
				return;
			}
			
			int lightColour = light.getLightValue(xStart, yStart);
			if(lightColour == 0) {
				return;
			}
			
			int lightMode = lightBlock[screenX + screenY * pixelWidth];
			
			if(lightMode == Light.BLOCKED) {
				return;
			}
			else if(lightMode == Light.ABSORBED) {
				blocked = true;
				setLightMap(screenX, screenY, light.getLightMap()[xStart + yStart * light.getDiameter()]);
			}
			else if(lightMode == Light.SELF_ILLUMINATED) {
				//Do nothing
			}
			else if(lightMode == Light.IGNORE) {
				setLightMap(screenX, screenY, ambientColour);
			}
			else if(blocked){
				
			}
			else { // if (Light.FULL) {
				setLightMap(screenX, screenY, light.getLightMap()[xStart + yStart * light.getDiameter()]);	
			}			
			
			if(xStart == xEnd && yStart == yEnd) {
				break;
			}
			err2 = 2 * err;
			if(err2 > -1 * deltaY) {
				err -= deltaY;
				xStart += sX;
			}
			if(err2 < deltaX) {
				err += deltaX;
				yStart += sY;
			}
		}
	}
	
	private int getAlphaChannel(int colour) {
		return (colour >> 24) & 0xff;
	}
	
	private int getRed(int colour) {
		return (colour >> 16) & 0xff;
	}
	
	private int getGreen(int colour) {
		return (colour >> 8) & 0xff;
	}
	
	private int getBlue(int colour) {
		return colour & 0xff;
	}
	
	private int getAdjustedCoordinate(int offset) {
		if(offset < 0) {
			return 0 - offset;
		}
		return 0;
	}
		
	private int getAdjustedMeasurement(int measurement, int offset, int availableSpace) {
		int adjustedMeasurement = measurement;
		if(measurement + offset >= availableSpace) {
			adjustedMeasurement -= adjustedMeasurement + offset - availableSpace;
		}
		return adjustedMeasurement;		
	}
		
	public int getzDepth() {
		return zDepth;
	}

	public void setzDepth(int zDepth) {
		this.zDepth = zDepth;
	}

	public int getAmbientColour() {
		return ambientColour;
	}

	public void setAmbientColour(int ambientColour) {
		this.ambientColour = ambientColour;
	}

	public int getCameraX() {
		return cameraX;
	}

	public void setCameraX(int cameraX) {
		this.cameraX = cameraX;
	}

	public int getCameraY() {
		return cameraY;
	}

	public void setCameraY(int cameraY) {
		this.cameraY = cameraY;
	}

	public float getAlphaMod() {
		return alphaMod;
	}

	public void setAlphaMod(float alphaMod) {
		this.alphaMod = alphaMod;
	}

	public NimbusFont getFont() {
		return font;
	}

	public void setFont(NimbusFont font) {
		this.font = font;
	}
	
}
