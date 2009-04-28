class Tagging {
	static mapping = {
		table 'TAGGING'
	}
	Tag tag
	long taggableId
	String taggableType
	Date dateCreated

	String toString() {
		return tag.name
	}
}
