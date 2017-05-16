package DataPreprocessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class BagOfWords {

	public HashMap<String, Integer> dictionaryMap;

	public BagOfWords(ArrayList<NoiseRemoval> tList) {

		HashMap<String, Integer> tempMap = new HashMap<>();

		for (NoiseRemoval nr : tList) {
			String str = nr.getString();
			String[] words = str.split("\\s+");
			for (String wrd : words) {
				// wrd = cleanWord(wrd);
				if (!tempMap.containsKey(wrd))
					tempMap.put(wrd, 1);
				else
					tempMap.put(wrd, tempMap.get(wrd) + 1);

			}
		}

		dictionaryMap = tempMap;
	}

	public void print() {
		// Get a set of the entries
		Set set = dictionaryMap.entrySet();
		// Get an iterator
		Iterator i = set.iterator();
		// Display elements
		while (i.hasNext()) {
			Map.Entry me = (Map.Entry) i.next();
			System.out.print(me.getKey() + ": ");
			System.out.println(me.getValue());
		}
		System.out.println();
	}

	public ArrayList<String> getList() {
		ArrayList<String> sList = new ArrayList<>();
		// Get a set of the entries
		Set set = dictionaryMap.entrySet();
		// Get an iterator
		Iterator i = set.iterator();
		// Display elements
		while (i.hasNext()) {
			Map.Entry me = (Map.Entry) i.next();
			if ((int) me.getValue() < 2 || (int) me.getValue() > 1000)
				sList.add(me.getKey().toString());
		}
		return sList;

	}

}
