import java.lang.Exception

class DuplicateCollaborationGroupException extends Exception{
	DuplicateCollaborationGroupException() {
		super("Collaboration Group with provided name already exists.")
	}
	
}