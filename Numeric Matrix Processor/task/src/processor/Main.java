package processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int option;
        Scanner scanner = new Scanner(System.in);

        do {
            printOptions();
            option = scanner.nextInt();
            operation(option, scanner);
        } while (option != 0);

    }
    private static void printOptions() {
        System.out.println("1. Add matrices");
        System.out.println("2. Multiply matrix by a constant");
        System.out.println("3. Multiply matrices");
        System.out.println("4. Transpose matrix");
        System.out.println("5. Calculate a determinant");
        System.out.println("6. Inverse matrix");
        System.out.println("0. Exit");
        System.out.print("Your choice: ");
    }
    private static void printTransposeOptions(){
        System.out.println("1. Main diagonal");
        System.out.println("2. Side diagonal");
        System.out.println("3. Vertical line");
        System.out.println("4. Horizontal line");
        System.out.print("Your choice: ");
    }
    private static void operation(int option, Scanner scanner) {
        switch (option) {
            case 0:
                break;
            case 1:
                List<double[][]> list1 = readTwoMatrices(scanner);
                System.out.println("The result is:");
                printMatrix(addMatrices(list1.get(0), list1.get(1)));
                break;
            case 2:
                double[][] matrix1 = readMatrix(scanner);
                System.out.println("Enter constant:");
                double k = scanner.nextDouble();
                System.out.println("The result is:");
                printMatrix(multiplyByConstant(matrix1, k));
                break;
            case 3:
                List<double[][]> list2 = readTwoMatrices(scanner);
                System.out.println("The result is:");
                printMatrix(multiplyMatrices(list2.get(0), list2.get(1)));
                break;
            case 4:
                printTransposeOptions();
                int tChoice = scanner.nextInt();
                double[][] matrix2 = readMatrix(scanner);
                System.out.println("The result is:");
                printMatrix(transpose(matrix2, tChoice));
                break;
            case 5:
                double[][] matrix3 = readMatrix(scanner);
                System.out.println("The result is:");
                System.out.println(determinant(matrix3));
                break;
            case 6:
                double[][] matrix4 = readMatrix(scanner);
                if (determinant(matrix4) == 0) {
                    System.out.println("This matrix doesn't have an inverse.");
                } else {
                    System.out.println("The result is:");
                    printMatrix(inverseMatrix(matrix4));
                }
                break;
        }
    }
    private static double[][] transpose(double[][] matrix, int tChoice) {
        switch (tChoice) {
            case 1:
                return mainTranspose(matrix);
            case 2:
                return sideTranspose(matrix);
            case 3:
                return verticalTranspose(matrix);
            case 4:
                return horizontalTranspose(matrix);
            default:
                return matrix;
        }
    }
    private static double[][] mainTranspose(double[][] matrix){
        int rows = matrix.length;
        int columns = matrix[0].length;
        double[][] result = new double[columns][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[j][i] = matrix[i][j];
            }
        }
        return result;
    }
    private static double[][] sideTranspose(double[][] matrix){
        int rows = matrix.length;
        int columns = matrix[0].length;
        double[][] result = new double[columns][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[i][j] = matrix[columns - 1 -j][rows - 1 - i];
            }
        }
        return result;
    }
    private static double[][] verticalTranspose(double[][] matrix){
        int rows = matrix.length;
        int columns = matrix[0].length;
        double[][] result = new double[columns][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[i][j] = matrix[i][columns - 1 - j];
            }
        }
        return result;
    }
    private static double[][] horizontalTranspose(double[][] matrix){
        int rows = matrix.length;
        int columns = matrix[0].length;
        double[][] result = new double[columns][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[i][j] = matrix[rows - 1 - i][j];
            }
        }
        return result;
    }
    private static double[][] multiplyByConstant(double[][] matrix, double k) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        double[][] result = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[i][j] = matrix[i][j] * k;
            }
        }
        return result;
    }
    private static double[][] addMatrices(double[][] matrixA, double[][] matrixB) {
        int rowsA = matrixA.length;
        int columnsA = matrixA[0].length;
        double[][] result = new double[rowsA][columnsA];
        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < columnsA; j++) {
                result[i][j] = matrixA[i][j] + matrixB[i][j];
            }
        }
        return result;
    }
    private static double[][] multiplyMatrices(double[][] matrixA, double[][] matrixB) {
        int rowsA = matrixA.length;
        int columnsA = matrixA[0].length;
        int rowsB = matrixB.length;
        int columnsB = matrixB[0].length;
        double[][] result = new double[rowsA][columnsB];
        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < columnsA; j++) {
                for (int k = 0; k < columnsB; k++) {
                    result[i][k] += matrixA[i][j] * matrixB[j][k];
                }
            }
        }
        return result;
    }
    private static double determinant(double[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        if (rows == 2 && columns == 2) {
            return (matrix[0][0] * matrix[1][1]) - (matrix[0][1] * matrix[1][0]);
        } else {
            double result = 0;
            for (int i = 0; i < columns; i++) {
                result += Math.pow(-1, i) * matrix[0][i] * determinant(getMinor(matrix, 0, i));
            }
            return result;
        }


    }
    private static double[][] getMinor(double[][] matrix, int r, int c) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        double[][] minor = new double[rows - 1][columns - 1];
        for (int i = 0; i < rows; i++) {
            if (i != r) {
                for (int j = 0; j < columns; j++) {
                    if (j != c) {
                        if (i < r && j < c) {
                            minor[i][j] = matrix[i][j];
                        } else if (i < r && j > c) {
                            minor[i][j - 1] = matrix[i][j];
                        } else if (i > r && j < c) {
                            minor[i - 1][j] = matrix[i][j];
                        } else if (i > r && j > c) {
                            minor[i - 1][j - 1] = matrix[i][j];
                        }
                    }
                }
            }
        }
        return minor;
    }
    private static double getCofactor(double[][] matrix, int r, int c) {
        return Math.pow(-1, r + c) * determinant(getMinor(matrix, r, c));
    }
    private static double[][] cofactorMatrix(double[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        double[][] cMatrix = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                cMatrix[i][j] = getCofactor(matrix, i, j);
            }
        }
        return cMatrix;

    }
    private static double[][] adjointMatrix(double[][] matrix) {
        return mainTranspose(cofactorMatrix(matrix));


    }
    private static double[][] inverseMatrix(double[][] matrix) {
        return multiplyByConstant(adjointMatrix(matrix), (1 / determinant(matrix)));
    }
    private static double[][] readMatrix(Scanner scanner) {
        System.out.print("Enter size of matrix:");
        int rows = scanner.nextInt();
        int columns = scanner.nextInt();
        System.out.println("Enter matrix:");
        double[][] matrix = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = scanner.nextDouble();
            }
        }
        return matrix;
    }
    private static List<double[][]> readTwoMatrices(Scanner scanner) {
        List<double[][]> list = new ArrayList<>();
        System.out.print("Enter size of first matrix:");
        int rows1 = scanner.nextInt();
        int columns1 = scanner.nextInt();
        System.out.println("Enter first matrix:");
        double[][] matrix1 = new double[rows1][columns1];
        for (int i = 0; i < rows1; i++) {
            for (int j = 0; j < columns1; j++) {
                matrix1[i][j] = scanner.nextDouble();
            }
        }
        System.out.print("Enter size of second matrix:");
        int rows2 = scanner.nextInt();
        int columns2 = scanner.nextInt();
        System.out.println("Enter second matrix:");
        double[][] matrix2 = new double[rows2][columns2];
        for (int i = 0; i < rows2; i++) {
            for (int j = 0; j < columns2; j++) {
                matrix2[i][j] = scanner.nextDouble();
            }
        }
        list.add(matrix1);
        list.add(matrix2);
        return list;
    }
    private static void printMatrix(double[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.print("\n");
        }
    }
}
