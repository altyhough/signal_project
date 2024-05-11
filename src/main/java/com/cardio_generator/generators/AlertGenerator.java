package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;


public class AlertGenerator implements PatientDataGenerator {

    public static final Random randomGenerator = new Random();

    //change to camelCase
    private boolean[] alertStates; // false = resolved, true = pressed


    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1];
    }
    //comment to public method missing
    //Generates random data for the patient and output, handles error if generating the data fails
    //@param patientID is the ID of the patient
    //@param outputStrategy handles alert data
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (alertStates[patientId]) {
                if (randomGenerator.nextDouble() < 0.9) { // 90% chance to resolve
                    alertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {

                //Lambda should be lambda since its a variable
                double lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
                double p = -Math.expm1(-lambda); // Probability of at least one alert in the period
                boolean alertTriggered = randomGenerator.nextDouble() < p;

                if (alertTriggered) {
                    alertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}
