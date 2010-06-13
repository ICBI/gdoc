package serviceinterfaces;
import java.util.HashSet;

public interface SessionCleanerServiceInt { 
	
	public void cleanup(String userId, HashSet lists, HashSet analyses);
	
	
}