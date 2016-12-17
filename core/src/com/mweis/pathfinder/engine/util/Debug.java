package com.mweis.pathfinder.engine.util;

public class Debug {
	
	public static boolean isDebugMode = false;

	public static <T> void printCommaSeperated(T ... arr) {
		if (isDebugMode) {
			if (arr.length == 1) {
				System.out.println(arr[0].toString());
			} else {
				System.out.print(arr[0].toString());
				for (int i=1; i < arr.length; i++) {
					System.out.print(", ");
					System.out.print(arr[i].toString());
				}
				System.out.println();
			}
		}
	}
	
	private Debug() { }; // THIS CLASS CAN NOT BE INSTANTIATED
	
}
