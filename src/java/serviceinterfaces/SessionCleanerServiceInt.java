package serviceinterfaces;
import java.util.HashSet;

/** interface responsible for cleaning session data **/

public interface SessionCleanerServiceInt { 
	
	public void cleanup(String userId, HashSet lists, HashSet analyses);
	
	
}