@Test 
void testFindUserByName()  
{ 
    // Setup test 
    var service = userService(); 
    service.createUser(User.createUser(USERNAME, DEFAULT_PASSWORD)); 
    var test = "hallo";
    // Find user and validate 
    var result = service.findUserWithName(USERNAME).orElseThrow(); 
 
    Assertions.assertEquals(1, result.getId()); 
    Assertions.assertEquals(USERNAME, result.getUsername()); 
    Assertions.assertEquals(DEFAULT_PASSWORD, result.getHashedPassword()); 
}