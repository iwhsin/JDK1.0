/*
 * @(#)String.java	1.54 95/12/07  
 *
 * Copyright (c) 1994 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Permission to use, copy, modify, and distribute this software
 * and its documentation for NON-COMMERCIAL purposes and without
 * fee is hereby granted provided that this copyright notice
 * appears in all copies. Please refer to the file "copyright.html"
 * for further important copyright and licensing information.
 *
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

package java.lang;

import java.util.Hashtable;

/**
 * A general class of objects to represent character Strings.
 * Strings are constant, their values cannot be changed after creation.
 * The compiler makes sure that each String constant actually results
 * in a String object. Because String objects are immutable they can
 * be shared. For example:
 * <pre>
 *	String str = "abc";
 * </pre>
 * is equivalent to:
 * <pre>
 *	char data[] = {'a', 'b', 'c'};
 *	String str = new String(data);
 * </pre>
 * Here are some more examples of how strings can be used:
 * <pre>
 * 	System.out.println("abc");
 * 	String cde = "cde";
 * 	System.out.println("abc" + cde);
 *	String c = "abc".substring(2,3);
 *	String d = cde.substring(1, 2);
 * </pre>
 * 
 * String objects are immutable, meaning that once created, their content cannot
 * be modified. This property ensures thread safety and allows strings to be
 * efficiently shared across multiple parts of a program. When a "modification"
 * operation is performed on a String, it actually creates and returns a new String
 * object rather than modifying the original.
 * 
 * @see		StringBuffer
 * @version 	1.54, 12/07/95
 * @author 	Lee Boynton
 * @author	Arthur van Hoff
 */
public final
class String {
    /** The value is used for character storage. */
    private char value[];

    /** The offset is the first index of the storage that is used. */
    private int offset;

    /** The count is the number of characters in the String. */
    private int count;

    /**
     * Constructs a new empty String.
     * Creates a String object with zero characters, representing an empty string.
     * The internal character array will have length 0.
     */
    public String() {
	value = new char[0];
    }

    /**
     * Constructs a new String that is a copy of the specified String.
     * Creates a deep copy of the provided String, ensuring the new String
     * has its own character array containing the same sequence of characters.
     * 
     * @param value the initial value of the String to be copied
     */
    public String(String value) {
	count = value.length();
	this.value = new char[count];
	value.getChars(0, count, this.value, 0);
    }

    /**
     * Constructs a new String whose initial value is the specified array 
     * of characters.
     * Creates a new String containing the characters in the provided array.
     * A copy of the array is made to ensure the String's immutability.
     * 
     * @param value the initial value of the String, an array of characters
     */
    public String(char value[]) {
	this.count = value.length;
	this.value = new char[count];
	System.arraycopy(value, 0, this.value, 0, count);
    }

    /**
     * Constructs a new String whose initial value is the specified sub array of characters.
     * The length of the new string will be count characters
     * starting at offset within the specified character array.
     * 
     * This constructor allows creating a String from a portion of a character array,
     * copying only the specified range of characters into the new String.
     * 
     * @param value	the initial value of the String, an array of characters
     * @param offset	the offset into the value of the String
     * @param count 	the length of the value of the String
     * @exception StringIndexOutOfBoundsException If the offset and count arguments are invalid.
     */
    public String(char value[], int offset, int count) {
	if (offset < 0) {
	    throw new StringIndexOutOfBoundsException(offset);
	}
	if (count < 0) {
	    throw new StringIndexOutOfBoundsException(count);
	}
	if (offset + count > value.length) {
	    throw new StringIndexOutOfBoundsException(offset + count);
	}

	this.value = new char[count];
	this.count = count;
	System.arraycopy(value, offset, this.value, 0, count);
    }

    /**
     * Constructs a new String whose initial value is the specified sub array of bytes.
     * The high-byte of each character can be specified, it should usually be 0.
     * The length of the new String will be count characters
     * starting at offset within the specified character array.
     * 
     * This constructor converts bytes to characters by treating each byte as the
     * low 8 bits of a Unicode character, and combining it with the specified high byte.
     * This is useful for converting 8-bit ASCII or ISO-8859-1 encoded bytes to Unicode.
     *  
     * @param ascii	the bytes that will be converted to characters
     * @param hibyte	the high byte of each Unicode character
     * @param offset	the offset into the ascii array
     * @param count 	the length of the String
     * @exception StringIndexOutOfBoundsException If the offset and count arguments are invalid.
     */
    public String(byte ascii[], int hibyte, int offset, int count) {
	if (offset < 0) {
	    throw new StringIndexOutOfBoundsException(offset);
	}
	if (count < 0) {
	    throw new StringIndexOutOfBoundsException(count);
	}
	if (offset + count > ascii.length) {
	    throw new StringIndexOutOfBoundsException(offset + count);
	}

	char value[] = new char[count];
	this.count = count;
	this.value = value;

	if (hibyte == 0) {
	    for (int i = count ; i-- > 0 ;) {
		value[i] = (char) (ascii[i + offset] & 0xff);
	    }
	} else {
	    hibyte <<= 8;
	    for (int i = count ; i-- > 0 ;) {
		value[i] = (char) (hibyte | (ascii[i + offset] & 0xff));
	    }
	}
    }

    /**
     * Constructs a new String whose value is the specified array of bytes.
     * The byte array transformed into Unicode chars using hibyte
     * as the upper byte of each character.
     * 
     * This is a convenience constructor that calls the more general constructor
     * with the offset set to 0 and count set to the length of the ascii array.
     * 
     * @param ascii	the byte that will be converted to characters
     * @param hibyte	the top 8 bits of each 16 bit Unicode character
     */
    public String(byte ascii[], int hibyte) {
	this(ascii, hibyte, 0, ascii.length);
    }

     
    /**
     * Construct a new string whose value is the current contents of the
     * given string buffer.
     * 
     * This constructor creates a new String containing the characters that are
     * currently in the StringBuffer. Subsequent changes to the StringBuffer
     * will not affect the String that was created.
     * 
     * @param buffer the stringbuffer to be converted
     */
    public String (StringBuffer buffer) { 
	synchronized(buffer) { 
	    buffer.setShared();
	    this.value = buffer.getValue();
	    this.offset = 0;
	    this.count = buffer.length();
	}
    }
    

    /**
     * Returns the length of the String.
     * The length of the String is equal to the number of 16 bit
     * Unicode characters in the String.
     * 
     * For an empty string, the length is 0.
     * 
     * @return the number of characters in this string
     */
    public int length() {
	return count;
    }

    /**
     * Returns the character at the specified index. An index ranges
     * from <tt>0</tt> to <tt>length() - 1</tt>.
     * 
     * The first character of the string is at index 0, the next at index 1,
     * and so on, similar to array indexing.
     * 
     * @param index	the index of the desired character
     * @return      the character at the specified index
     * @exception	StringIndexOutOfBoundsException If the index is not
     *			in the range <tt>0</tt> to <tt>length()-1</tt>.
     */
    public char charAt(int index) {
	if ((index < 0) || (index >= count)) {
	    throw new StringIndexOutOfBoundsException(index);
	}
	return value[index + offset];
    }

    /**
     * Copies characters from this String into the specified character array.
     * The characters of the specified substring (determined by
     * srcBegin and srcEnd) are copied into the character array,
     * starting at the array's dstBegin location.
     * 
     * This method can be used to efficiently transfer characters from a String
     * to a character array without creating intermediate objects.
     * 
     * @param srcBegin	index of the first character in the string to copy
     * @param srcEnd	index after the last character in the string to copy
     * @param dst		the destination array where the characters are copied
     * @param dstBegin	the start offset in the destination array
     */
    public void getChars(int srcBegin, int srcEnd, char dst[], int dstBegin) {
	System.arraycopy(value, offset + srcBegin, dst, dstBegin, srcEnd - srcBegin);
    }

    /**
     * Copies characters from this String into the specified byte array.
     * Copies the characters of the specified substring (determined by
     * srcBegin and srcEnd) into the byte array, starting at the
     * array's dstBegin location.
     * 
     * This method copies characters to bytes by taking only the lower 8 bits
     * of each character. This can result in data loss for characters that
     * require more than 8 bits to represent in Unicode.
     * 
     * @param srcBegin	index of the first character in the String to copy
     * @param srcEnd	index after the last character to copy
     * @param dst		the destination array
     * @param dstBegin	the start offset in the destination array
     */
    public void getBytes(int srcBegin, int srcEnd, byte dst[], int dstBegin) {
	int j = dstBegin;
	int n = offset + srcEnd;
	int i = offset + srcBegin;
	while (i < n) {
	    dst[j++] = (byte)value[i++];
	}
    }

    /**
     * Compares this String to the specified object.
     * Returns true if the object is equal to this String; that is,
     * has the same length and the same characters in the same sequence.
     * 
     * Two Strings are considered equal if and only if they contain exactly the
     * same sequence of characters. This method performs a character-by-character
     * comparison after first checking that the other object is a String and has
     * the same length.
     * 
     * @param anObject	the object to compare this String against
     * @return 	true if the Strings are equal; false otherwise.
     */
    public boolean equals(Object anObject) {
	if ((anObject != null) && (anObject instanceof String)) {
	    String anotherString = (String)anObject;
	    int n = count;
	    if (n == anotherString.count) {
		char v1[] = value;
		char v2[] = anotherString.value;;
		int i = offset;
		int j = anotherString.offset;
		while (n-- != 0) {
		    if (v1[i++] != v2[j++]) {
			return false;
		    }
		}
		return true;
	    }
	}
	return false;
    }

    /**
     * Compares this String to another object.
     * Returns true if the object is equal to this String; that is,
     * has the same length and the same characters in the same sequence.
     * Upper case characters are folded to lower case before
     * they are compared.
     * 
     * This method performs a case-insensitive comparison of the two strings.
     * It is useful when comparing strings where case differences should be ignored,
     * like user input or identifiers.
     * 
     * @param anotherString	the String to compare this String against
     * @return 	true if the Strings are equal, ignoring case; false otherwise.
     */
    public boolean equalsIgnoreCase(String anotherString) {
	return (anotherString != null) && (anotherString.count == count) &&
		regionMatches(true, 0, anotherString, 0, count);
    }

    /**
     * Compares this String to another specified String.
     * Returns an integer that is less than, equal to, or greater than zero.
     * The integer's value depends on whether this String is less than, equal to, or greater
     * than anotherString.
     * 
     * The comparison is based on the Unicode value of each character in the strings.
     * The result is negative if this string lexicographically precedes the argument string,
     * positive if this string lexicographically follows the argument string, or zero if
     * the strings are equal.
     * 
     * @param anotherString the String to be compared
     * @return a negative integer, zero, or a positive integer as this string is
     *         lexicographically less than, equal to, or greater than the specified string
     */
    public int compareTo(String anotherString) {
	int len1 = count;
	int len2 = anotherString.count;
	int n = Math.min(len1, len2);
	char v1[] = value;
	char v2[] = anotherString.value;
	int i = offset;
	int j = anotherString.offset;

	while (n-- != 0) {
	    char c1 = v1[i++];
	    char c2 = v2[j++];
	    if (c1 != c2) {
		return c1 - c2;
	    }
	}
	return len1 - len2;
    }

    /**
     * Determines whether a region of this String matches the specified region
     * of the specified String.
     * 
     * This method tests if two string regions are equal. The regions are of the
     * same length and are character-by-character compared for equality.
     * 
     * @param toffset	where to start looking in this String
     * @param other     the other String
     * @param ooffset	where to start looking in the other String
     * @param len       the number of characters to compare
     * @return          true if the region matches with the other; false otherwise.
     */
    public boolean regionMatches(int toffset, String other, int ooffset, int len) {
	char ta[] = value;
	int to = offset + toffset;
	int tlim = offset + count;
	char pa[] = other.value;
	int po = other.offset + ooffset;
	int plim = po + other.count;
	if ((ooffset < 0) || (toffset < 0) || (to + len > tlim) || (po + len > plim)) {
	    return false;
	}
	while (--len >= 0) {
	    if (ta[to++] != pa[po++]) {
	        return false;
	    }
	}
	return true;
    }

    /**
     * Determines whether a region of this String matches the specified region
     * of the specified String.  If the boolean ignoreCase is true, upper case characters are 
     * considered equivalent to lower case letters.
     * 
     * This method is similar to regionMatches(int, String, int, int) but with
     * an additional parameter to specify case sensitivity. When ignoreCase is true,
     * the comparison is performed using a case-insensitive ordering.
     * 
     * @param ignoreCase if true, case is ignored when comparing characters
     * @param toffset	where to start looking in this String
     * @param other     the other String
     * @param ooffset	where to start looking in the other String
     * @param len       the number of characters to compare
     * @return          true if the region matches with the other; false otherwise.
     */
    public boolean regionMatches(boolean ignoreCase,
				         int toffset,
			               String other, int ooffset, int len) {
	char ta[] = value;
	int to = offset + toffset;
	int tlim = offset + count;
	char pa[] = other.value;
	char trt[] = Character.upCase;
	int po = other.offset + ooffset;
	int plim = po + other.count;
	if ((ooffset < 0) || (toffset < 0) || (to + len > tlim) || (po + len > plim)) {
	    return false;
	}
	while (--len >= 0) {
	    int c1 = ta[to++];
	    int c2 = pa[po++];
	    if ((c1 != c2)
		    && (!ignoreCase ||
			(c1 > 256) || (c2 > 256) ||
			(trt[c1] != trt[c2]))) {
		return false;
	    }
	}
	return true;
    }

    /**
     * Determines whether this String starts with some prefix.
     * 
     * Tests if the substring of this string beginning at the specified
     * offset starts with the specified prefix.
     * 
     * @param prefix	the prefix to check for
     * @param toffset	where to begin looking in the string
     * @return 		true if the String starts with the specified prefix at the given offset; 
     *                false otherwise.
     */
    public boolean startsWith(String prefix, int toffset) {
	char ta[] = value;
	int to = offset + toffset;
	int tlim = offset + count;
	char pa[] = prefix.value;
	int po = prefix.offset;
	int pc = prefix.count;
	int plim = po + pc;
	if ((toffset < 0) || (to + pc > tlim)) {
	    return false;
	}
	while (--pc >= 0) {
	    if (ta[to++] != pa[po++]) {
	        return false;
	    }
	}
	return true;
    }

    /**
     * Determines whether this String starts with some prefix.
     * 
     * Tests if this string starts with the specified prefix.
     * This is a convenience method that calls startsWith(prefix, 0).
     * 
     * @param prefix	the prefix to check for
     * @return 		true if the String starts with the specified prefix; false otherwise. 
     */
    public boolean startsWith(String prefix) {
	return startsWith(prefix, 0);
    }

    /**
     * Determines whether the String ends with some suffix.
     * 
     * Tests if this string ends with the specified suffix.
     * 
     * @param suffix	the suffix to check for
     * @return 		true if the String ends with the specified suffix; false otherwise.
     */
    public boolean endsWith(String suffix) {
	return startsWith(suffix, count - suffix.count);
    }

    /**
     * Returns a hashcode for this String. This is a large
     * number composed of the character values in the String.
     * 
     * The hash code is computed using a specific algorithm to ensure consistent
     * behavior across different JVMs. For performance reasons, strings longer than
     * 16 characters are hashed by sampling rather than using all characters.
     * 
     * The general contract of hashCode is that equal objects must produce the
     * same hash code. This implementation ensures that for any two strings s1 and s2,
     * s1.equals(s2) implies s1.hashCode() == s2.hashCode().
     * 
     * @return a hash code value for this string
     */
    public int hashCode() {
	int h = 0;
	int off = offset;
	char val[] = value;
	int len = count;

	if (len < 16) {
	    for (int i = len ; i > 0; i--) {
		h = (h * 37) + val[off++];
	    }
	} else {
	    // only sample some characters
	    int skip = len / 8;
	    for (int i = len ; i > 0; i -= skip, off += skip) {
		h = (h * 39) + val[off];
	    }
	}
	return h;
    }

    /**
     * Returns the index within this String of the first occurrence of the specified 
     * character.  This method returns -1 if the index is not found.
     * 
     * The search begins from the beginning of the string (index 0) and proceeds
     * character by character to the end of the string.
     * 
     * @param ch	the character to search for
     * @return the index of the first occurrence of the character, or -1 if not found
     */
    public int indexOf(int ch) {
	return indexOf(ch, 0);
    }

    /**
     * Returns the index within this String of the first occurrence of the specified 
     * character, starting the search at fromIndex.  This method 
     * returns -1 if the index is not found.
     * 
     * The search begins at the specified fromIndex and proceeds character by character
     * to the end of the string.
     * 
     * @param ch	the character to search for
     * @param fromIndex	the index to start the search from
     * @return the index of the first occurrence of the character, or -1 if not found
     */
    public int indexOf(int ch, int fromIndex) {
	int max = offset + count;
	char v[] = value;

	for (int i = offset + fromIndex ; i < max ; i++) {
	    if (v[i] == ch) {
		return i - offset;
	    }
	}
	return -1;
    }

    /**
     * Returns the index within this String of the last occurrence of the specified character.
     * The String is searched backwards starting at the last character.
     * This method returns -1 if the index is not found.
     * 
     * This method starts the search from the end of the string and works
     * backward to find the specified character.
     * 
     * @param ch	the character to search for
     * @return the index of the last occurrence of the character, or -1 if not found
     */
    public int lastIndexOf(int ch) {
	return lastIndexOf(ch, count - 1);
    }

    /**
     * Returns the index within this String of the last occurrence of the specified character.
     * The String is searched backwards starting at fromIndex.
     * This method returns -1 if the index is not found.
     * 
     * This method starts the search from the specified fromIndex and works
     * backward to find the specified character.
     * 
     * @param ch	the character to search for
     * @param fromIndex	the index to start the search from
     * @return the index of the last occurrence of the character, or -1 if not found
     */
    public int lastIndexOf(int ch, int fromIndex) {
	int min = offset;
	char v[] = value;
	
	for (int i = offset + ((fromIndex >= count) ? count - 1 : fromIndex) ; i >= min ; i--) {
	    if (v[i] == ch) {
		return i - offset;
	    }
	}
	return -1;
    }

    /**
     * Returns the index within this String of the first occurrence of the specified substring.
     * This method returns -1 if the index is not found.
     * 
     * The search begins at the beginning of the string and proceeds character by character
     * to find the specified substring.
     * 
     * @param str 	the substring to search for
     * @return the index of the first occurrence of the substring, or -1 if not found
     */
    public int indexOf(String str) {
	return indexOf(str, 0);
    }

    /**
     * Returns the index within this String of the first occurrence of the specified substring.
     * The search is started at fromIndex.
     * This method returns -1 if the index is not found.
     * 
     * The search begins at the specified fromIndex and proceeds character by character
     * to find the specified substring.
     * 
     * @param str 	the substring to search for
     * @param fromIndex	the index to start the search from
     * @return the index of the first occurrence of the substring, or -1 if not found
     */
    public int indexOf(String str, int fromIndex) {
	char v1[] = value;
	char v2[] = str.value;
	int max = offset + (count - str.count);
      test:
	for (int i = offset + ((fromIndex < 0) ? 0 : fromIndex); i <= max ; i++) {
	    int n = str.count;
	    int j = i;
	    int k = str.offset;
	    while (n-- != 0) {
		if (v1[j++] != v2[k++]) {
		    continue test;
		}
	    }
	    return i - offset;
	}
	return -1;
    }

    /**
     * Returns the index within this String of the last occurrence of the specified substring.
     * The String is searched backwards.
     * This method returns -1 if the index is not found.
     * 
     * This method starts the search from the end of the string and works
     * backward to find the specified substring.
     * 
     * @param str 	the substring to search for
     * @return the index of the last occurrence of the substring, or -1 if not found
     */
    public int lastIndexOf(String str) {
	return lastIndexOf(str, count - 1);
    }

    /**
     * Returns the index within this String of the last occurrence of the specified substring.
     * The String is searched backwards starting at fromIndex.
     * This method returns -1 if the index is not found.
     * 
     * This method starts the search from the specified fromIndex and works
     * backward to find the specified substring.
     * 
     * @param str 	the substring to search for
     * @param fromIndex	the index to start the search from
     * @return the index of the last occurrence of the substring, or -1 if not found
     */
    public int lastIndexOf(String str, int fromIndex) {
	char v1[] = value;
	char v2[] = str.value;
	int min = offset;
      test:
	for (int i = offset + ((fromIndex >= count) ? count - 1 : fromIndex); i >= min ; i--) {
	    int n = str.count;
	    int j = i;
	    int k = str.offset;
	    while (n-- != 0) {
		if (v1[j++] != v2[k++]) {
		    continue test;
		}
	    }
	    return i - offset;
	}
	return -1;
    }

    /**
     * Returns the substring of this String. The substring is specified
     * by a beginIndex (inclusive) and the end of the string.
     * 
     * This method creates a new String object that contains a subsequence of
     * characters from this string, starting with the character at the specified
     * index and extending to the end of the string.
     * 
     * @param beginIndex the beginning index, inclusive
     * @return the substring starting at beginIndex to the end of the string
     * @exception StringIndexOutOfBoundsException if beginIndex is negative or
     *            greater than the length of this String
     */
    public String substring(int beginIndex) {
	return substring(beginIndex, length());
    }

    /**
     * Returns the substring of a String. The substring is specified
     * by a beginIndex (inclusive) and an endIndex (exclusive).
     * 
     * This method creates a new String object that contains a subsequence of
     * characters from this string, starting with the character at the specified
     * beginIndex and extending to the character at index endIndex - 1.
     * 
     * @param beginIndex the beginning index, inclusive
     * @param endIndex the ending index, exclusive
     * @return the substring from beginIndex to endIndex
     * @exception StringIndexOutOfBoundsException If the beginIndex or the endIndex is out 
     * of range.
     */
    public String substring(int beginIndex, int endIndex) {
	if (beginIndex > endIndex) {
	    int tmp = beginIndex;
	    beginIndex = endIndex;
	    endIndex = tmp;
	}
	if (beginIndex < 0) {
	    throw new StringIndexOutOfBoundsException(beginIndex);
	} 
	if (endIndex > count) {
	    throw new StringIndexOutOfBoundsException(endIndex);
	}
	return ((beginIndex == 0) && (endIndex == count)) ? this :
		   new String(value, offset + beginIndex, endIndex - beginIndex);
    }

    /**
     * Concatenates the specified string to the end of this String.
     * 
     * This method creates a new String that is the combination of this string
     * followed by the specified string. If the specified string has length 0,
     * then the original string is returned.
     * 
     * @param str	the String which is concatenated to the end of this String
     * @return a new String that represents the concatenation of this string and str
     */
    public String concat(String str) {
	int otherLen = str.length();
	if (otherLen == 0) {
	    return this;
	}
	char buf[] = new char[count + otherLen];
	getChars(0, count, buf, 0);
	str.getChars(0, otherLen, buf, count);
	return new String(buf);
    }

    /**
     * Converts this String by replacing all occurences of oldChar with newChar.
     * 
     * This method returns a new string resulting from replacing all occurrences
     * of oldChar in this string with newChar. If the character oldChar does not
     * occur in this string, then a reference to this string is returned.
     * 
     * @param oldChar	the old character to be replaced
     * @param newChar	the new character to replace with
     * @return a new String with all occurrences of oldChar replaced with newChar,
     *         or this string if oldChar doesn't appear in the string
     */
    public String replace(char oldChar, char newChar) {
	if (oldChar != newChar) {
	    int len = count;
	    int i = -1;
	    while (++i < len) {
		if (value[offset + i] == oldChar) {
		    break;
		}
	    }
	    if (i < len) {
		char buf[] = new char[len];
		for (int j = 0 ; j < i ; j++) {
		    buf[j] = value[offset+j];
		}
		while (i < len) {
		    char c = value[offset + i];
		    buf[i] = (c == oldChar) ? newChar : c;
		    i++;
		}
		return new String(buf);
	    }
	}
	return this;
    }

    /**
     * Converts all of the characters in this String to lower case.
     * 
     * This method creates a new String with all characters converted to lowercase
     * according to the rules of the default locale. Characters that are not
     * letters are not modified.
     * 
     * @return the String, converted to lowercase.
     * @see Character#toLowerCase
     * @see String#toUpperCase
     */
    public String toLowerCase() {
	int len = count;
	char trt[] = Character.downCase;
	int i, c;
	for (i = 0 ; i < len ; i++) {
	    c = value[offset+i];
	    if ((c < 256) && (trt[c] != c)) {
		break;
	    }
	}
	if (i >= len) {
	    return this;
	}
	char buf[] = new char[len];
	for (i = 0 ; i < len ; i++) {
	    c = value[offset+i];
	    buf[i] = (c < 256) ? trt[c] : (char)c;
	}
	return new String(buf);
    }

    /**
     * Converts all of the characters in this String to upper case.
     * 
     * This method creates a new String with all characters converted to uppercase
     * according to the rules of the default locale. Characters that are not
     * letters are not modified.
     * 
     * @return the String, converted to uppercase.
     * @see Character#toUpperCase
     * @see String#toLowerCase
     */
    public String toUpperCase() {
	int len = count;
	char trt[] = Character.upCase;
	int i, c;
	for (i = 0 ; i < len ; i++) {
	    c = value[offset+i];
	    if ((c < 256) && (trt[c] != c)) {
		break;
	    }
	}
	if (i >= len) {
	    return this;
	}
	char buf[] = new char[len];
	for (i = 0 ; i < len ; i++) {
	    c = value[offset+i];
	    buf[i] = (c < 256) ? trt[c] : (char)c;
	}
	return new String(buf);
    }

    /**
     * Trims leading and trailing whitespace from this String.
     * 
     * This method creates a new String with leading and trailing whitespace
     * removed. Whitespace is defined as any character whose code is less than
     * or equal to the space character (0x20).
     * 
     * @return the String, with whitespace removed.
     */
    public String trim() {
	int len = count;
	int st = 0;
	while ((st < len) && (value[offset + st] <= ' ')) {
	    st++;
	}
	while ((st < len) && (value[offset + len - 1] <= ' ')) {
	    len--;
	}
	return ((st > 0) || (len < count)) ? substring(st, len) : this;
    }

    /**
     * Converts this String to a String.
     * 
     * This method returns a reference to this string, since strings are immutable
     * and no conversion is needed.
     * 
     * @return the String itself.
     */
    public String toString() {
	return this;
    }

    /**
     * Converts this String to a character array. This creates a new array.
     * 
     * This method creates a newly allocated character array whose length is the
     * length of this string and whose contents are initialized to contain the
     * character sequence represented by this string.
     * 
     * @return 	an array of characters containing the characters of this string.
     */
    public char[] toCharArray() {
	int i, max = length();
	char result[] = new char[max];
	getChars(0, max, result, 0);
	return result;
    }

    /**
     * Returns a String that represents the String value of the object.
     * The object may choose how to represent itself by implementing
     * the toString() method.
     * 
     * If the object is null, the string "null" is returned. Otherwise,
     * the object's toString method is called to get its string representation.
     * 
     * @param obj	the object to be converted
     * @return a string representation of the object
     */
    public static String valueOf(Object obj) {
	return (obj == null) ? "null" : obj.toString();
    }

    /**
     * Returns a String that is equivalent to the specified character array.
     * Uses the original array as the body of the String (ie. it does not
     * copy it to a new array).
     * 
     * This method creates a new String containing the characters in the
     * specified character array.
     * 
     * @param data	the character array
     * @return a new String containing the characters in the character array
     */
    public static String valueOf(char data[]) {
	return new String(data);
    }

    /**
     * Returns a String that is equivalent to the specified character array.
     * 
     * This method creates a new String containing count characters from the
     * specified character array, starting at the specified offset.
     * 
     * @param data	the character array
     * @param offset	the offset into the value of the String
     * @param count 	the length of the value of the String
     * @return a new String containing characters from the character array
     */
    public static String valueOf(char data[], int offset, int count) {
	return new String(data, offset, count);
    }

    
    /**
     * Returns a String that is equivalent to the specified character array.
     * It creates a new array and copies the characters into it.
     * 
     * This method creates a new String containing count characters from the
     * specified character array, starting at the specified offset. Unlike
     * valueOf(char[], int, int), this method creates a copy of the data.
     * 
     * @param data	the character array
     * @param offset	the offset into the value of the String
     * @param count 	the length of the value of the String
     * @return a new String containing a copy of characters from the character array
     */
    public static String copyValueOf(char data[], int offset, int count) {
	char str[] = new char[count];
	System.arraycopy(data, offset, str, 0, count);
	return new String(str);
    }

    /**
     * Returns a String that is equivalent to the specified character array.
     * It creates a new array and copies the characters into it.
     * 
     * This method creates a new String containing all the characters in the
     * specified character array. It's equivalent to calling 
     * copyValueOf(data, 0, data.length).
     * 
     * @param data	the character array
     * @return a new String containing a copy of characters from the character array
     */
    public static String copyValueOf(char data[]) {
	return copyValueOf(data, 0, data.length);
    }

    /**
     * Returns a String object that represents the state of the specified boolean.
     * 
     * The result is "true" if the boolean argument is true, and "false" if
     * the argument is false.
     * 
     * @param b	the boolean
     * @return "true" if the boolean argument is true, "false" otherwise
     */
    public static String valueOf(boolean b) {
	return b ? "true" : "false";
    }

    /**
     * Returns a String object that contains a single character
     * 
     * This method creates a new String of length 1 containing the specified
     * character.
     * 
     * @param c the character
     * @return 	a new String containing the single character.
     */
    public static String valueOf(char c) {
	char data[] = {c};
	return new String(data);
    }

    /**
     * Returns a String object that represents the value of the specified integer.
     * 
     * This method converts the integer to a string representation in base 10.
     * 
     * @param i	the integer
     * @return a string representation of the integer in base 10
     */
    public static String valueOf(int i) {
        return Integer.toString(i, 10);
    }

    /**
     * Returns a String object that represents the value of the specified long.
     * 
     * This method converts the long to a string representation in base 10.
     * 
     * @param l	the long
     * @return a string representation of the long in base 10
     */
    public static String valueOf(long l) {
        return Long.toString(l, 10);
    }

    /**
     * Returns a String object that represents the value of the specified float.
     * 
     * This method converts the float to a string representation.
     * 
     * @param f	the float
     * @return a string representation of the float
     */
    public static String valueOf(float f) {
	return Float.toString(f);
    }

    /**
     * Returns a String object that represents the value of the specified double.
     * 
     * This method converts the double to a string representation.
     * 
     * @param d	the double
     * @return a string representation of the double
     */
    public static String valueOf(double d) {
	return Double.toString(d);
    }


    /**
     * The set of internalized Strings.
     * 
     * This is a cache of strings that have been interned, to ensure that
     * identical string literals refer to the same String object.
     */
    private static Hashtable InternSet;

    /**
     * Returns a String that is equal to this String
     * but which is guaranteed to be from the unique String pool.  For example:
     * <pre>s1.intern() == s2.intern() <=> s1.equals(s2).</pre>
     * 
     * This method maintains a global pool of strings, where each distinct string
     * is represented by a single String object. When intern() is called, if the pool
     * already contains a string equal to this String object, then the string from
     * the pool is returned. Otherwise, this String object is added to the pool and
     * a reference to it is returned.
     * 
     * @return a string that has the same contents as this string, but is guaranteed to
     *         be from a pool of unique strings
     */
    public String intern() {
	if (InternSet == null) {
	    InternSet = new Hashtable();
	}
	String s = (String) InternSet.get(this);
	if (s != null) {
	    return s;
	}
	InternSet.put(this, this);
	return this;
    }

    /**
     * Compute the length of this string's UTF encoded form.
     * 
     * This method calculates how many bytes would be needed to represent this
     * string in UTF-8 encoding. This is used internally for serialization purposes.
     * 
     * @return the number of bytes needed to represent this string in UTF-8 format
     */
    int utfLength() {
	int limit = offset + count;
	int utflen = 0;
	for (int i = offset; i < limit; i++) {
	    int c = value[i];
	    if ((c >= 0x0001) && (c <= 0x007F)) {
		utflen++;
	    } else if (c > 0x07FF) {
		utflen += 3;
	    } else {
		utflen += 2;
	    }
	}
	return utflen;
    }
}
