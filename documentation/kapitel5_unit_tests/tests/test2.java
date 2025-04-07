@Test 
void testUpdateUserPassword() 
{ 
    // Setup test 
    var service = userService(); 
    var creatingUser = User.createUser(USERNAME, DEFAULT_PASSWORD); 
 
    // user can't be updated before creation 
    Assertions.assertFalse(creatingUser.canBeUpdated()); 
 
    service.createUser(creatingUser); 
 
    // Find user and update password 
    var user = service.findUser(1).orElseThrow(); 
    Assertions.assertTrue(user.canBeUpdated()); 
    user.setHashedPassword(OTHER_PASSWORD); 
    service.updateUserPassword(user); 
 
    // Find user again 
    var sameUser = service.findUser(1).orElseThrow(); 
    Assertions.assertEquals(OTHER_PASSWORD, sameUser.getHashedPassword()); 
} 