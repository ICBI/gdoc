class ValidatorMixin {
	
	static doListsOverlap(self, userListService, listOne, listTwo) {
		return userListService.doListsOverlap(listOne, listTwo)
	}
}