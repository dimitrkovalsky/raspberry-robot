package com.liberty.robot.model;

import utils.RobotParams;

/**
 * User: Dimitr
 * Date: 07.12.2015
 * Time: 15:45
 */
public class Robot {
    private byte steering;
    private float speed;
    private int positionX;
    private int positionY;

    public byte getSteering() {
        return steering;
    }

    public void setSteering(byte steering) {
        if (steering > RobotParams.MAX_STEERING_ANGLE)
            steering = RobotParams.MAX_STEERING_ANGLE;
        else if (steering < RobotParams.MIN_STEERING_ANGLE)
            steering = RobotParams.MIN_STEERING_ANGLE;
        this.steering = steering;
    }

    public void setSteering(int steering) {
        setSteering((byte) steering);
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }
}
