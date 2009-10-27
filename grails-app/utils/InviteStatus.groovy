public enum InviteStatus {
	PENDING,
	WITHDRAWN, 
	REJECTED,
	ACCEPTED;

	public String value() {
		return name();
	}

}