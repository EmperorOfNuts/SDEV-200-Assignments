import M4A1.*;

static Scanner scanner = new Scanner(System.in);

public static void main() throws IOException {
    String filename = scanner.nextLine();
    // C:\Users\icete\Desktop\IvyTech\SDEV 200\src\M4A1\Welcome.java

    if (checkGroupingSymbols(filename)) System.out.println("Correct grouping pairs");
    else System.out.println("Incorrect grouping pairs");

}

public static boolean checkGroupingSymbols(String filename) throws IOException {
    Stack<Character> stack = new Stack<>();

    // Output File
    try (BufferedReader displayReader = new BufferedReader(new FileReader(filename))) {
        String line;
        while ((line = displayReader.readLine()) != null) System.out.println(line);
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
        String line;

        while ((line = reader.readLine()) != null) {

            for (int i = 0; i < line.length(); i++) {
                char ch = line.charAt(i);

                // Handle Character/String literals
                if (ch == '\'' || ch == '"') {
                    char quote = ch;
                    while (++i < line.length() && line.charAt(i) != quote) if (line.charAt(i) == '\\') i++;
                    continue;
                }


                // Handle Comments
                if (ch == '/' && i + 1 < line.length()) {
                    if (line.charAt(i + 1) == '/') break;
                    else if (line.charAt(i + 1) == '*') {
                        // Handle Multi-Line Comments
                        i += 2;
                        while (i < line.length() && !(line.charAt(i) == '*' && i + 1 < line.length() && line.charAt(i + 1) == '/')) i++;
                        if (i < line.length() && line.charAt(i) == '*')  i++; // Skip the *

                        continue;
                    }
                }

                // Check for opening symbols
                if (ch == '(' || ch == '{' || ch == '[') stack.push(ch);

                // Check for closing symbols
                else if (ch == ')' || ch == '}' || ch == ']') {
                    if (stack.isEmpty()) return false;
                    char top = stack.pop();
                    if (!isMatchingPair(top, ch)) return false;
                }
            }
        }
        // Check if any opening symbols aren't matched
        return stack.isEmpty();
    }
}

private static boolean isMatchingPair(char opening, char closing) {
    return (opening == '(' && closing == ')') || (opening == '{' && closing == '}') || (opening == '[' && closing == ']');
}