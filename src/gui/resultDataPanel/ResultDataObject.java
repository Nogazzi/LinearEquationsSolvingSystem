package gui.resultDataPanel;

import gui.baseDataPanel.MatrixA;

/**
 * Created by Nogaz on 10.02.2017.
 */
public class ResultDataObject {
    protected double[][] matrixA;
    protected double[] matrixB;
    protected double[] resultX;
    protected String methodName;
    protected double precision;
    protected double parameter;
    protected int iterationsNumber;
    protected double computationsTime;
    protected boolean completed;
    protected String reason;
    protected int matrixLevel;

    public ResultDataObject(){

    }

    public void setMethodName(String methodName){
        this.methodName = methodName;
    }
    public void setMatrixA(double[][] matrixA){
        this.matrixA = matrixA;
    }
    public void setMatrixB(double[] matrixB){
        this.matrixB = matrixB;
    }
    public void setResultX(double[] resultX){
        this.resultX = resultX;
    }
    public void setPrecision(double precision){
        this.precision = precision;
    }
    public void setParameter(double parameter){
        this.parameter = parameter;
    }
    public void setIterationsNumber(int iterationsNumber){
        this.iterationsNumber = iterationsNumber;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    public void setReason(String reason){
        this.reason = reason;
    }
    public String getMethodName(){
        return this.methodName;
    }
    public double[] getMatrixB() {
        return matrixB;
    }
    public double[] getResultX() {
        return resultX;
    }
    public int getIterationsNumber() {
        return iterationsNumber;
    }
    public double getParameter() {
        return parameter;
    }
    public double[][] getMatrixA() {
        return matrixA;
    }
    public double getPrecision() {
        return precision;
    }
    public int getMatrixLevel() {
        return matrixLevel;
    }
    public void setMatrixLevel(int matrixLevel) {
        this.matrixLevel = matrixLevel;
    }
    public boolean isCompleted() {
        return completed;
    }
    public String getReason() {
        return reason;
    }
    public double getComputationsTime() {
        return computationsTime;
    }
    public void setComputationsTime(double computationsTime) {
        this.computationsTime = computationsTime;
    }
}
