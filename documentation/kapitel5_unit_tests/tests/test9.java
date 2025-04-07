@Test 
void testCreateAndGet() 
{ 
    var service = bankAccountService(); 
 
    service.createBankAccount(BankAccount.create(ModelUtils.existingUser(), NAME)); 
    var account = service.getBankAccount(1).orElseThrow(); 
 
    Assertions.assertEquals(NAME, account.getName()); 
    Assertions.assertFalse(service.getBankAccountsOfUser(ModelUtils.existingUser(), 1).isEmpty()); 
    Assertions.assertFalse(service.getBankAccountOfUserByName(ModelUtils.existingUser(), NAME).isEmpty()); 
} 