package practice;

import java.io.FileNotFoundException;
import java.io.IOException;

public class kuir {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int size = args.length;
		
		for(int i = 0; i < args.length - 1; i++) {
			String str = args[i + 1];
			if(args[i].equals("-c")) {
				String input_str = str.substring(2, str.length());
				makeCollection do_fun = new makeCollection(input_str);
				break;
			}
			else if(args[i].equals("-k")) {
				String input_str = str.substring(2, str.length());
				makeKeyword do_fun = new makeKeyword(input_str);
				break;
			}
			else if(args[i].equals("-i")) {
				String input_str = str.substring(2, str.length());
				indexer do_fun = new indexer(input_str);
				break;
			}
			else if(args[i].equals("-s")) {
				String[] input_args = new String[2];
				input_args[0] = str.substring(2, str.length());
				for(int j = i; j < args.length - 1; j++) {
					if(args[j].equals("-q")) {
						String query_str = args[j + 1];
						if(query_str.length() > 2) {
							input_args[1] = query_str.substring(1, query_str.length() - 1);
						}
						input_args[1] = null;
					}
				}
				searcher do_fun = new searcher(input_args);
				break;
			}
		}
	}
}