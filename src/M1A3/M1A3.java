import java.util.*;
static Scanner scanner = new Scanner(System.in);

public static void main(String[] args) {

    int[][] matrix1 = new int[3][3];
    int[][] matrix2 = new int[3][3];

    System.out.print("Enter matrix1 (a 3 by 3 matrix) row by row: ");
    String inputLine1 = scanner.nextLine();
    System.out.print("Enter matrix2 (a 3 by 3 matrix) row by row: ");
    String inputLine2 = scanner.nextLine();

    String[] numberStrings1 = inputLine1.trim().split("\\s+");
    String[] numberStrings2 = inputLine2.trim().split("\\s+");

    int index = 0;
    for(int r = 0; r < 3; r++) {
        for(int c = 0; c < 3; c++) {
            matrix1[r][c] = Integer.parseInt(numberStrings1[index]);
            index++;
        }
    }

    index = 0;
    for(int r = 0; r < 3; r++) {
        for(int c = 0; c < 3; c++) {
            matrix2[r][c] = Integer.parseInt(numberStrings2[index]);
            index++;
        }
    }

    if(equals(matrix1, matrix2)) System.out.println("The matrices are equal");
    else System.out.println("The matrices are not equal");

}

public static boolean equals(int[][] m1, int[][] m2) {
    return Arrays.deepEquals(m1, m2);
}