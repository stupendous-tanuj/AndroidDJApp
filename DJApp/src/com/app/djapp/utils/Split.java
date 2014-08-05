package com.app.djapp.utils;
import java.util.Vector;

public class Split {

	 public static String[] splitclass(final String s, final String sep) {
		    // convert a String s to an Array, the elements
		    // are delimited by sep
		    final Vector tokenIndex = new Vector();
		    final int len = s.length();
		    int i;

		    // Find all characters in string matching one of the separators in 'sep'
		    for (i = 0; i < len; i++)
		      if (sep.indexOf(s.charAt(i)) != -1)
		        tokenIndex.addElement(new Integer(i));

		    final int size = tokenIndex.size();
		    final String[] elements = new String[size + 1];

		    // No separators: return the string as the first element
		    if (size == 0)
		      elements[0] = s;
		    else {
		      // Init indexes
		      int start = 0;
		      int end = ((Integer) tokenIndex.elementAt(0)).intValue();
		      // Get the first token
		      elements[0] = s.substring(start, end);

		      // Get the mid tokens
		      for (i = 1; i < size; i++) {
		        // update indexes
		        start = ((Integer) tokenIndex.elementAt(i - 1)).intValue() + 1;
		        end = ((Integer) tokenIndex.elementAt(i)).intValue();
		        elements[i] = s.substring(start, end);
		      }
		      // Get last token
		      start = ((Integer) tokenIndex.elementAt(i - 1)).intValue() + 1;
		      elements[i] = (start < s.length()) ? s.substring(start) : "";
		    }

		    return elements;
		  }
	
}