package old;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class MatrixOp {

    public static final int INPUT_LINES = 3;

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] inputData = new String[INPUT_LINES];
        for(int i = 0; i < INPUT_LINES; i++){
            inputData[i] = br.readLine();
        }


        String[] first = inputData[0].split(" ");
        double[] firstMatrix = new double[first.length];
        for(int i = 0; i < first.length; i++){
            firstMatrix[i] = Double.parseDouble(first[i]);

        }

        String[] second = inputData[1].split(" ");
        double[] secondMatrix = new double[second.length];
        for(int i = 0; i < second.length; i++){
            secondMatrix[i] = Double.parseDouble(second[i]);

        }

        String[] third =  inputData[2].split(" ");
        double[] thirdMatrix = new double[third.length];
        for(int i = 0; i < third.length; i++){
            thirdMatrix[i] = Double.parseDouble(third[i]);

        }


        double [][] a = initMatrix(firstMatrix);
        double [][] b = initMatrix(secondMatrix);
        double [][] pi = initMatrix(thirdMatrix);

        double [][] vectorTrans = multiMatrix(pi, a);
        double [][] vectorEmission = multiMatrix(vectorTrans, b);
        printMatrix(vectorEmission);



    }

    public static void printMatrix(double [][] matrix){
        System.out.print(matrix.length + " " + matrix[0].length + " ");
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[0].length; j++){
                System.out.print((float) matrix[i][j] + " ");
            }
        }
    }


    public static double[][] initMatrix(double[] num){
        int rows = (int) num[0];
        int collons = (int) num[1];
        double [][] matrix = new double[rows][collons];
        int increment = 2;
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < collons; j++){
                matrix[i][j] = num[increment];
                increment++;
            }
        }
        return matrix;
    }

    public static double [][] multiMatrix(double [][] firstMatrix, double[][] secondMatrix) {
        double[][] result = new double[firstMatrix.length][secondMatrix[0].length];
        for (int i = 0; i < firstMatrix.length; i++) {
            for (int j = 0; j < secondMatrix[0].length; j++) {
                for (int k = 0; k < firstMatrix[0].length; k++) {
                    result[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
                }
            }
        }
        return result;
    }
}