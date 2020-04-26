package com.nimbus.engine.util;

public class Noise {

	public float[] GenerateOctavedSimplexNoise(int width, int height, int octaves, float roughness, float layerFrequency) {
		float[] resultNoise = new float[width * height];
		float layerWeight = 1;
		for (int octave = 0; octave < octaves; octave++) {
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					resultNoise[x + y * height] += (float) SimplexNoise.noise(x * layerFrequency, y * layerFrequency) * layerWeight;
				}
			}
			layerFrequency *= 2;
			layerWeight *= roughness;
		}
		return resultNoise;
	}
	
	//Example: float[] noise = Noise.GenerateOctavedSimplexNoise(512, 512, 3, 0.4f, 0.005f);
}
