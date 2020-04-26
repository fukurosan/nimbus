package com.nimbus.engine.graphics.image;

public class ImageJob {
	
	public Image image;
	public int zDepth;
	public int offsetX;
	public int offsetY;
	
	public ImageJob next = null;
	
	public ImageJob(Image image, int zDepth, int offsetX, int offsetY) {
		this.image = image;
		this.zDepth = zDepth;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}
	
	//Sort the linked list continuously
	public void setNext(ImageJob imageRequest) {
		if(next == null) {
			next = imageRequest;
			return;
		}
		if(imageRequest.zDepth > next.zDepth) {
			imageRequest.next = next;
			next = imageRequest;
		}
		else {
			next.setNext(imageRequest);
		}
	}
}
