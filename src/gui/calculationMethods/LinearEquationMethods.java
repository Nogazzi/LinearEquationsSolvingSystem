package gui.calculationMethods;

import Jama.LUDecomposition;
import Jama.Matrix;
import gui.baseDataPanel.AvailableMethodsPanel;
import gui.resultDataPanel.ResultDataObject;

/**
 * Created by Nogaz on 28.01.2017.
 */
public class LinearEquationMethods {
    public static String COMMENT_ACCEPT = "accept";
    public static String COMMENT_FAIL = "fail";


    public ResultDataObject metodaJacobiego(double[][] matrixA, double[] matrixB, double precisionParameter, int maxIterationsNumber){
        long startTime = System.currentTimeMillis();
        long endTime;
        ResultDataObject resultDataObject = new ResultDataObject();
        resultDataObject.setMethodName(AvailableMethodsPanel.JacobiMethod);
        resultDataObject.setMatrixA(copyMatrix(matrixA));
        resultDataObject.setMatrixB(copyMatrix(matrixB));
        resultDataObject.setPrecision(precisionParameter);
        resultDataObject.setMatrixLevel(matrixB.length);


        double[][] matrixL = getLowMAtrix(matrixA);
        double[][] matrixU = getUpperMAtrix(matrixA);
        double[][] matrixD = getDiagonalMAtrix(matrixA);
        //matrixD = opositeMatrix(matrixD);   //d = D^-1
        try {
            matrixD = opositeMatrix(matrixD);
        } catch (PeculiarMatrixException e) {
            e.printStackTrace();
            resultDataObject.setCompleted(false);
            resultDataObject.setReason("Macierz jest osobliwa");
            endTime = System.currentTimeMillis();
            resultDataObject.setComputationsTime(endTime-startTime);
            return resultDataObject;
        }
        double[] matrixN = matrixMultiplication(matrixD, matrixB);
        double[][] matrixM = matrixMultiplication(matrixD, -1);
        double[][] matrixLsumU = addMatrix(matrixL, matrixU);
        matrixM = matrixMultiplication(matrixM, matrixLsumU);

        double[] resultMatrix = new double[matrixB.length];
        double[] tmpResultMatrix = new double[matrixB.length];
        for( int i = 0 ; i < resultMatrix.length ; ++i ){
            resultMatrix[i] = 0;
            tmpResultMatrix[i] = 0;
        }
        boolean preciseEnough = false;
        double difference;
        int iterations = 1;
        while( preciseEnough == false && iterations < maxIterationsNumber ){
            resultMatrix = copyMatrix(tmpResultMatrix);
            //reprintMatrix(tmpResultMatrix, resultMatrix);
            System.out.println("Iteracja: " + iterations);
            System.out.println("Wejsciowa macierz M:");
            printMatrix(matrixM);
            System.out.println("Wejsciowa macierz x^n:");
            printMatrix(resultMatrix);
            System.out.println("Wejsciowa macierz N:");
            printMatrix(matrixN);

            tmpResultMatrix = matrixMultiplication(matrixM, tmpResultMatrix);
            tmpResultMatrix = addMatrix(tmpResultMatrix, matrixN);

            //spr wielkosc bledu
            preciseEnough = true;
            System.out.println("aktualny stan wyniku:");
            for(int i = 0 ; i < tmpResultMatrix.length ; ++i){
                System.out.println(tmpResultMatrix[i]);
            }
            for( int i = 0 ; i < resultMatrix.length ; ++i ){
                if( Math.abs(resultMatrix[i] - tmpResultMatrix[i]) > precisionParameter ){
                    System.out.println("Nadal zbyt duzy blad");
                    preciseEnough = false;
                }
            }
            iterations++;
        }
        printMatrix(resultMatrix);

        resultDataObject.setCompleted(true);
        resultDataObject.setIterationsNumber(iterations);
        resultDataObject.setResultX(copyMatrix(resultMatrix));
        endTime = System.currentTimeMillis();
        resultDataObject.setComputationsTime(endTime-startTime);
        return resultDataObject;
    }



    public ResultDataObject metodaGaussaSeidla(double[][] matrixA, double[] matrixB, double precisionParameter, int maxIterationsNumber){
        long startTime = System.currentTimeMillis();
        long endTime;
        ResultDataObject resultDataObject = new ResultDataObject();
        resultDataObject.setMethodName(AvailableMethodsPanel.GaussaSeidla);
        resultDataObject.setMatrixA(copyMatrix(matrixA));
        resultDataObject.setMatrixB(copyMatrix(matrixB));
        resultDataObject.setPrecision(precisionParameter);
        resultDataObject.setMatrixLevel(matrixB.length);

        double[][] matrixL = getLowMAtrix(matrixA);
        double[][] matrixU = getUpperMAtrix(matrixA);
        double[][] matrixD = getDiagonalMAtrix(matrixA);
        double[][] samePartMatrix = addMatrix(matrixD, matrixL);
        try {
            samePartMatrix = opositeMatrix(samePartMatrix); //w postaci (D+L)^-1
        } catch (PeculiarMatrixException e) {
            e.printStackTrace();
            resultDataObject.setCompleted(false);
            resultDataObject.setReason("Macierz jest osobliwa");
            endTime = System.currentTimeMillis();
            resultDataObject.setComputationsTime(endTime-startTime);
            return resultDataObject;
        }
        if( matrixDiagonalDomination(matrixA) ){
            System.out.println("Macierz A jest diagonalnie dominująca.");
        }else{
            System.out.println("Macierz A NIE jest diagonalnie dominująca.");
            System.out.println("Metoda Jacobiego nie będzie zbieżna.");
            resultDataObject.setCompleted(false);
            resultDataObject.setReason("Macierz nie jest diagonalnie dominująca");
            endTime = System.currentTimeMillis();
            resultDataObject.setComputationsTime(endTime-startTime);
            return resultDataObject;
        }
        if( peculiar(matrixA) ){
            System.out.println("Macierz jest osobliwa!");
            resultDataObject.setCompleted(false);
            resultDataObject.setReason("Macierz jest osobliwa");
            endTime = System.currentTimeMillis();
            resultDataObject.setComputationsTime(endTime-startTime);
            return resultDataObject;
        }

        //przygotowanie macierzy M
        double[] matrixN = matrixMultiplication(samePartMatrix, matrixB);
        double[][] matrixM = matrixMultiplication(samePartMatrix, -1.0);
        matrixM = matrixMultiplication(matrixM, matrixU);


        //obliczenia iteracyjne

        double[] resultMatrix;
        double[] tmpMat = new double[matrixB.length];

        double[] rightEquation;
        boolean enough = false;
        double[] actualRes = new double[matrixB.length];
        int iterations = 0;
        for( int i = 0 ; i < tmpMat.length ; ++i ){
            tmpMat[i] = 0;
            actualRes[i] = 0;
        }
        while( enough == false && iterations < maxIterationsNumber  ){
            actualRes = copyMatrix(tmpMat);
            //reprintMatrix(tmpMat, actualRes);
            tmpMat = matrixMultiplication(matrixM, tmpMat);
            tmpMat = addMatrix(tmpMat, matrixN);
            enough = true;
            for( int i = 0 ; i < tmpMat.length ; ++i ){
                if( Math.abs(tmpMat[i] - actualRes[i]) > precisionParameter ){
                    enough = false;
                }
                System.out.println("Roznica wyrazen w iteracji : " +( iterations+1) + " " + Math.abs(tmpMat[i] - actualRes[i]));
            }
            iterations++;
            System.out.println("Iteracja " + iterations);
            printMatrix(tmpMat);
        }
        resultMatrix = tmpMat;

        System.out.println("Wynik obliczeń:");
        printMatrix(resultMatrix);
        System.out.println("Potrzebna liczba iteracji: " + iterations);

        resultDataObject.setIterationsNumber(iterations);
        resultDataObject.setResultX(copyMatrix(resultMatrix));
        resultDataObject.setCompleted(true);
        endTime = System.currentTimeMillis();
        resultDataObject.setComputationsTime(endTime-startTime);
        return resultDataObject;
    }

    public ResultDataObject metodaSOR(double[][] matrixA, double[] matrixB, double wspolczynnik, double precisionParameter, int maxIterationsNumber){
        long startTime = System.currentTimeMillis();
        long endTime;
        ResultDataObject resultDataObject = new ResultDataObject();
        resultDataObject.setMethodName(AvailableMethodsPanel.SOR);
        resultDataObject.setMatrixA(copyMatrix(matrixA));
        resultDataObject.setMatrixB(copyMatrix(matrixB));
        resultDataObject.setPrecision(precisionParameter);
        resultDataObject.setParameter(wspolczynnik);
        resultDataObject.setMatrixLevel(matrixB.length);

        double[][] matrixL = getLowMAtrix(matrixA);
        double[][] matrixU = getUpperMAtrix(matrixA);
        double[][] matrixD = getDiagonalMAtrix(matrixA);

        double[][] matrixM;
        double[] matrixN;
        double[][] sharedPart = matrixMultiplication(matrixL, wspolczynnik);
        sharedPart = addMatrix(sharedPart, matrixD);
        try {
            sharedPart = opositeMatrix(sharedPart);
        } catch (PeculiarMatrixException e) {
            e.printStackTrace();
            resultDataObject.setCompleted(false);
            resultDataObject.setReason("Macierz jest osobliwa");
            endTime = System.currentTimeMillis();
            resultDataObject.setComputationsTime(endTime-startTime);
            return resultDataObject;
        }
        //macierz shared juz gotowa

        //macierz M
        matrixM = matrixMultiplication(matrixD, (1.0d-wspolczynnik));
        double[][] tmpM = matrixMultiplication(matrixU, wspolczynnik);
        matrixM = substractMatrix(matrixM, tmpM);
        matrixM = matrixMultiplication(sharedPart, matrixM);


        //macierz N
        double[][] tmpMat = matrixMultiplication(sharedPart, wspolczynnik);
        matrixN = matrixMultiplication(tmpMat, matrixB);


        if( matrixDiagonalDomination(matrixA) ){
            System.out.println("Macierz A jest diagonalnie dominująca.");
        }else{
            System.out.println("Macierz A NIE jest diagonalnie dominująca.");
            System.out.println("Metoda Jacobiego nie będzie zbieżna.");
            resultDataObject.setCompleted(false);
            resultDataObject.setReason("Macierz nie jest diagonalnie dominująca");
            endTime = System.currentTimeMillis();
            resultDataObject.setComputationsTime(endTime-startTime);
            return resultDataObject;
        }
        if( peculiar(matrixA) ){
            System.out.println("Macierz jest osobliwa!");
            resultDataObject.setCompleted(false);
            resultDataObject.setReason("Macierz jest osobliwa!");
            endTime = System.currentTimeMillis();
            resultDataObject.setComputationsTime(endTime-startTime);
            return resultDataObject;
        }
        //obliczenia iteracyjne

        double[] resultMatrix = new double[matrixB.length];

        double[] rightEquation;
        boolean enough = false;
        double[] actualRes = new double[matrixB.length];
        int iterations = 1;
        for( int i = 0 ; i < resultMatrix.length ; ++i ){
            resultMatrix[i] = 0;
            actualRes[i] = 0;
        }
        while( enough == false && iterations < maxIterationsNumber ){
            actualRes = copyMatrix(resultMatrix);
            System.out.println("\nIteracja " + iterations);
            System.out.println("Macierz wejsciowa M:");
            printMatrix(matrixM);
            System.out.println("Macierz wejsciowa x^n:");
            printMatrix(resultMatrix);
            System.out.println("Macierz wejsciowa N:");
            printMatrix(matrixN);
            resultMatrix = matrixMultiplication(matrixM, resultMatrix);
            resultMatrix = addMatrix(resultMatrix, matrixN);
            enough = true;
            for( int i = 0 ; i < resultMatrix.length ; ++i ){
                if( Math.abs(resultMatrix[i] - actualRes[i]) > precisionParameter ){
                    enough = false;
                }
                //System.out.println("Roznica wyrazen w iteracji : " +( iterations+1) + " " + Math.abs(resultMatrix[i] - actualRes[i]));
            }
            iterations++;

            //printMatrix(resultMatrix);
        }

        System.out.println("Wynik obliczeń:");
        printMatrix(resultMatrix);
        System.out.println("Potrzebna liczba iteracji: " + iterations);

        resultDataObject.setIterationsNumber(iterations);
        resultDataObject.setResultX(copyMatrix(resultMatrix));
        resultDataObject.setCompleted(true);
        endTime = System.currentTimeMillis();
        resultDataObject.setComputationsTime(endTime-startTime);
        return resultDataObject;
    }

    public ResultDataObject metodaEliminacjiGaussa(double[][] matrixA, double[] matrixB){
        long startTime = System.currentTimeMillis();
        long endTime;
        ResultDataObject resultDataObject = new ResultDataObject();
        resultDataObject.setMethodName(AvailableMethodsPanel.ElimGausSeidl);
        resultDataObject.setMatrixA(copyMatrix(matrixA));
        resultDataObject.setMatrixB(copyMatrix(matrixB));
        resultDataObject.setMatrixLevel(matrixB.length);

        int n = matrixA.length;
        double[][] tmpMatrixA = copyMatrix(matrixA);
        double[] tmpMatrixB = copyMatrix(matrixB);

        //eliminacja do przodu
        for( int k = 0 ; k < n ; ++k ){
            for( int i = k+1 ; i < n ; ++i ){
                if( tmpMatrixA[k][k] == 0 ){
                    resultDataObject.setCompleted(false);
                    resultDataObject.setReason("Dzielenie przez 0");
                    endTime = System.currentTimeMillis();
                    resultDataObject.setComputationsTime(endTime-startTime);
                    return resultDataObject;
                }
                double alfa = tmpMatrixA[i][k] / tmpMatrixA[k][k];

                for( int j = k ; j < n ; ++j ){
                    tmpMatrixA[i][j] -= alfa * tmpMatrixA[k][j];
                }
                tmpMatrixB[i] -= alfa * tmpMatrixB[k];
            }
        }

        //podstawienie wstecz
        for( int i = n-1 ; i >= 0 ; --i ){
            for( int j = i+1 ; j < n ; ++j ){
                tmpMatrixB[i] -= tmpMatrixA[i][j] * tmpMatrixB[j];
            }
            if( tmpMatrixA[i][i] == 0 ){
                resultDataObject.setCompleted(false);
                resultDataObject.setReason("Dzielenie przez 0");
                endTime = System.currentTimeMillis();
                resultDataObject.setComputationsTime(endTime-startTime);
                return resultDataObject;
            }
            tmpMatrixB[i] /= tmpMatrixA[i][i];
        }

        System.out.println("Wyniki obliczeń:");
        printMatrix(tmpMatrixB);

        resultDataObject.setResultX(copyMatrix(tmpMatrixB));
        resultDataObject.setCompleted(true);
        endTime = System.currentTimeMillis();
        resultDataObject.setComputationsTime(endTime-startTime);
        return resultDataObject;
    }

    public ResultDataObject metodaRozkladuLU(double[][] matrixA, double[] matrixB){
        long startTime = System.currentTimeMillis();
        long endTime;
        ResultDataObject resultDataObject = new ResultDataObject();
        resultDataObject.setMethodName(AvailableMethodsPanel.LUdecomposition);
        resultDataObject.setMatrixA(copyMatrix(matrixA));
        resultDataObject.setMatrixB(copyMatrix(matrixB));
        resultDataObject.setMatrixLevel(matrixB.length);

        int n = matrixA.length;
        double[][] tmpMatrixU = copyMatrix(matrixA);
        double[] tmpMatrixB = copyMatrix(matrixB);
        double[][] tmpMatrixL = copyMatrix(matrixA);


        //eliminacja do przodu
        for( int k = 0 ; k < n ; ++k ){
            for( int i = k+1 ; i < n ; ++i ){
                if( tmpMatrixU[k][k] == 0 ){
                    resultDataObject.setCompleted(false);
                    resultDataObject.setReason("Dzielenie przez 0");
                    endTime = System.currentTimeMillis();
                    resultDataObject.setComputationsTime(endTime-startTime);
                    return resultDataObject;
                }
                double alfa = tmpMatrixU[i][k] / tmpMatrixU[k][k];
                double tmpL = matrixA[i][k];
                for( int j = k ; j < n ; ++j ){

                    tmpMatrixU[i][j] -= alfa * tmpMatrixU[k][j];
                    //tmpL -=  matrixA[j][k]*tmpMatrixA[k][i];
                    //tmpMatrixL[i][k] -=  alfa// / tmpMatrixA[k][k];
                }
                tmpMatrixL[i][k] =  alfa;//tmpL / tmpMatrixA[k][k];
            }
        }
        for(int i = 0 ; i < n ; i++ ){
            for( int j = 0 ; j < n ; ++j ){
                if( i < j ){
                    tmpMatrixL[i][j] = 0;
                }else if( i == j ){
                    tmpMatrixL[i][j] = 1;
                }
            }
        }

        //Ly = b
        double[] resultY = copyMatrix(matrixB);
        //podstawianie wprzod
        for( int i = 0 ; i < n ; ++i ){
            for( int j = 0 ; j < i ; ++j ){
                resultY[i] -= tmpMatrixL[i][j] * resultY[j];
            }
            if( tmpMatrixL[i][i] == 0 ){
                resultDataObject.setCompleted(false);
                resultDataObject.setReason("Dzielenie przez 0");
                endTime = System.currentTimeMillis();
                resultDataObject.setComputationsTime(endTime-startTime);
                return resultDataObject;
            }
            resultY[i] /= tmpMatrixL[i][i];
        }

        //Ux = y
        double[] resutlX = copyMatrix(resultY);
        //podstawianie wstecz
        for( int i = n-1 ; i >= 0 ; --i ){
            for( int j = i+1 ; j < n ; ++j ){
                resutlX[i] -= tmpMatrixU[i][j] * resutlX[j];
            }
            if( tmpMatrixU[i][i] == 0 ){
                resultDataObject.setCompleted(false);
                resultDataObject.setReason("Dzielenie przez 0");
                endTime = System.currentTimeMillis();
                resultDataObject.setComputationsTime(endTime-startTime);
                return resultDataObject;
            }
            resutlX[i] /= tmpMatrixU[i][i];
        }

        System.out.println("Wyniki obliczeń:");
        System.out.println("Macierz U");
        printMatrix(tmpMatrixU);
        System.out.println("Macierz L");
        printMatrix(tmpMatrixL);
        System.out.println("Macierz result Y");
        printMatrix(resultY);
        System.out.println("Macierz result X");
        printMatrix(resutlX);

        resultDataObject.setResultX(copyMatrix(resutlX));
        resultDataObject.setCompleted(true);
        endTime = System.currentTimeMillis();
        resultDataObject.setComputationsTime(endTime-startTime);
        return resultDataObject;
    }



    public void printMatrix(double[][] matrix) {

        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[0].length; ++j) {
                if (j == 0) {
                    System.out.println();
                }
                System.out.print(matrix[i][j] + " \t");
            }
        }
        System.out.println();
    }
    public void printMatrix(double[] matrix){
        for (int i = 0; i < matrix.length; ++i) {
            System.out.println(matrix[i]);
        }
    }
    public void reprintMatrix(double[] matrixFrom, double[] matrixTo){
        for( int i = 0 ; i < matrixFrom.length ; ++i ){
            matrixTo[i] = matrixFrom[i];
        }
    }

    double[][] getDiagonalMAtrix(double[][] matrix){
        double[][] resultMatrix = new double[matrix.length][matrix[0].length];
        for( int i = 0 ; i < matrix.length ; ++i ) {
            for (int j = 0; j < matrix.length; ++j) {
                if( i > j ){
                    //ponizej gornej przekatnej
                    resultMatrix[i][j] = 0;
                }else if( i < j ){
                    //powyzej gornej przekatnej
                    resultMatrix[i][j] = 0;
                }else{
                    //glowna przekatna
                    resultMatrix[i][j] = matrix[i][j];
                }
            }
        }
        return resultMatrix;
    }
    double[][] getUpperMAtrix(double[][] matrix){
        double[][] resultMatrix = new double[matrix.length][matrix[0].length];
        for( int i = 0 ; i < matrix.length ; ++i ) {
            for (int j = 0; j < matrix.length; ++j) {
                if( i > j ){
                    //ponizej gornej przekatnej
                    resultMatrix[i][j] = 0;
                }else if( i < j ){
                    //powyzej gornej przekatnej
                    resultMatrix[i][j] = matrix[i][j];
                }else{
                    //glowna przekatna
                    resultMatrix[i][j] = 0;
                }
            }
        }
        return resultMatrix;
    }
    double[][] getLowMAtrix(double[][] matrix){
        double[][] resultMatrix = new double[matrix.length][matrix[0].length];
        for( int i = 0 ; i < matrix.length ; ++i ) {
            for (int j = 0; j < matrix.length; ++j) {
                if( i > j ){
                    //ponizej gornej przekatnej
                    resultMatrix[i][j] = matrix[i][j];
                }else if( i < j ){
                    //powyzej gornej przekatnej
                    resultMatrix[i][j] = 0;
                }else{
                    //glowna przekatna
                    resultMatrix[i][j] = 0;
                }
            }
        }
        return resultMatrix;
    }

    public double[][] transposedMatrix(double[][] matrix){
        double[][] transposed = new double[matrix.length][matrix[0].length];
        for( int i = 0 ; i < transposed.length ; ++i ){
            for( int j = 0 ; j < transposed[0].length ; ++j ){
                transposed[i][j] = matrix[j][i];
            }
        }
        return transposed;
    }

    public double[][] opositeMatrix(double[][] matrix) throws PeculiarMatrixException {
        double[][] opositeMatrix = new double[matrix.length][matrix[0].length];

        //oblicz wyznacznik detA
        double detA = wybierzIObliczWyznacznik(matrix);

        System.out.println("Wyznacznik macierzy = " + detA);
        if( detA == 0 ){
            //nie istnieje macierz odwrotna
            System.out.println("Nie istnieje macierz odwrotna, sorki");
            throw new PeculiarMatrixException();
        }
        System.out.println("Możemy liczyć macierz odwrotną");

        //wyznacz dopelnienia algebraiczne wszystkich wyrazow A
        double[][] algebraicAdditionMatrix = produceAlgebraicAdditionMatrix(matrix);

        //transponowanie macierzy dopelnien
        double[][] transposedAlgebraicAdditionMatrix = transposedMatrix(algebraicAdditionMatrix);

        //mnozenie macierzy dopelnien przez 1/detA
        opositeMatrix = matrixMultiplication(transposedAlgebraicAdditionMatrix, 1/detA);

        return opositeMatrix;
    }

    public boolean matrixDiagonalDomination(double[][] matrix){
        double rowSum = 0;
        double diagonalNumber = 0;
        for( int i = 0 ; i < matrix.length ; ++i ){
            rowSum = 0;
            for( int j = 0 ; j < matrix[0].length ; ++j ){
                if( i==j ){
                    diagonalNumber = Math.abs(matrix[i][j]);
                }else{
                    rowSum += Math.abs(matrix[i][j]);
                }
            }
            if( diagonalNumber < rowSum ){
                return false;
            }
        }
        return true;
    }

    public double[][] produceAlgebraicAdditionMatrix(double[][] matrix){
        double[][] resultMatrix = new double[matrix.length][matrix[0].length];
        for( int i = 0 ; i < resultMatrix.length ; ++i ){
            for( int j = 0 ; j < resultMatrix[0].length ; ++j ){
                resultMatrix[i][j] = Math.pow(-1, i+j) * wybierzIObliczWyznacznik(getAlgebraicAdditionsOfElements(matrix, i, j));
            }
        }
        return resultMatrix;
    }

    public double[][] getAlgebraicAdditionsOfElements(double[][] matrix, int i, int j){
        double[][] resultMatrix = new double[matrix.length-1][matrix[0].length-1];

        int resultI = 0;
        int resultJ = 0;
        for( int y = 0 ; y < matrix.length ; ++y ){
            resultJ = 0;
            if( y != i ) {
                for (int x = 0; x < matrix[0].length; ++x) {
                    if (x != j) {
                        resultMatrix[resultI][resultJ] = matrix[y][x];
                        resultJ++;
                    }
                }
                resultI++;
            }

        }

        printMatrix(resultMatrix);

        return resultMatrix;
    }

    public double[][] opositeDiagonalMatrix(double[][] matrix){
        double[][] resultMatrix = new double[matrix.length][matrix[0].length];
        for( int i = 0 ; i < matrix.length ; ++i ){
            for( int j = 0 ; j < matrix[0].length ; ++j ){
                if( i==j ){
                    resultMatrix[i][j] = 1/matrix[i][j];
                }else{
                    resultMatrix[i][j] = 0;
                }
            }
        }
        return resultMatrix;
    }

    public double[][] divideMatrix(double[][] matrix, double divider){
        double[][] result = new double[matrix.length][matrix[0].length];
        return result;
    }

    public boolean peculiar(double[][] matrix){
        double det = 0;
        double stopienWyznacznika = matrix.length;
        double[][] tmpMatrix = new double[matrix.length][matrix.length];
        for( int i = 0 ; i < matrix.length ; ++i ){
            for( int j = 0 ; j < matrix.length ; ++j ){
                tmpMatrix[i][j] = matrix[i][j];
            }
        }
        //utworzenie macierzy trojkatnej gornej
        for( int k = 0 ; k < stopienWyznacznika-1 ; ++k ){
            for( int i = k+1 ; i < stopienWyznacznika ; ++i ){
                for(int j = (int) (stopienWyznacznika-1); j >= k ; --j ){
                    tmpMatrix[i][j] = tmpMatrix[i][j] - tmpMatrix[i][k]*tmpMatrix[k][j]/tmpMatrix[k][k];
                }
            }
        }

        //obliczenie wyznacznika
        det = 1;
        for( int i = 0 ; i < stopienWyznacznika ; ++i ){
            det *= tmpMatrix[i][i];
        }
        System.out.println("METHOD_NAME: \'Peculiar\' ,Wyznacznik stopnia " + stopienWyznacznika + " ma wartość: " + det);
        if( det == 0 ){
            return true;    //macierz osobliwa, det == 0
        }
        return false;
    }

    public double obliczWyznacznik(double[][] matrix){
        double det = 0;
        double stopienWyznacznika = matrix.length;
        double[][] tmpMatrix = new double[matrix.length][matrix.length];
        for( int i = 0 ; i < matrix.length ; ++i ){
            for( int j = 0 ; j < matrix.length ; ++j ){
                tmpMatrix[i][j] = matrix[i][j];
            }
        }
        //utworzenie macierzy trojkatnej gornej
        for( int k = 0 ; k < stopienWyznacznika-1 ; ++k ){
            for( int i = k+1 ; i < stopienWyznacznika ; ++i ){
                for(int j = (int) (stopienWyznacznika-1); j >= k ; --j ){
                    if( tmpMatrix[k][k] == 0 ){
                        System.out.println("Dzielenie przez 0!!!!!");
                        return 0;
                    }
                    tmpMatrix[i][j] = tmpMatrix[i][j] - tmpMatrix[i][k]*tmpMatrix[k][j]/tmpMatrix[k][k];
                }
            }
        }

        //obliczenie wyznacznika
        det = 1;
        for( int i = 0 ; i < stopienWyznacznika ; ++i ){
            det *= tmpMatrix[i][i];
        }
        System.out.println("METHOD_NAME: \'obliczWyznacznik\' ,Wyznacznik stopnia " + stopienWyznacznika + " ma wartość: " + det);
        return det;
    }

    public double wyznacznik(double[][] matrix){
        double result = 0;
        int lastIndex = matrix.length-1;
        int matrixLength = matrix.length;
        if( matrixLength == 2 ){
            result = matrix[0][0]*matrix[1][1] - matrix[1][0]*matrix[0][1];
            return result;
        }

        for( int j = 0 ; j < matrix.length ; ++j ){
            double left = 1;
            double right = 1;
            for( int i = 0 ; i < matrix[0].length ; ++i ){
                left *= matrix[i][(i+j)%matrixLength];
            }
            for( int i = lastIndex ; i >= 0 ; --i ){
                right *= matrix[i][(j+lastIndex-i)%matrixLength];
            }
            //System.out.println("Lewy[" + j + "] = " + left);
            //System.out.println("Prawy[" + j + "] = " + right);
            result += left;
            result -= right;

        }
        System.out.println("Wyznacznik: " + result);
        //result = left-right;
        return result;
    }

    public double wybierzIObliczWyznacznik(double[][] matrix){
        if( matrix.length > 3 ){
            return obliczWyznacznik(matrix);
        }else{
            return wyznacznik(matrix);
        }
    }

    public double[] matrixMultiplication(double[][] matrix1, double[] matrix2){
        double[][] matrixTmp1 = new double[matrix1.length][matrix1.length];
        double[] matrixTmp2 = new double[matrix2.length];
        int leftRowNumber = matrix1.length;
        int leftColumnNumber = matrix1[0].length;
        double[]resultMatrix = new double[matrix2.length];
        double s = 0;
        for( int i = 0 ; i < matrix1.length; ++i ){
            s = 0;
            for( int j = 0 ; j < matrix2.length ; ++j ){
                s = s + matrix1[i][j]*matrix2[j];
            }
            resultMatrix[i] = s;
        }
        return resultMatrix;
    }

    public double[][] matrixMultiplication(double[][] matrix1, double[][] matrix2){
        double[][] resultMatrix = new double[matrix1.length][matrix2.length];
        double s;
        for( int i = 0 ; i < matrix1.length; ++i ){
            for( int j = 0 ; j < matrix2[0].length ; ++j ){
                s = 0;
                for( int k = 0 ; k < matrix1[0].length ; ++k ){
                    s = s + matrix1[i][k]*matrix2[k][j];
                }
                resultMatrix[i][j] = s;
            }
        }
        return resultMatrix;
    }

    public double[][] matrixMultiplication(double[][] matrix1, double multiplicator){
        double[][] resultMatrix = new double[matrix1.length][matrix1[0].length];
        for( int i = 0 ; i < matrix1.length ; ++i ){
            for( int j = 0 ; j < matrix1.length ; ++j ){
                resultMatrix[i][j] = matrix1[i][j]*multiplicator;
            }
        }
        return resultMatrix;
    }

    public double[][] addMatrix(double[][] matrix1, double[][] matrix2){
        int rows = matrix1.length;
        int columns = matrix1[0].length;
        double[][] resultMatrix = new double[rows][columns];
        for( int i = 0 ; i < rows ; ++i ){
            for( int j = 0 ; j < columns ; ++j ){
                resultMatrix[i][j] = matrix1[i][j] + matrix2[i][j];
            }
        }
        return resultMatrix;
    }
    public double[][] substractMatrix(double[][] matrix1, double[][] matrix2){
        int rows = matrix1.length;
        int columns = matrix1[0].length;
        double[][] resultMatrix = new double[rows][columns];
        for( int i = 0 ; i < rows ; ++i ){
            for( int j = 0 ; j < columns ; ++j ){
                resultMatrix[i][j] = matrix1[i][j] - matrix2[i][j];
            }
        }
        return resultMatrix;
    }

    public double[] addMatrix(double[] matrix1, double[] matrix2){
        int rows = matrix1.length;
        double[] resultMatrix = new double[rows];
        for( int i = 0 ; i < rows ; ++i ){
                resultMatrix[i] = matrix1[i] + matrix2[i];
        }
        return resultMatrix;
    }

    public double[] copyMatrix(double[] matrix){
        double[] result = new double[matrix.length];
        for( int i = 0 ; i < result.length ; ++i ){
            result[i] = new Double(matrix[i]);
        }
        return result;
    }
    public double[][] copyMatrix(double[][] matrix){
        double[][] result = new double[matrix.length][matrix[0].length];
        for( int i = 0 ; i < result.length ; ++i ){
            for( int j = 0 ; j < matrix[0].length ; ++j ){
                result[i][j] = new Double(matrix[i][j]);
            }
        }
        return result;
    }

    public boolean diagonalNotZeros(double[][] matrix){
        for( int i = 0 ; i < matrix.length ; ++i ){
            if( matrix[i][i] == 0 ){
                return false;
            }
        }
        return true;
    }
    public void gausElimination(double[][] matrix, double[] matrixB){
        double[][] matrixGaus = copyMatrix(matrix);
        double[] matrixGausB = copyMatrix(matrixB);

        for( int i = 1 ; i < matrixGaus.length ; ++i ){
            for( int j = i ; j < matrixGaus.length ; ++j ){
                if( j < i ){
                    matrixGaus[i][j] = 0;
                }else{
                    matrixGaus[i][j] = matrixGaus[i][j] - matrixGaus[i-1][j]*(matrixGaus[j][i-1]/matrixGaus[i-1][i-1]);
                }
                matrixGausB[j] = matrixGausB[j] - matrixGausB[i-1]*(matrixGausB[j]);

            }
        }
    }

    public class PeculiarMatrixException extends Exception{
    }
    public class DivideByZeroException extends Exception{
    }
}
