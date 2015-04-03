package com.mayguzer.wikiparser;

import java.net.MalformedURLException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * This class accepts a Wikipedia topic from the user either from command line
 * or from standard input. The class pulls introduction paragraph from wikipedia
 * for the topic and prints it to the output after removing Html tags.
 * 
 * @author Murat Ayguzer
 *
 */
public class WikiParser {
	private static final String WIKI_URL = "http://en.wikipedia.org/wiki/";

	/**
	 * This method accepts a Wikipedia topic from the user either from command
	 * line or from standard input. The method pulls introduction paragraph from
	 * wikipedia for the topic and prints it to the output after removing Html tags.
	 * 
	 * @param args Command line arguments
	 * @throws Exception
	 */
	public static void main(String[] args) {
		// Declare variable
		String topic = "";
		// Create scanner
		Scanner input = new Scanner(System.in);

		// To join string array, Apache Commons Lang has StringUtils.join()
		// method.
		// In this program, instead of using it, I wrote my own.
		// To append strings, instead of plus operator I could use
		// StringBuilder.append()
		// Which is more efficient.

		// If user passed a topic from the command line, set it to the topic
		// variable
		for (int i = 0; i < args.length; i++) {
			if ((args.length - 1) != i) {// Add space until last one
				topic += args[i] + " ";
			} else {
				topic += args[i];
			}
		}

		// Check command line arguments,if topic has already been entered or not
		if (topic == "") {
			// Prompt the user to enter a topic
			System.out.println("Please enter a topic :");
			topic = input.nextLine();
		}
		input.close();

		// If topic has spaces replace with underscore
		topic = topic.replace(' ', '_');
		System.out.println("The topic is " + topic + "\n");

		// Print introduction paragraph for the topic from Wikipedia
		printWikiText(topic);

	}

	/**
	 * This method prints introduction paragraph for the given topic from
	 * Wikipedia by removing Html tags.
	 * 
	 * @param topic The topic to print text for
	 */
	private static void printWikiText(String topic) {
		try {
			// Create a topic URL
			URL url = new URL(WIKI_URL + topic);

			// Check the URL if exists
			boolean urlExist = checkUrlExists(url);
			if (!urlExist) {
				System.out.println("Not Found");
				return;
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					url.openStream()));

			String line;
			boolean contentStarted = false;
			boolean contentEnded = false;
			StringBuilder textBuilder = new StringBuilder();

			while ((line = reader.readLine()) != null) {

				if (!contentStarted)
					contentStarted = line.contains("<p>");

				if (contentStarted) {
					contentEnded = line
							.contains("<div id=\"toc\" class=\"toc\">");

					if (contentEnded)
						break;

					// Remove Html tags
					line = line.replaceAll("\\<.*?>", "");

					textBuilder.append(line);
					textBuilder.append("\n");
				}
			}
			String text = textBuilder.toString();
			// Print out page's introduction
			System.out.println(text);
			reader.close();

		} catch (MalformedURLException e) {
			System.err.println("Caught MalformedURLException: "
					+ e.getMessage());
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Caught IOException: " + "Page Not Found!!! " 
		            + e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * The method checks if the URL exists.
	 * 
	 * @param url The URL to check.
	 * @return Returns true if the URL exists. Otherwise, returns false.
	 */
	public static boolean checkUrlExists(URL url) {
		try {
			url.openConnection();
		} catch (IOException e) {
			return false;
		}

		return true;
	}
}
