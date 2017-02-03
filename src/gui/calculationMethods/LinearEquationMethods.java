package gui.calculationMethods;

import Jama.LUDecomposition;
import Jama.Matrix;
import gui.baseDataPanel.AvailableMethodsPanel;

/**
 * Created by Nogaz on 28.01.2017.
 */
public class LinearEquationMethods {
    public double[][] jacobiMethod(double[][] matrixA, double[]matrixX, double[][] matrixB){
        double result[][] = new double[2][2];

        return result;
    }

    public double[][] LUDecompositionMethod(double[][] matrixA, double[]matrixB){
        if( peculiar(matrixA) ){
            System.out.println("Macierz jest osobliwa!");
            return null;
        }
        double[][] result = new double[matrixA.length][matrixB.length];
        double[][] matrixL = new double[matrixA.length][matrixB.length];
        double[][] matrixU = new double[matrixA.length][matrixB.length];
        for( int i = 0 ; i < matrixA.length ; ++i ){
            for( int j = 0 ; j < matrixB.length ; ++j ){

                if( i > j ){
                    //ponizej gornej przekatnej
                    matrixL[i][j] = matrixA[i][j];
                    matrixU[i][j] = 0;
                }else if( i < j ){
                    //powyzej gornej przekatnej
                    matrixL[i][j] = 0;
                    matrixU[i][j] = matrixA[i][j];
                }else{
                    //glowna przekatna
                    matrixL[i][j] = 1;
                    matrixU[i][j] = matrixA[i][j];
                }
            }
        }
        System.out.println("Matrix A");
        printMatrix(matrixA);
        System.out.println("Matrix L");
        printMatrix(matrixL);
        System.out.println("Matrix U");
        printMatrix(matrixU);
        System.out.println("Matrix B");
        printMatrix(matrixB);


        return result;
    }

    public void jacubi(double[][] matrixA, double[] matrixB){

        LUDecomposition luDecomposition = new LUDecomposition(new Matrix(matrixA));
        Matrix matrixL = new Matrix(getDownMAtrix(matrixA));
        Matrix matrixU = new Matrix(getUpperMAtrix(matrixA));
        Matrix matrixD = new Matrix(getDiagonalMAtrix(matrixA));

        Matrix matrixN = matrixD.inverse();
        Matrix matrixM = matrixN.times(-1.0);
        Matrix sum = matrixL.plus(matrixU);
        matrixM = matrixM.times(sum);
        double[] matrixNB = matrixMultiplication( matrixN.getArrayCopy(), matrixB);

        double[] resultMatrix = new double[matrixB.length];
        double[] tmpResultMatrix = new double[matrixB.length];
        for( int i = 0 ; i < resultMatrix.length ; ++i ){
            resultMatrix[i] = 0;
            tmpResultMatrix[i] = 0;
        }
        printMatrix(matrixM.getArrayCopy());
        printMatrix(matrixNB);
        boolean preciseEnough = false;
        double difference;
        double possibleError = 0.002;
        int iteracja = 1;
        while( preciseEnough == false ){
            reprintMatrix(resultMatrix, tmpResultMatrix);
            resultMatrix = matrixMultiplication(matrixM.getArrayCopy(), resultMatrix);
            resultMatrix = addMatrix(resultMatrix, matrixNB);
            //spr wielkosc bledu
            preciseEnough = true;
            System.out.println("Iteracja: " + iteracja);
            System.out.println("aktualny stan wyniku:");
            for(int i = 0 ; i < resultMatrix.length ; ++i){
                System.out.println(resultMatrix[i]);
            }
            for( int i = 0 ; i < resultMatrix.length ; ++i ){
                if( Math.abs(resultMatrix[i] - tmpResultMatrix[i]) > possibleError ){
                    System.out.println("Koniec roboty");
                    preciseEnough = false;
                }
            }
            iteracja++;
        }
        printMatrix(resultMatrix);

    }

    public void jacobieMethod(double[][] matrixA, double[]matrixB){


        double[][] matrixL = getDownMAtrix(matrixA);
        double[][] matrixU = getUpperMAtrix(matrixA);
        double[][] matrixD = getDiagonalMAtrix(matrixA);

        if( matrixDiagonalDomination(matrixA) ){
            System.out.println("Macierz A jest diagonalnie dominująca.");
        }else{
            System.out.println("Macierz A NIE jest diagonalnie dominująca.");
            System.out.println("Metoda Jacobiego nie będzie zbieżna.");
            return;
        }
        System.out.println("Matrix A");
        printMatrix(matrixA);
        System.out.println("Matrix L");
        printMatrix(matrixL);
        System.out.println("Matrix U");
        printMatrix(matrixU);
        System.out.println("Matrix D");
        printMatrix(matrixD);
        System.out.println("Matrix B");
        printMatrix(matrixB);

        if( peculiar(matrixA) ){
            System.out.println("Macierz jest osobliwa!");
            return;
        }

        //obliczenia
        double[] prawyCzynnik = matrixMultiplication(matrixD, matrixB);
        double[] initialVector = new double[matrixB.length];
        double[] lewyCzynnik = new double[matrixB.length];
        double[][] matrixN;
        double[][] matrixM;
        for( int i = 0 ; i < initialVector.length ; ++i ){
            initialVector[i] = 0;
        }
        //matrixD = opositeMatrix(matrixD);

        //przygotowanie macierzy M
        matrixN = opositeMatrix(matrixD);
        matrixM = matrixMultiplication(matrixN, -1);
        System.out.println("\nMacierz -D^-1");
        printMatrix(matrixM);
        matrixM = matrixMultiplication(matrixM, addMatrix(matrixL, matrixU));
        System.out.println("\nMacierz -D(L+U)");
        printMatrix(matrixM);


        //przygotowanie macierzy N
        System.out.println("\nMacierz N");
        printMatrix(matrixN);


        double[] nbMatrix = matrixMultiplication(matrixN, matrixB);
        //obliczenia iteracyjne

        double[] resultMatrix;
        double[] tmpMat = new double[matrixB.length];

        boolean enough = false;
        double[] enoughMeter = tmpMat;
        double errorMeter = 0.002;
        int iterations = 0;
        for( int i = 0 ; i < tmpMat.length ; ++i ){
            tmpMat[i] = 0;
            enoughMeter[i] = 0;
        }
        while( enough == false ){
            reprintMatrix(tmpMat, enoughMeter);
            tmpMat = addMatrix( matrixMultiplication(matrixM, tmpMat), nbMatrix);
            enough = true;
            for( int i = 0 ; i < tmpMat.length ; ++i ){
                if( Math.abs(tmpMat[i] - enoughMeter[i]) > errorMeter ){
                    enough = false;
                }
                System.out.println("Roznica wyrazen w iteracji : " +( iterations+1) + " " + Math.abs(tmpMat[i] - enoughMeter[i]));
            }

            iterations++;
            System.out.println("Iteracja " + iterations);
            printMatrix(tmpMat);
        }
        resultMatrix = tmpMat;

        System.out.println("Wynik obliczeń:");
        printMatrix(resultMatrix);
        System.out.println("Potrzebna liczba iteracji: " + iterations);

    }

    public void gaussSeidlMethod(double[][] matrixA, double[]matrixB){


        double[][] matrixL = getDownMAtrix(matrixA);
        double[][] matrixU = getUpperMAtrix(matrixA);
        double[][] matrixD = getDiagonalMAtrix(matrixA);

        if( matrixDiagonalDomination(matrixA) ){
            System.out.println("Macierz A jest diagonalnie dominująca.");
        }else{
            System.out.println("Macierz A NIE jest diagonalnie dominująca.");
            System.out.println("Metoda Jacobiego nie będzie zbieżna.");
            return;
        }


        if( peculiar(matrixA) ){
            System.out.println("Macierz jest osobliwa!");
            return;
        }

        //obliczenia
        double[] initialVector = new double[matrixB.length];
        double[][] matrixN;
        double[][] matrixM;
        double[][] matrixOpisiteDiag;
        double[] matrixO;
        for( int i = 0 ; i < initialVector.length ; ++i ){
            initialVector[i] = 0;
        }
        //matrixD = opositeMatrix(matrixD);

        //przygotowanie macierzy M
        matrixM = addMatrix(matrixD, matrixL);
        matrixM = opositeMatrix(matrixM);

        matrixU = matrixMultiplication(matrixU, -1);

        //obliczenia iteracyjne

        double[] resultMatrix = new double[matrixB.length];
        double[] tmpMat = new double[matrixB.length];

        double[] rightEquation = new double[matrixB.length];
        boolean enough = false;
        double[] actualRes = new double[matrixB.length];
        double errorMeter = 0.002;
        int iterations = 0;
        for( int i = 0 ; i < tmpMat.length ; ++i ){
            tmpMat[i] = 0;
            actualRes[i] = 0;
        }
        while( enough == false ){
            reprintMatrix(tmpMat, actualRes);
            rightEquation = matrixMultiplication(matrixU, tmpMat);
            rightEquation = addMatrix(rightEquation, matrixB);
            tmpMat = matrixMultiplication(matrixM, rightEquation);
            enough = true;
            for( int i = 0 ; i < tmpMat.length ; ++i ){
                if( Math.abs(tmpMat[i] - actualRes[i]) > errorMeter ){
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
    double[][] getDownMAtrix(double[][] matrix){
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

    public double[][] transposedMatrix(double[][] matrix){
        double[][] transposed = new double[matrix.length][matrix[0].length];
        for( int i = 0 ; i < transposed.length ; ++i ){
            for( int j = 0 ; j < transposed[0].length ; ++j ){
                transposed[i][j] = matrix[j][i];
            }
        }
        return transposed;
    }

    public double[][] opositeMatrix(double[][] matrix){
        double[][] opositeMatrix = new double[matrix.length][matrix[0].length];

        //oblicz wyznacznik detA
        double detA = wybierzIObliczWyznacznik(matrix);

        System.out.println("Wyznacznik macierzy = " + detA);
        if( detA == 0 ){
            //nie istnieje macierz odwrotna
            System.out.println("Nie istnieje macierz odwrotna, sorki");
            return null;
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

    public double[] addMatrix(double[] matrix1, double[] matrix2){
        int rows = matrix1.length;
        double[] resultMatrix = new double[rows];
        for( int i = 0 ; i < rows ; ++i ){
                resultMatrix[i] = matrix1[i] + matrix2[i];
        }
        return resultMatrix;
    }

    public boolean diagonalNotZeros(double[][] matrix){
        for( int i = 0 ; i < matrix.length ; ++i ){
            if( matrix[i][i] == 0 ){
                return false;
            }
        }
        return true;
    }
}
