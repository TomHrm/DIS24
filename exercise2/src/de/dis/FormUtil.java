package de.dis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Kleine Helferklasse zum Einlesen von Formulardaten
 */
public class FormUtil {
	/**
	 * Liest einen String vom standard input ein
	 * @param label Zeile, die vor der Eingabe gezeigt wird
	 * @return eingelesene Zeile
	 */
	public static String readString(String label) {
		String ret = null;
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

		try {
			System.out.print(label+": ");
			ret = stdin.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ret;
	}

	public static Date readDate(String label) {
		java.sql.Date ret = null;
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");

		try {
			System.out.print(label+"(dd-MMM-yyyy): ");
			ret = new java.sql.Date(formatter.parse(stdin.readLine()).getTime());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return ret;
	}
	
	/**
	 * Liest einen Integer vom standard input ein
	 * @param label Zeile, die vor der Eingabe gezeigt wird
	 * @return eingelesener Integer
	 */
	public static int readInt(String label) {
		int ret = 0;
		boolean finished = false;

		while(!finished) {
			String line = readString(label);
			
			try {
				ret = Integer.parseInt(line);
				finished = true;
			} catch (NumberFormatException e) {
				System.err.println("Ungültige Eingabe: Bitte geben Sie eine Zahl an!");
			}
		}
		
		return ret;
	}
}
