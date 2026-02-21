package M8E;

import java.util.ArrayList;

public class StoreStrings {
	
	public static int count(ArrayList <Integer> words)
    {
        int lSize = 1;
        for(String i : words)
        {
            lSize++;
        }
        
		return lSize;
    
    }
	
	public static boolean duplicateString(ArrayList <String> words) {
		boolean isDuplicateString = False;
		
		ArrayList<> items = new ArrayList<String>();
		
        for(String word : words)
        {
            if (items.contains(word)) {
            	isDuplicateString = True;
            }else {
            	items.add(word);
            }
        }
		
		
		return isDuplicateString;
		
	}
}
