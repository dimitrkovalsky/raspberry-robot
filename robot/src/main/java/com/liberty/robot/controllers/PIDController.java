package com.liberty.robot.controllers;

import utils.RobotParams;

/**
 * Created by Dmytro_Kovalskyi on 03.12.2015.
 */
public class PIDController {
    private static float DESIRED_DISTANCE_TO_WALL = 50f;
    private float proportionalMultiplier = 1f;
    private float differentialMultiplier = 1f;
    private float integralMultiplier = 1f;


    public void move(float steering, float distance) {
        steering = evaluateSteering(steering);
        distance = evaluateDistance(distance);

    }

    private float evaluateDistance(float distance) {
        return distance < 0 ? 0 : distance;
    }

    private float evaluateSteering(float steering) {
        float currentSteer;
        if(steering < RobotParams.MIN_STEERING_ANGLE) {
            currentSteer = RobotParams.MIN_STEERING_ANGLE;
        } else if(steering > RobotParams.MAX_STEERING_ANGLE) {
            currentSteer = RobotParams.MAX_STEERING_ANGLE;
        } else {
            currentSteer = steering;
        }
        return currentSteer;
    }

    private float calculateCTE(float currentDistance) {
        return currentDistance - DESIRED_DISTANCE_TO_WALL;
    }
}
