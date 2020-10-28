package com.example.fitnessapp5;

import com.google.firebase.auth.FirebaseUser;

public class Users {

    String UserHeight ;
    String UserWeight ;
    String UserWeightGoal;
    String UserCalGoals;
    String UserEmail;

  /*  public void convert (boolean isImperial){

        double rUserHeight = Double.parseDouble(UserHeight);
        double rUserWeight = Double.parseDouble(UserWeight);
        double rUserWeightGoals = Double.parseDouble(UserWeightGoal);


        if (isImperial == true){
            Converter.feetToCm(rUserHeight);
            Converter.poundsToKg(rUserWeight);
            Converter.poundsToKg(rUserWeightGoals);

        }

        UserHeight = Double.toString(rUserHeight);
        UserWeight = Double.toString(rUserWeight);
        UserWeightGoal = Double.toString(rUserWeightGoals);
    }
*/

    public Users(){

    }

    public Users(String pUserHeight, String pUserWeight, String pUserWeightGoal, String pUserCalGoal,
                 String pUserEmail){

        UserHeight= pUserHeight;
        UserWeight = pUserWeight;
        UserWeightGoal = pUserWeightGoal;
        UserCalGoals = pUserCalGoal;
        UserEmail = pUserEmail;

    }

    public String getUserHeight() {
        return UserHeight;
    }

    public void setUserHeight(String userHeight) {
        UserHeight = userHeight;
    }

    public String getUserWeight() {
        return UserWeight;
    }

    public void setUserWeight(String userWeight) {
        UserWeight = userWeight;
    }

    public String getUserWeightGoal() {
        return UserWeightGoal;
    }

    public void setUserWeightGoal(String userWeightGoal) {
        UserWeightGoal = userWeightGoal;
    }

    public String getUserCalGoals() {
        return UserCalGoals;
    }

    public void setUserCalGoals(String userCalGoals) {
        UserCalGoals = userCalGoals;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String ToString (){


        return "Height: " + UserHeight + "Weight: " + UserWeight + "Weight Goals: " + UserWeightGoal
                + "Calorie Intake Goals: " + UserCalGoals + "User: " + UserEmail;
    }
}
