public static void main() throws Exception {
    Scanner input = new Scanner(System.in);
    System.out.print("Enter a Java source file: ");
    String filename = input.nextLine();
    // C:\Users\icete\Desktop\IvyTech\SDEV 200\src\M4A2\Welcome.java
    // Answer should be 6 because of the keyword "package"

    File file = new File(filename);
    if (file.exists()) System.out.println("The number of keywords in " + filename + " is " + countKeywords(file));
    else System.out.println("File " + filename + " does not exist");
}

public static int countKeywords(File file) throws Exception {
    Set<String> keywordSet = new HashSet<>(Arrays.asList(
            "abstract", "assert", "boolean", "break", "byte", "case",
            "catch", "char", "class", "const", "continue", "default",
            "do", "double", "else", "enum", "extends", "for", "final",
            "finally", "float", "goto", "if", "implements", "import",
            "instanceof", "int", "interface", "long", "native", "new",
            "package", "private", "protected", "public", "return",
            "short", "static", "strictfp", "super", "switch",
            "synchronized", "this", "throw", "throws", "transient",
            "try", "void", "volatile", "while"
    ));

    int count = 0;

    try (Scanner input = new Scanner(file)) {
        while (input.hasNextLine()) {
            String line = input.nextLine();
            count += processLine(line, keywordSet);
        }
    }

    return count;
}

private static int processLine(String line, Set<String> keywordSet) {
    int count = 0;
    Scanner lineScanner = new Scanner(line);

    while (lineScanner.hasNext()) {
        String token = lineScanner.next();

        if (token.startsWith("//")) break; // Break at comment lines
        if (token.startsWith("\"") || token.startsWith("'")) continue; // Skip anything in quotes
        String cleanToken = token.replaceAll("[^a-zA-Z0-9_]", ""); // Remove trash such as punctuation

        if (keywordSet.contains(cleanToken)) count++;
    }

    lineScanner.close();
    return count;
}