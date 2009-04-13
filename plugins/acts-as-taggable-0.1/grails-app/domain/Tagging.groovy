class Tagging {
	static mapping = {
		table 'GUIPERSIST.TAGGING'
	}
	Tag tag
	long taggableId
	String taggableType
	Date dateCreated

	String toString() {
		return tag.name
	}
}
