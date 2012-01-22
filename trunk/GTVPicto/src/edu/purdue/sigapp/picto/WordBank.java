package edu.purdue.sigapp.picto;

import java.util.ArrayList;
import java.util.Random;

public class WordBank {
	
	String hard_words;
	ArrayList<Integer> used;
	Random r;
	
	public WordBank() {
		used = new ArrayList<Integer>();
		r = new Random();
		r.setSeed(System.currentTimeMillis());
	}

	
	public void loadWords() {
		StringBuilder builder = new StringBuilder();
		builder.append("snag jungle important ");
		builder.append("mime peasant baggage ");
		builder.append("hail clog pizza sauce ");
		builder.append("password Heinz-57 scream ");
		builder.append("newsletter bookend pro ");
		builder.append("dripping pharmacist lie ");
		builder.append("catalog ringleader husband ");
		builder.append("laser diagonal comfy ");
		builder.append("myth dorsal biscuit ");
		builder.append("hydrogen macaroni rubber ");
		builder.append("darkness yolk exercise ");
		builder.append("vegetarian shrew chestnut ");
		builder.append("ditch wobble glitter ");
		builder.append("neighborhood dizzy ﬁreside ");
		builder.append("retail drawback logo ");
		builder.append("fabric mirror barber ");
		builder.append("jazz migrate drought ");
		builder.append("commercial dashboard bargain ");
		builder.append("double download professor ");
		builder.append("landscape ski goggles vitamin ");
		// medium words
		builder.append("horse door song ");
		builder.append("trip backbone bomb ");
		builder.append("round treasure garbage ");
		builder.append("park pirate ski ");
		builder.append("state whistle palace ");
		builder.append("baseball coal queen ");
		builder.append("dominoes photograph computer ");
		builder.append("hockey aircraft hot dog ");
		builder.append("salt and pepper key iPad ");
		builder.append("whisk frog lawnmower ");
		builder.append("mattress pinwheel cake ");
		builder.append("circus battery mailman ");
		builder.append("cowboy password bicycle ");
		builder.append("skate electricity lightsaber ");
		builder.append("thief teapot deep ");
		builder.append("spring nature shallow ");
		builder.append("toast outside America ");
		builder.append("roller blading gingerbread man bowtie ");
		builder.append("half spare wax ");
		builder.append("light bulb platypus music ");
		// easy words
		builder.append("cat sun cup ");
		builder.append("ghost ﬂower pie ");
		builder.append("cow banana snowﬂake ");
		builder.append("bug book jar ");
		builder.append("snake light tree ");
		builder.append("lips apple slide ");
		builder.append("socks smile swing ");
		builder.append("coat shoe water ");
		builder.append("heart hat ocean ");
		builder.append("kite dog mouth ");
		builder.append("milk duck eyes ");
		builder.append("skateboard bird boy ");
		builder.append("apple person girl ");
		builder.append("mouse ball house ");
		builder.append("star nose bed ");
		builder.append("whale jacket shirt ");
		builder.append("hippo beach egg ");
		builder.append("face cookie cheese ");
		builder.append("ice cream cone drum circle ");
		builder.append("spoon worm spider web ");
		hard_words = builder.toString();
	}
	
	public String getNextWord() {
		if (used.size() == hard_words.split(" ").length) {
			used.clear();
		}
		
		int next = r.nextInt(hard_words.split(" ").length);
		while (used.contains(new Integer(next))) {
			next = r.nextInt(hard_words.split(" ").length);
		}
		used.add(new Integer(next));
		return hard_words.split(" ")[next];
	}
	
	public void resetUsedWords() {
		used.clear();
	}
	
	
}
