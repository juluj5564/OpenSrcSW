package practice;

public class kuir {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int size = args.length;
		String str = args[size - 1];
		String path = "C:/Users/a/eclipse-workspace/openSource";
		String final_str = path + str.substring(1, str.length());
		
		if(args[size - 2].equals("-c")) {
			makeCollection do_fun = new makeCollection(final_str);
		}
		else if(args[size - 2].equals("-k")) {
			makeKeyword do_fun = new makeKeyword(final_str);
		}
	}
}