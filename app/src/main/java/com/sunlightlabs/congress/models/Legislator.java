package com.sunlightlabs.congress.models;

import java.io.Serializable;

public class Legislator implements Comparable<Legislator>, Serializable {
	private static final long serialVersionUID = 1L;
	
	public String bioguide_id, govtrack_id, thomas_id;
	public String first_name, middle_name, last_name;
	public String nickname, name_suffix; // TODO: remove
	public String title, party, state, district, chamber;
	public String gender, office, website, phone;
	public String twitter_id, youtube_id, facebook_id; 
	public String term_start, term_end, leadership_role;
	public boolean in_office;
	
	// this gets assigned onto the legislator, even though it's not set this way in the API,
	// so that we can reuse legislator listing code to list committee memberships
	public Committee.Membership membership;

		
	public String getName() {
		return firstName() + " " + last_name;
	}
	
	public String firstName() {
		if (nickname != null && nickname.length() > 0)
			return nickname;
		else
			return first_name;
	}
	
	public String titledName() {
		String name = title + ". " + getName();
		if (name_suffix != null && !name_suffix.equals(""))
			name += ", " + name_suffix;
		return name;
	}
	
	public String getOfficialName() {
		return last_name + ", " + firstName();
	}

	public static String[] splitName(String displayName) {
		String[] pieces = displayName.split(" ");
		String first_name = pieces[0];
		String last_name = pieces[pieces.length-1];

		if (
			last_name.equals("Jr.") ||
			last_name.equals("II") ||
			last_name.equals("III")
		)
			last_name = pieces[pieces.length-2] + " " + pieces[pieces.length-1];


		String[] names = {first_name, last_name};
		return names;
	}

	// Used to parse long titles from Pro Publica API
	// TODO: store long title natively and abbreviate at render-time
	public static String shortTitle(String longTitle) {
		if (longTitle.equals("Representative"))
			return "Rep";
		// Can be "Senator, 3rd Class"
		else if (longTitle.startsWith("Senator"))
			return "Sen";
		else if (longTitle.equals("Delegate"))
			return "Del";
		else if (longTitle.equals("Resident Commissioner"));
			return "Com";
	}

	public String fullTitle() {
		String title = this.title;
		if (title.equals("Del"))
			return "Delegate";
		else if (title.equals("Com"))
			return "Resident Commissioner";
		else if (title.equals("Sen"))
			return "Senator";
		else // "Rep"
			return "Representative";
	}
	
	public String getDomain() {
		if (this.chamber.equals("senate"))
			return "Senator";
		else if (district != null && district.equals("0"))
			return "At-Large";
		else
			return "District " + district;
	}
	
	public static String partyName(String party) {
		if (party.equals("D"))
			return "Democrat";
		if (party.equals("R"))
			return "Republican";
		if (party.equals("I"))
			return "Independent";
		else
			return "";
	}
	
	public String getPosition(String stateName) {
		String position = "";

		if (this.chamber.equals("senate"))
			position = "Senator from " + stateName;
		else if (district != null && district.equals("0")) {
			if (title.equals("Rep"))
				position = "Representative for " + stateName + " At-Large";
			else
				position = fullTitle() + " for " + stateName;
		} else
			position = "Representative for " + stateName + "-" + district;

		return "(" + party + ") " + position;
	}

	public static String bioguideUrl(String bioguide_id) {
		return "http://bioguide.congress.gov/scripts/biodisplay.pl?index=" + bioguide_id;
	}
	
	public static String govTrackUrl(String govtrack_id) {
        return "https://www.govtrack.us/congress/members/anything/" + govtrack_id;
	}
	
	public String toString() {
		return titledName();
	}
	
	public int compareTo(Legislator another) {
		return this.last_name.compareTo(another.last_name);
	}

	
	public String twitterUrl() {
		if (this.twitter_id == null || this.twitter_id.equals(""))
			return null;
		return "https://twitter.com/" + this.twitter_id;
	}
	
	public String youtubeUrl() {
		if (this.youtube_id == null || this.youtube_id.equals(""))
			return null;
		return "https://www.youtube.com/" + this.youtube_id;
	}
	
	public String facebookUrl() {
		if (this.facebook_id == null || this.facebook_id.equals(""))
			return null;
		return "https://www.facebook.com/" + this.facebook_id;
	}

}