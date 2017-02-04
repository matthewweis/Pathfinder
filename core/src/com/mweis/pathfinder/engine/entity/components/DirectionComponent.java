package com.mweis.pathfinder.engine.entity.components;

import com.badlogic.ashley.core.Component;

public class DirectionComponent implements Component {
	private float angrad; // conform to java's standards in angles
	private float angdeg;
	
	public float getDegrees() {
		return angdeg;
	}
	
	public float getRadians() {
		return angrad;
	}
	
	public void setAngleDegrees(float angdeg) {
		this.angdeg = angdeg;
		this.angrad = (float) Math.toRadians(angdeg);
	}
	
	public void setAngleRadians(float angrad) {
		this.angdeg = (float) Math.toDegrees(angrad);
		this.angrad = angrad;
	}
	
	public DirectionComponent() {
		angrad = 0.0f;
	}
	
	public DirectionComponent(float angrad) {
		this.angrad = angrad;
	}
}